package com.nightcoder.ilahianz.NoticeBoardFragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.Adapters.NoticeAdapter;
import com.nightcoder.ilahianz.Models.Notice;
import com.nightcoder.ilahianz.R;

import java.util.ArrayList;

public class AllFragment extends Fragment{

    private RecyclerView recyclerView;

    private Context mContext;
    private LinearLayout nothing, loading;
    private Handler handler = new Handler(Looper.getMainLooper());
    private NoticeAdapter noticeAdapter;
    private SwipeRefreshLayout refreshLayout;
    private ArrayList<Notice> notices = new ArrayList<>();
    public AllFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        nothing = view.findViewById(R.id.nothing);
        loading = view.findViewById(R.id.loading);
        refreshLayout = view.findViewById(R.id.swipeLayout);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setContents();
            }
        });

        setLoading();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setContents();
            }
        }, 1000);
        return view;
    }

    private void setNoting() {
        loading.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_out));
        loading.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        nothing.setVisibility(View.VISIBLE);
        nothing.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
    }

    private void setLoading() {
        nothing.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_out));
        nothing.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        loading.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
    }

    private void setRecyclerView() {
        nothing.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_out));
        nothing.setVisibility(View.GONE);
        loading.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_out));
        loading.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));

    }

    private void setContents() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notice");
                notices.clear();
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        refreshLayout.setRefreshing(false);
                        if (notices.isEmpty()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Notice notice = snapshot.getValue(Notice.class);
                                notices.add(notice);
                            }
                            if (notices.isEmpty()) {
                                setNoting();
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        noticeAdapter = new NoticeAdapter(notices, mContext);
                                        recyclerView.setAdapter(noticeAdapter);
                                        setRecyclerView();
                                    }
                                });

                            }
                        } else {
                            final ArrayList<Notice> mList = new ArrayList<>();
                            mList.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Notice notice = snapshot.getValue(Notice.class);
                                assert notice != null;
                                mList.add(notice);
                            }
                            if (mList.isEmpty()) {
                                setNoting();
                            } else if (mList.size() < notices.size()) {
                                for (int i = 0; i < notices.size(); i++) {
                                    boolean equal = true;
                                    for (Notice notice : mList) {
                                        if (notices.get(i).getId().equals(notice.getId())) {
                                            equal = true;
                                            break;
                                        } else {
                                            equal = false;
                                        }
                                    }
                                    if (!equal) {
                                        removeItem(i);
                                    }
                                }
                            } else {
                                for (int i = 0; i < mList.size(); i++) {
                                    boolean equal = true;
                                    for (Notice notice : notices) {
                                        if (mList.get(i).getId().equals(notice.getId())) {
                                            equal = true;
                                            break;
                                        } else {
                                            equal = false;
                                        }
                                    }
                                    if (!equal) {
                                        notices.add(mList.get(i));
                                        noticeAdapter.notifyItemInserted(i);
                                        noticeAdapter.notifyItemRangeChanged(i, notices.size());
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("Notice", "Not Found");
                    }
                });
            }
        });

    }

    private void removeItem(int i) {
        notices.remove(i);
        noticeAdapter.notifyItemRemoved(i);
        Log.d("Remove", "Item " + i);
        noticeAdapter.notifyItemRangeChanged(i, notices.size());
    }
}
