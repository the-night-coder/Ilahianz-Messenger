package com.nightcoder.ilahianz.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.Models.ChatListModel;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Utils.Time;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private Context mContext;
    private List<ChatListModel> mUsers;

    public ChatListAdapter(Context mContext, List<ChatListModel> users) {
        this.mContext = mContext;
        this.mUsers = users;
    }

    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chat_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListAdapter.ViewHolder holder, int position) {
        ChatListModel model = mUsers.get(position);

        holder.lastMessage.setText(model.getLastMessage());
        holder.time.setText(Time.covertTimeToText(model.getTime()));
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Users").child(model.getId()).child("username");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                holder.name.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imageView;
        private TextView time;
        private TextView name, lastMessage;
        private View online;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            online = itemView.findViewById(R.id.online);
            name = itemView.findViewById(R.id.name);
            lastMessage = itemView.findViewById(R.id.last_message);
            time = itemView.findViewById(R.id.last_time);
            imageView = itemView.findViewById(R.id.profile_image);
        }
    }
}
