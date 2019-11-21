package com.nightcoder.ilahianz.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nightcoder.ilahianz.Models.Chats;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.nightcoder.ilahianz.Supports.ViewSupports;
import com.vanniktech.emoji.EmojiTextView;

import java.util.List;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int CHAT_TYPE_LEFT = 0;
    private static final int CHAT_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chats> mChats;
    private boolean flag;
    private boolean lastAnimation;

    public MessageAdapter(Context mContext, List<Chats> mChats, boolean animation, boolean lastAnimatin) {
        this.mContext = mContext;
        this.mChats = mChats;
        this.fUser = MemorySupports.getUserInfo(mContext, KEY_ID);
        flag = animation;
        this.lastAnimation = lastAnimatin;
    }

    private String fUser;

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CHAT_TYPE_LEFT)
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.chat_item_reciver, parent, false));
        else
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.chat_item_sender, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder holder, int position) {
        Chats chat = mChats.get(position);

        holder.message.setText(chat.getMessage());
        holder.date.setText(chat.getTimestamp());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.dateContainer.getVisibility() == View.VISIBLE)
                    ViewSupports.visibilitySlideAnimation(Gravity.BOTTOM, 600, holder.dateContainer,
                            holder.container, View.GONE);
                else
                    ViewSupports.visibilitySlideAnimation(Gravity.BOTTOM, 600, holder.dateContainer,
                            holder.container, View.VISIBLE);

            }
        });

        if (chat.getIsSeen()) {
            holder.status.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_circle_blue_24dp));
        } else if (chat.getIsDelivered()) {
            holder.status.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_circle_black_24dp));
        } else if (chat.isSent()) {
            holder.status.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_black_24dp));
        }

        if (flag) {
            if (chat.getSender().equals(MemorySupports.getUserInfo(mContext, KEY_ID))) {
                holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.sender_item_animation));
            } else {
                holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.receiver_item_animation));
            }
        }
        if (lastAnimation) {
            if (getItemCount() == position + 1) {
                if (chat.getSender().equals(MemorySupports.getUserInfo(mContext, KEY_ID))) {
                    holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.sender_item_animation));
                } else {
                    holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.receiver_item_animation));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mChats.get(position).getReceiver().equals(fUser))
            return CHAT_TYPE_LEFT;
        else return CHAT_TYPE_RIGHT;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, date;
        EmojiTextView message;
        ImageView status;
        LinearLayout container;
        LinearLayout dateContainer;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            container = itemView.findViewById(R.id.container);
            dateContainer = itemView.findViewById(R.id.date_container);
        }
    }
}
