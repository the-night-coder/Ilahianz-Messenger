package com.nightcoder.ilahianz;

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
import com.nightcoder.ilahianz.Adapters.StarredNoticeAdapter;
import com.nightcoder.ilahianz.Models.Notice;
import com.nightcoder.ilahianz.Supports.MemorySupports;

import java.util.ArrayList;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;

public class NoticeStarredActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayout nothing, loading;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_starred);
        recyclerView = findViewById(R.id.recycler_view);
        loading = findViewById(R.id.loading);
        nothing = findViewById(R.id.nothing);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        final ArrayList<Notice> notices = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("StarredNotices")
                .child(MemorySupports.getUserInfo(this, KEY_ID));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notices.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notice notice = snapshot.getValue(Notice.class);
                    notices.add(notice);
                }

                if (notices.isEmpty()) {
                    setNoting();
                } else {
                    StarredNoticeAdapter noticeAdapter = new StarredNoticeAdapter(notices, NoticeStarredActivity.this);
                    recyclerView.setAdapter(noticeAdapter);
                    setRecyclerView();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Notice", "Not Found");
            }
        });
    }
}
