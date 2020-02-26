package com.nightcoder.ilahianz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nightcoder.ilahianz.Models.Notice;
import com.nightcoder.ilahianz.R;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private List<Notice> mNotices;
    private Context mContext;

    public NoticeAdapter(List<Notice> mNotices, Context mContext) {
        this.mNotices = mNotices;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NoticeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Notice.TYPE_TEXT) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_notice_type_text, parent));
        }
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_notice_type_text, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.ViewHolder holder, int position) {

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
        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
