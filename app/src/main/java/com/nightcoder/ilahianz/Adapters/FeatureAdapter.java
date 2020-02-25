package com.nightcoder.ilahianz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nightcoder.ilahianz.BloodConsoleActivity;
import com.nightcoder.ilahianz.Models.FeatureModel;
import com.nightcoder.ilahianz.NoticeBoardActivity;
import com.nightcoder.ilahianz.R;

import java.util.List;

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.ViewHolder> {

    private List<FeatureModel> mData;
    private Context mContext;

    public FeatureAdapter(Context context, List<FeatureModel> data) {
        this.mData = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.feature_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FeatureModel data = mData.get(position);

        holder.imageView.setImageResource(data.getImageId());
        holder.content.setText(data.getContent());
        holder.heading.setText(data.getHeading());
        holder.button.setText(data.getButtonName());
        holder.container.setVisibility(View.VISIBLE);
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.feature_item_animation));

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (data.getImageId()) {
                    case R.mipmap.blood_transfusion:
                        mContext.startActivity(new Intent(mContext, BloodConsoleActivity.class));
                        break;
                    case R.mipmap.megaphone:
                        mContext.startActivity(new Intent(mContext, NoticeBoardActivity.class));
                        break;

                    default:
                        Toast.makeText(mContext, "This feature Coming soon", Toast.LENGTH_SHORT).show();
                        
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout container;
        private Button button;
        private TextView heading, content;
        private ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            button = itemView.findViewById(R.id.btn);
            heading = itemView.findViewById(R.id.heading);
            content = itemView.findViewById(R.id.text);
            imageView = itemView.findViewById(R.id.image);
            container.setVisibility(View.GONE);
        }
    }
}
