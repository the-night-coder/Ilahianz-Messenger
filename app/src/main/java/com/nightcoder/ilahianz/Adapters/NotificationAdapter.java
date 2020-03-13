package com.nightcoder.ilahianz.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.Databases.NotificationDBHelper;
import com.nightcoder.ilahianz.Models.Notification;
import com.nightcoder.ilahianz.Models.UserData;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.ViewSupports;
import com.nightcoder.ilahianz.Utils.Time;
import com.vanniktech.emoji.EmojiTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {


    private Context mContext;
    private List<Notification> mNotifications;

    public NotificationAdapter(Context mContext, List<Notification> mNotifications) {
        this.mContext = mContext;
        this.mNotifications = mNotifications;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.ViewHolder holder, final int position) {
        final Notification notification = mNotifications.get(position);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(notification.getId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                assert userData != null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (notification.getType() == Notification.TYPE_THANKS) {
            holder.container.setBackground(mContext.getDrawable(R.drawable.notification_item_background));
            holder.notType.setImageDrawable(mContext.getDrawable(R.drawable.heart));
        } else if (notification.getType() == Notification.TYPE_COMMENT) {
            holder.container.setBackground(mContext.getDrawable(R.drawable.item_content_background));
            holder.notType.setImageDrawable(mContext.getDrawable(R.drawable.comment));
        }

        if (notification.isSeen()) {
            holder.seenDot.setVisibility(View.GONE);
        } else {
            holder.seenDot.setVisibility(View.VISIBLE);
        }

        holder.notType.setVisibility(View.VISIBLE);
        holder.notType.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
        String message = "<b>" + notification.getUsername() + "</b>" + notification.getMessage();
        holder.content.setText(Html.fromHtml(message));
        holder.time.setText(Time.covertTimeToText(notification.getTimestamp()));
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.snackbar_enter_animation));
        holder.container.setVisibility(View.VISIBLE);

        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = ViewSupports.materialSnackBarDialog(mContext, R.layout.delete_option);
                ImageButton delete = dialog.findViewById(R.id.option_delete);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteItem(position, notification.getKey(), holder);
                        dialog.cancel();
                    }
                });
                dialog.show();
                return true;
            }
        });

    }

    private void deleteItem(int pos, String key, NotificationAdapter.ViewHolder holder) {
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.notification_item_delete_animation));
        holder.container.setVisibility(View.GONE);
        NotificationDBHelper dbHelper = new NotificationDBHelper(mContext);
        dbHelper.deleteData(key);
        mNotifications.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, mNotifications.size());
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        EmojiTextView content;
        TextView time;
        CircleImageView profileImage;
        ImageView notType;
        RelativeLayout container;
        ImageView seenDot;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            profileImage = itemView.findViewById(R.id.profile_image);
            notType = itemView.findViewById(R.id.notification_icon);
            container = itemView.findViewById(R.id.container);
            seenDot = itemView.findViewById(R.id.seen_dot);
            container.setVisibility(View.GONE);
            notType.setVisibility(View.GONE);
        }
    }
}
