package com.nightcoder.ilahianz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.CommentActivity;
import com.nightcoder.ilahianz.Models.Comment;
import com.nightcoder.ilahianz.Models.Like;
import com.nightcoder.ilahianz.Models.Notice;
import com.nightcoder.ilahianz.Models.Notification;
import com.nightcoder.ilahianz.Models.UserData;
import com.nightcoder.ilahianz.ProfileActivity;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.nightcoder.ilahianz.UserProfileActivity;
import com.nightcoder.ilahianz.Utils.Time;
import com.vanniktech.emoji.EmojiPopup;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_USERNAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.MESSAGE;
import static com.nightcoder.ilahianz.Literals.StringConstants.TIMESTAMP;
import static com.nightcoder.ilahianz.Models.Notice.TYPE_TEXT;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private List<Notice> mNotices;
    private Context mContext;
    private MediaPlayer mediaPlayer;

    private String id;

    public NoticeAdapter(List<Notice> mNotices, Context mContext) {
        this.mNotices = mNotices;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NoticeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == Notice.TYPE_TEXT) {
//            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_notice_type_text, parent, false));
//        }
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_notice_type_text, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NoticeAdapter.ViewHolder holder, final int position) {
        final Notice notice = mNotices.get(position);
        id = MemorySupports.getUserInfo(mContext, KEY_ID);
        final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(holder.container).build(holder.commentContent);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(notice.getComposerId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                assert userData != null;
                holder.username.setText(userData.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //emoji setup
        holder.emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emojiPopup.isShowing()) {
                    emojiPopup.toggle();
                    holder.emojiButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_insert_emoticon_black_24dp));
                } else {
                    emojiPopup.toggle();
                    holder.emojiButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_keyboard_black_24dp));
                }
            }
        });

        holder.commentContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiPopup.dismiss();
                holder.emojiButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_insert_emoticon_black_24dp));
            }
        });

        //


        holder.time.setText(Time.covertTimeToText(notice.getTimestamp()));
        holder.content.setText(notice.getText());
        holder.container.setVisibility(View.VISIBLE);
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.snackbar_enter_animation));
        holder.target.setText(notice.getTarget());

        if (notice.getSubject().isEmpty()) {
            holder.subject.setVisibility(View.GONE);
        } else
            holder.subject.setText(notice.getSubject());

        holder.thanksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.thanksButtonView.getTag().equals("UNLIKE"))
                    setThanks(notice, holder);
                else setUnThanks(notice.getId(), holder);
            }
        });
        holder.replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyActivity();
            }
        });
        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentActivity(notice.getId());
            }
        });

        holder.starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.starButton.getTag().toString().equals("STARRED"))
                    setUnStarred(notice.getId(), holder);
                else
                    setStarred(notice, holder);
            }
        });

        holder.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment(notice.getId(), holder);
            }
        });

        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.equals(notice.getComposerId()))
                    mContext.startActivity(new Intent(mContext, ProfileActivity.class));
                else
                    mContext.startActivity(new Intent(mContext, UserProfileActivity.class).putExtra(KEY_ID, notice.getComposerId()));
            }
        });
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.equals(notice.getComposerId()))
                    mContext.startActivity(new Intent(mContext, ProfileActivity.class));
                else
                    mContext.startActivity(new Intent(mContext, UserProfileActivity.class).putExtra(KEY_ID, notice.getComposerId()));
            }
        });

        syncThanks(mNotices.get(position).getId(), holder);
        syncComments(mNotices.get(position).getId(), holder);
        syncStarredNotices(mNotices.get(position).getId(), holder);
    }

    private synchronized void syncThanks(final String key, final NoticeAdapter.ViewHolder holder) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                DatabaseReference thanksRef = FirebaseDatabase.getInstance()
                        .getReference("NoticeReaction").child("Thanks").child(key);
                thanksRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int likes = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Like data = snapshot.getValue(Like.class);
                            assert data != null;
                            likes++;
                            if (id.equals(data.getId())) {
                                setThanksRed(holder);
                            }
                        }
                        if (likes != 0) {
                            holder.thanksCount.setText(String.format("%s thanks", likes));
                            holder.thanksCount.setAnimation(AnimationUtils.loadAnimation(mContext,
                                    R.anim.fade_in));
                            holder.thanksCount.setVisibility(View.VISIBLE);
                            //holder.reactContainer.setVisibility(View.VISIBLE);
                        } else {
                            holder.thanksCount.setAnimation(AnimationUtils.loadAnimation(mContext,
                                    R.anim.fade_out));
                            holder.thanksCount.setVisibility(View.GONE);
                            //holder.reactContainer.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }.run();
    }

    private synchronized void syncComments(final String key, final NoticeAdapter.ViewHolder holder) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                DatabaseReference commentRef = FirebaseDatabase.getInstance()
                        .getReference("NoticeReaction").child("Comments").child(key);
                commentRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int comments = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Comment data = snapshot.getValue(Comment.class);
                            assert data != null;
                            comments++;
                        }
                        if (comments != 0) {
                            //holder.reactContainer.setVisibility(View.VISIBLE);
                            holder.commentCount.setText(String.format("%s comments", comments));
                            holder.commentCount.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
                            holder.commentCount.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }.run();
    }

    private synchronized void syncStarredNotices(final String key, final NoticeAdapter.ViewHolder holder) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("StarredNotices").child(id);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Notice notice = snapshot.getValue(Notice.class);
                            assert notice != null;
                            if (notice.getId().equals(key)) {
                                holder.starButton.setImageResource(R.drawable.ic_star_black_24dp);
                                holder.starButton.setTag("STARRED");
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("Notice", "Not Found");
                    }
                });
            }
        }.run();
    }

    private void setStarred(Notice notice, NoticeAdapter.ViewHolder holder) {
        DatabaseReference starredRef = FirebaseDatabase.getInstance().getReference("StarredNotices").child(id);
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put(Notice.KEY_COMPOSER_ID, notice.getComposerId());
        hashMap.put(Notice.KEY_CONTENT_PATH, "null");
        hashMap.put(Notice.KEY_CONTENT_TYPE, TYPE_TEXT);
        hashMap.put(Notice.KEY_SUBJECT, notice.getSubject());
        hashMap.put(Notice.KEY_TEXT, notice.getText());
        hashMap.put(Notice.KEY_TARGET, notice.getTarget());
        hashMap.put(Notice.KEY_TIMESTAMP, notice.getTimestamp());
        hashMap.put(Notice.KEY_ID, notice.getId());

        starredRef.child(notice.getId()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Starred", Toast.LENGTH_SHORT).show();
                    mediaPlayer = MediaPlayer.create(mContext, R.raw.when);
                    mediaPlayer.start();
                }
            }
        });

        holder.starButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_exit_animation));
        holder.starButton.setVisibility(View.GONE);
        holder.starButton.setImageResource(R.drawable.ic_star_black_24dp);
        holder.starButton.setTag("STARRED");
        holder.starButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
        holder.starButton.setVisibility(View.VISIBLE);


    }

    private void setUnStarred(String key, NoticeAdapter.ViewHolder holder) {
        DatabaseReference starredRef = FirebaseDatabase.getInstance().getReference("StarredNotices").child(id);
        starredRef.child(key).setValue(null);

        holder.starButton.setTag("NOT_STARRED");
        holder.starButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_exit_animation));
        holder.starButton.setVisibility(View.GONE);
        holder.starButton.setImageResource(R.drawable.ic_star_border_black_24dp);
        holder.starButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
        holder.starButton.setVisibility(View.VISIBLE);
    }

    private void setThanksRed(NoticeAdapter.ViewHolder holder) {
        holder.thanks.setTextColor(mContext.getResources().getColor(R.color.red_favorite));
        holder.thanksButtonView.setImageResource(R.drawable.ic_favorite_red_24dp);
        holder.thanksButtonView.setTag("THANKED");
    }

    private void setThankGrey(NoticeAdapter.ViewHolder holder) {
        holder.thanks.setTextColor(mContext.getResources().getColor(R.color.dark_grey));
        holder.thanksButtonView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        holder.thanksButtonView.setTag("UNLIKE");
    }

    private void replyActivity() {

    }

    private void commentActivity(String key) {
        mContext.startActivity(new Intent(mContext, CommentActivity.class).putExtra("id", key));
        mediaPlayer = MediaPlayer.create(mContext, R.raw.tik);
        mediaPlayer.start();
    }

    private void setThanks(final Notice notice, final NoticeAdapter.ViewHolder holder) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("NoticeReaction");
        reference.child("Thanks").child(notice.getId()).child(id).child("id").setValue(id)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            syncThanks(notice.getId(), holder);
                            holder.commentContainer.setVisibility(View.VISIBLE);
                            holder.commentContainer.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
                            String message;
                            if (notice.getSubject().toLowerCase().contains("notice")) {
                                 message = " say thanks for your '" + notice.getSubject() + "'";
                            } else {
                                message = " say thanks for your '" + notice.getSubject() + "' notice";
                            }
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Notifications")
                                    .child(notice.getComposerId());

                            String key = reference1.push().getKey();

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", id);
                            hashMap.put("message", message);
                            hashMap.put("type", Notification.TYPE_THANKS);
                            hashMap.put("ref", notice.getId());
                            hashMap.put("timestamp", ServerValue.TIMESTAMP);
                            hashMap.put("seen", false);
                            hashMap.put("key", key);
                            hashMap.put("username", MemorySupports.getUserInfo(mContext, KEY_USERNAME));
                            assert key != null;
                            reference1.child(key).setValue(hashMap);

                        }
                    }
                });
        mediaPlayer = MediaPlayer.create(mContext, R.raw.tik);
        mediaPlayer.start();
        holder.thanksButtonView.setTag("THANKED");
        holder.thanksButtonView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_exit_animation));
        holder.thanksButtonView.setVisibility(View.GONE);
        holder.thanksButtonView.setImageResource(R.drawable.ic_favorite_red_24dp);
        holder.thanksButtonView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
        holder.thanksButtonView.setVisibility(View.VISIBLE);
        setThanksRed(holder);
    }

    private void setUnThanks(final String key, final NoticeAdapter.ViewHolder holder) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("NoticeReaction").child("Thanks").child(key);
        reference.child(id).child("id").setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    syncThanks(key, holder);
                    holder.commentContainer.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_out));
                    holder.commentContainer.setVisibility(View.GONE);
                }
            }
        });
        holder.thanksButtonView.setTag("UNLIKE");
        holder.thanksButtonView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_exit_animation));
        holder.thanksButtonView.setVisibility(View.GONE);
        holder.thanksButtonView.setImageResource(R.drawable.ic_favorite_black_24dp);
        holder.thanksButtonView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
        holder.thanksButtonView.setVisibility(View.VISIBLE);
        setThankGrey(holder);
    }

    private void sendComment(String key, NoticeAdapter.ViewHolder holder) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("NoticeReaction").child("Comments").child(key);

        HashMap<String, Object> hashMap = new HashMap<>();
        String message = Objects.requireNonNull(holder.commentContent.getText()).toString().trim();
        if (!message.isEmpty()) {
            hashMap.put(KEY_ID, id);
            hashMap.put(MESSAGE, message);
            hashMap.put(TIMESTAMP, ServerValue.TIMESTAMP);
            holder.commentContent.setText("");
            reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mediaPlayer = MediaPlayer.create(mContext, R.raw.tik);
                        mediaPlayer.start();
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mNotices.get(position).getContentType();
    }

    @Override
    public int getItemCount() {
        return mNotices.size();
    }

    // View Holders

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView content, username, time, thanks;
        LinearLayout thanksButton, commentButton, replyButton;
        RelativeLayout container;
        ImageView thanksButtonView;
        TextView commentCount, thanksCount;
        ImageButton starButton;
        RelativeLayout commentContainer;
        TextView target;
        EditText commentContent;
        ImageButton emojiButton;
        CircleImageView profileImage;
        ImageButton sendButton;
        TextView subject;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.contents);
            username = itemView.findViewById(R.id.username);
            thanks = itemView.findViewById(R.id.thanks);
            time = itemView.findViewById(R.id.time);
            container = itemView.findViewById(R.id.container);
            container.setVisibility(View.GONE);

            thanksButton = itemView.findViewById(R.id.thanks_button);
            commentButton = itemView.findViewById(R.id.comment_button);
            replyButton = itemView.findViewById(R.id.reply_button);
            commentCount = itemView.findViewById(R.id.comment_count);
            thanksCount = itemView.findViewById(R.id.thanks_count);
            starButton = itemView.findViewById(R.id.star_button);
            target = itemView.findViewById(R.id.target);
            subject = itemView.findViewById(R.id.subject);
            commentContainer = itemView.findViewById(R.id.comment_container);
            sendButton = itemView.findViewById(R.id.btn_sent);
            emojiButton = itemView.findViewById(R.id.emoji_btn);
            commentContent = itemView.findViewById(R.id.comment_edit_text);
            profileImage = itemView.findViewById(R.id.profile_image);
            commentContainer.setVisibility(View.GONE);

            thanksButtonView = itemView.findViewById(R.id.thanks_button_view);
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView content, username, time, thanks;
        LinearLayout thanksButton, commentButton, replyButton;
        RelativeLayout container;
        ImageView thanksButtonView;
        TextView commentCount, thanksCount;
        ImageButton starButton;
        RelativeLayout commentContainer;
        TextView target;
        EditText commentContent;
        ImageButton emojiButton;
        CircleImageView profileImage;
        ImageButton sendButton;
        TextView subject;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.contents);
            username = itemView.findViewById(R.id.username);
            thanks = itemView.findViewById(R.id.thanks);
            time = itemView.findViewById(R.id.time);
            container = itemView.findViewById(R.id.container);
            container.setVisibility(View.GONE);

            thanksButton = itemView.findViewById(R.id.thanks_button);
            commentButton = itemView.findViewById(R.id.comment_button);
            replyButton = itemView.findViewById(R.id.reply_button);
            commentCount = itemView.findViewById(R.id.comment_count);
            thanksCount = itemView.findViewById(R.id.thanks_count);
            starButton = itemView.findViewById(R.id.star_button);
            target = itemView.findViewById(R.id.target);
            subject = itemView.findViewById(R.id.subject);
            commentContainer = itemView.findViewById(R.id.comment_container);
            sendButton = itemView.findViewById(R.id.btn_sent);
            emojiButton = itemView.findViewById(R.id.emoji_btn);
            commentContent = itemView.findViewById(R.id.comment_edit_text);
            profileImage = itemView.findViewById(R.id.profile_image);
            commentContainer.setVisibility(View.GONE);

            thanksButtonView = itemView.findViewById(R.id.thanks_button_view);
        }
    }


}
