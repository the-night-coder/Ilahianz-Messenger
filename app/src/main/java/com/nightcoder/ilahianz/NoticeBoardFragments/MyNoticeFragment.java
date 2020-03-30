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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.Adapters.NoticeAdapter;
import com.nightcoder.ilahianz.Models.Notice;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.MemorySupports;

import java.util.ArrayList;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;

public class MyNoticeFragment extends Fragment implements FragmentListener {

    private RecyclerView recyclerView;

    private Context mContext;
    private ArrayList<Notice> notices = new ArrayList<>();
    private NoticeAdapter noticeAdapter;

    private String id;
    private LinearLayout nothing, loading;
    private Handler handler = new Handler(Looper.getMainLooper());
    public MyNoticeFragment(Context context) {
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
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        id = MemorySupports.getUserInfo(mContext, KEY_ID);
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
        recyclerView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_exit_animation));

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
                        if (notices.isEmpty()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Notice notice = snapshot.getValue(Notice.class);
                                assert notice != null;
                                if (notice.getComposerId().equals(id))
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
                                if (notice.getComposerId().equals(id))
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

    @Override
    public void onSyncData() {
        setContents();
    }
}
