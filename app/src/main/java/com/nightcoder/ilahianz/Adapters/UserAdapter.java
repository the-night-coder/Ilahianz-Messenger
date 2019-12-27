package com.nightcoder.ilahianz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nightcoder.ilahianz.Databases.Model.UserModel;
import com.nightcoder.ilahianz.MessagingActivity;
import com.nightcoder.ilahianz.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<UserModel> mUsers;

    public UserAdapter(Context mContext, List<UserModel> users) {
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
        UserModel userData = mUsers.get(position);
        holder.username.setText(userData.getUsername());
        final String UID = userData.getId();
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, MessagingActivity.class).putExtra(KEY_ID, UID));
            }
        });
        holder.container.setVisibility(View.VISIBLE);
        holder.profileImage.setVisibility(View.VISIBLE);
        holder.username.setVisibility(View.VISIBLE);
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
        holder.username.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
        holder.profileImage.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        RelativeLayout container;
        CircleImageView profileImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            container = itemView.findViewById(R.id.container);
            profileImage = itemView.findViewById(R.id.profile_image);
            container.setVisibility(View.GONE);
            profileImage.setVisibility(View.GONE);
            username.setVisibility(View.GONE);
        }
    }
}
