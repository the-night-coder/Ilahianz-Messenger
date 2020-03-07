package com.nightcoder.ilahianz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.CommentActivity;
import com.nightcoder.ilahianz.Models.Like;
import com.nightcoder.ilahianz.Models.Notice;
import com.nightcoder.ilahianz.Models.UserData;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.nightcoder.ilahianz.Utils.Time;

import java.util.List;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;

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
        Notice notice = mNotices.get(position);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(notice.getComposerId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                assert userData != null;
                holder.username.setText(userData.getUsername());
                Log.d("Notice", "getting user data");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.time.setText(Time.covertTimeToText(notice.getTimestamp()));
        holder.content.setText(notice.getText());
        holder.container.setVisibility(View.VISIBLE);
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
        holder.thanksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setThanks(mNotices.get(position).getId());
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
                commentActivity(mNotices.get(position).getId());
            }
        });

        id = MemorySupports.getUserInfo(mContext, KEY_ID);
        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                .getReference("NoticeReaction").child("Thanks").child(mNotices.get(position).getId());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int likes = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Like data = snapshot.getValue(Like.class);
                    assert data != null;
                    likes++;
                    Log.d("Data", data.getId());
                    if (id.equals(data.getId())) {
                        setThanksBlue(holder);
                    }
                }
                if (likes != 0) {
                    Log.d("Likes", String.valueOf(likes));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setThanksBlue(NoticeAdapter.ViewHolder holder) {
        holder.thanks.setTextColor(mContext.getResources().getColor(R.color.blue_dark));
        holder.thanksButtonView.setImageResource(R.drawable.ic_thumb_up_blue_24dp);
    }

    private void replyActivity() {

    }

    private void commentActivity(String key) {
        mContext.startActivity(new Intent(mContext, CommentActivity.class).putExtra("id", key));
        Log.d("KEY", key);
    }

    private void setThanks(String key) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("NoticeReaction");
        reference.child("Thanks").child(key).child(id).child("id").setValue(id)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mediaPlayer = MediaPlayer.create(mContext, R.raw.tik);
                            mediaPlayer.start();
                        }
                    }
                });
    }

    @Override
    public int getItemViewType(int position) {
        return mNotices.get(position).getContentType();
    }

    @Override
    public int getItemCount() {
        return mNotices.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView content, username, time, thanks;
        LinearLayout thanksButton, commentButton, replyButton;
        RelativeLayout container;
        ImageView thanksButtonView;

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

            thanksButtonView = itemView.findViewById(R.id.thanks_button_view);
        }
    }


}
