package com.nightcoder.ilahianz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.Supports.MemorySupports;

import java.util.HashMap;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CHATS;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CHAT_LISTS;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Literals.StringConstants.REFERENCE;

public class MessagingActivity extends AppCompatActivity {

    private String userId;
    protected MyApp myApp;
    private String myId;
    private DatabaseReference chatReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        myApp = new MyApp();
        myId = MemorySupports.getUserInfo(this, KEY_ID);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myApp.setCurrentActivity(this);
        myApp.setOnline();
        myId = MemorySupports.getUserInfo(this, KEY_ID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        myApp.setOffline();
    }

    private void init() {
        userId = getIntent().getStringExtra(KEY_ID);
    }

    private void updateChildren(DatabaseReference reference, HashMap<String, Object> hashMap) {
        reference.getRef().updateChildren(hashMap);
    }

    private void sendMessage(final HashMap message) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(KEY_CHATS);
        reference.child(userId);
        reference.push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //
                    String key = reference.getKey();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(REFERENCE, key);
                    updateChildren(reference, hashMap);
                    //
                }
            }
        });
    }

    private ValueEventListener chatListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    Runnable readMessage = new Runnable() {
        @Override
        public void run() {
            chatReference = FirebaseDatabase.getInstance().getReference(KEY_CHATS).child(myId);
            chatReference.addValueEventListener(chatListener);
        }
    };

    @Override
    protected void onDestroy() {
//        chatReference.removeEventListener(chatListener);
        super.onDestroy();
    }
}


