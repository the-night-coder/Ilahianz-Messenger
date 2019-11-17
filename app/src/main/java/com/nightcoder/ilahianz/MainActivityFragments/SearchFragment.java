package com.nightcoder.ilahianz.MainActivityFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.Adapters.UserAdapter;
import com.nightcoder.ilahianz.Models.UserData;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.ScanProfileActivity;
import com.nightcoder.ilahianz.Supports.MemorySupports;

import java.util.ArrayList;
import java.util.List;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;

public class SearchFragment extends Fragment {
    private Context mContext;
    private ImageView scanButton;
    private View root;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<UserData> mUsers;

    public SearchFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search, container, false);
        init();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mUsers = new ArrayList<>();

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ScanProfileActivity.class));
            }
        });
        readUsers();
        return root;
    }

    private void init() {
        scanButton = root.findViewById(R.id.scan_button);
        recyclerView = root.findViewById(R.id.recycler_view);
    }

    private void readUsers() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mUsers.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UserData userData = snapshot.getValue(UserData.class);
                            if (userData != null) {
                                if (!userData.getId().equals(MemorySupports.getUserInfo(mContext, KEY_ID))) {
                                    mUsers.add(userData);
                                }
                            }
                        }

                        userAdapter = new UserAdapter(mContext, mUsers);
                        recyclerView.setAdapter(userAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }.run();

    }

    private void setUserData() {

    }
}
