package com.nightcoder.ilahianz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.Adapters.UserAdapter;
import com.nightcoder.ilahianz.Databases.Model.UserModel;
import com.nightcoder.ilahianz.Databases.UsersDBHelper;
import com.nightcoder.ilahianz.Models.UserData;
import com.nightcoder.ilahianz.Supports.MemorySupports;

import java.util.ArrayList;
import java.util.List;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;

public class SearchActivity extends AppCompatActivity {
    private Context mContext;
    private ImageView scanButton;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<UserData> mUsers;
    private UsersDBHelper usersDBHelper;
    private ImageButton backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mContext = SearchActivity.this;
        init();
        usersDBHelper = new UsersDBHelper(mContext);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mUsers = new ArrayList<>();

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ScanProfileActivity.class));
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        readUsers();
    }

    private void init() {
        scanButton = findViewById(R.id.scan_button);
        recyclerView = findViewById(R.id.recycler_view);
        backBtn = findViewById(R.id.back_btn);
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
                        ArrayList<UserData> users = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UserData userData = snapshot.getValue(UserData.class);
                            if (userData != null) {
                                if (!userData.getId().equals(MemorySupports.getUserInfo(mContext, KEY_ID))) {
                                    users.add(userData);
                                }
                            }
                        }
                        userAdapter = new UserAdapter(mContext, users);
                        recyclerView.setAdapter(userAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }.run();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
