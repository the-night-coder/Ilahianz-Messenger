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

import com.nightcoder.ilahianz.MessagingActivity;
import com.nightcoder.ilahianz.Models.UserData;
import com.nightcoder.ilahianz.R;

import java.util.List;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<UserData> mUsers;

    public UserAdapter(Context mContext, List<UserData> users) {
        this.mContext = mContext;
        this.mUsers = users;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        UserData userData = mUsers.get(position);
        holder.username.setText(userData.getUsername());
        final String UID = userData.getId();
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, MessagingActivity.class).putExtra(KEY_ID, UID));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        RelativeLayout container;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            container = itemView.findViewById(R.id.container);
        }
    }
}
