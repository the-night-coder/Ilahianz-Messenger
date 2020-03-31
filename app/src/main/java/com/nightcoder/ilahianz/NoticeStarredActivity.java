package com.nightcoder.ilahianz;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.Adapters.NoticeAdapter;
import com.nightcoder.ilahianz.Models.Notice;
import com.nightcoder.ilahianz.Supports.MemorySupports;

import java.util.ArrayList;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;

public class NoticeStarredActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayout nothing, loading;
    private Handler handler = new Handler();
    private ArrayList<Notice> notices = new ArrayList<>();
    private NoticeAdapter noticeAdapter;
    protected Context mContext;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_starred);
        mContext = NoticeStarredActivity.this;
        recyclerView = findViewById(R.id.recycler_view);
        id = MemorySupports.getUserInfo(mContext, KEY_ID);
        loading = findViewById(R.id.loading);
        nothing = findViewById(R.id.nothing);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        setLoading();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadContents();
            }
        }, 2000);
    }

    private void setNoting() {
        loading.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
        loading.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        nothing.setVisibility(View.VISIBLE);
        nothing.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
    }

    private void setLoading() {
        nothing.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
        nothing.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        loading.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
    }

    private void setRecyclerView() {
        nothing.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
        nothing.setVisibility(View.GONE);
        loading.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
        loading.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
    }

    private void loadContents() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("StarredNotices").child(id);
                notices.clear();
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
