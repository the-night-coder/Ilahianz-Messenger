package com.nightcoder.ilahianz.NoticeBoardFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;

public class AllFragment extends Fragment implements RefershListener {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void setContents() {

        final ArrayList<Notice> notices = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notice");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notices.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notice notice = snapshot.getValue(Notice.class);
                    notices.add(notice);
                }
                NoticeAdapter noticeAdapter = new NoticeAdapter(notices, getContext());
                recyclerView.setAdapter(noticeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Notice", "Not Found");
            }
        });


    }

    @Override
    public void onRefreshed() {
        setContents();
    }
}
