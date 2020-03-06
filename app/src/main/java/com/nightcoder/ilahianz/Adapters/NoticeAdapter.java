package com.nightcoder.ilahianz.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.Models.Notice;
import com.nightcoder.ilahianz.Models.UserData;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.MemorySupports;

import java.util.HashMap;
import java.util.List;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private List<Notice> mNotices;
    private Context mContext;

    private String key;
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
    public void onBindViewHolder(@NonNull final NoticeAdapter.ViewHolder holder, int position) {
        Notice notice = mNotices.get(position);
        key = notice.getId();
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

        holder.content.setText(notice.getText());
        holder.container.setVisibility(View.VISIBLE);
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
        holder.thanksButton.setOnClickListener(clickListener);
        holder.replyButton.setOnClickListener(clickListener);
        holder.commentButton.setOnClickListener(clickListener);
        id = MemorySupports.getUserInfo(mContext, KEY_ID);
        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                .getReference("NoticeReactions").child("Thanks").child(key);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String data = snapshot.getValue(String.class);
                    assert data != null;
                    Log.d("Data", data);
                    if (id.equals(data)) {
                        setThanksBlue(holder);
                    } else {
                        setDislike(holder);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setThanksBlue(NoticeAdapter.ViewHolder holder) {
        holder.thanks.setTextColor(mContext.getResources().getColor(R.color.blue_dark));
        holder.thanksButtonView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp));
    }

    private void setDislike(NoticeAdapter.ViewHolder holder) {
        holder.thanks.setTextColor(mContext.getResources().getColor(R.color.dark_grey));
        holder.thanksButtonView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.thanks_button:
                    setThanks();
                    break;
                case R.id.comment_button:
                    commentActivity();
                    break;
                case R.id.reply_button:
                    replyActivity();
                    break;

            }
        }
    };

    private void replyActivity() {

    }

    private void commentActivity() {

    }

    private void setThanks() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("NoticeReaction");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        reference.child("Thanks").child(key).child(id).setValue(hashMap);
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
        ImageButton thanksButtonView;

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
