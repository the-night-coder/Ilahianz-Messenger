package com.nightcoder.ilahianz.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.MessagingActivity;
import com.nightcoder.ilahianz.Models.ChatListModel;
import com.nightcoder.ilahianz.Models.UserData;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Utils.Time;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;

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
        final ChatListModel model = mUsers.get(position);

        holder.lastMessage.setText(model.getLastMessage());
        holder.time.setText(Time.covertTimeToText(model.getTime()));
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Users").child(model.getId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                assert userData != null;
                holder.name.setText(userData.getUsername());
                if (userData.getStatus().equals("Active")) {
                    holder.online.setVisibility(View.VISIBLE);
                } else {
                    holder.online.setVisibility(View.GONE);
                }
                if (userData.getThumbnailURL().equals("default")) {
                    holder.imageView.setImageResource(R.drawable.ic_person);
                } else {
                    Picasso.with(mContext).load(userData.getThumbnailURL()).into(holder.imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, MessagingActivity.class).putExtra(KEY_ID, model.getId()));
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
        private RelativeLayout container;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            online = itemView.findViewById(R.id.online);
            name = itemView.findViewById(R.id.name);
            lastMessage = itemView.findViewById(R.id.last_message);
            time = itemView.findViewById(R.id.last_time);
            imageView = itemView.findViewById(R.id.profile_image);
            container = itemView.findViewById(R.id.container);
        }
    }
}
