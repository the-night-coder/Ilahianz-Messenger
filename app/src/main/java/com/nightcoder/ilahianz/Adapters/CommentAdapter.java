package com.nightcoder.ilahianz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.Models.Comment;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Utils.Time;
import com.vanniktech.emoji.EmojiTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_USERNAME;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context mContext;
    private List<Comment> mComments;

    public CommentAdapter(Context mContext, List<Comment> mComments) {
        this.mContext = mContext;
        this.mComments = mComments;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentAdapter.ViewHolder holder, int position) {
        Comment comment = mComments.get(position);
        holder.message.setText(comment.getMessage());
        holder.time.setText(Time.getTextShortTime(comment.getTimestamp()));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(comment.getId()).child(KEY_USERNAME);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);
                holder.username.setText(username);
                holder.profileImage.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
                holder.profileImage.setVisibility(View.VISIBLE);
                holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.receiver_item_animation));
                holder.container.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private EmojiTextView message;
        private TextView time, username;
        private CircleImageView profileImage;
        private LinearLayout container;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            profileImage = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.username);
            container = itemView.findViewById(R.id.comment_container);
            container.setVisibility(View.GONE);
            profileImage.setVisibility(View.GONE);
        }
    }
}
