package com.nightcoder.ilahianz;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.Adapters.MessageAdapter;
import com.nightcoder.ilahianz.Databases.ChatDBHelper;
import com.nightcoder.ilahianz.Listeners.DatabaseListener.DataChangeCallbacks;
import com.nightcoder.ilahianz.Models.Chats;
import com.nightcoder.ilahianz.Supports.MemorySupports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.nightcoder.ilahianz.Literals.StringConstants.FALSE;
import static com.nightcoder.ilahianz.Literals.StringConstants.IS_DELIVERED;
import static com.nightcoder.ilahianz.Literals.StringConstants.IS_SEEN;
import static com.nightcoder.ilahianz.Literals.StringConstants.IS_SENT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CHATS;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_STATUS;
import static com.nightcoder.ilahianz.Literals.StringConstants.LINK;
import static com.nightcoder.ilahianz.Literals.StringConstants.MESSAGE;
import static com.nightcoder.ilahianz.Literals.StringConstants.RECEIVER;
import static com.nightcoder.ilahianz.Literals.StringConstants.REFERENCE;
import static com.nightcoder.ilahianz.Literals.StringConstants.SENDER;
import static com.nightcoder.ilahianz.Literals.StringConstants.TIMESTAMP;

public class MessagingActivity extends AppCompatActivity implements DataChangeCallbacks {

    private String userId;
    protected MyApp myApp;
    private String myId;
    private DatabaseReference chatReference, stateReference;
    private List<Chats> mChats;
    private MessageAdapter messageAdapter;
    private RecyclerView recyclerView;
    private TextView status;
    protected ChatDBHelper chatDBHelper;
    private ImageButton sentButton;
    private EditText message;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        myApp = new MyApp();
        myId = MemorySupports.getUserInfo(this, KEY_ID);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        handler.post(readMessage);
        init();


        sentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!message.getText().toString().trim().isEmpty()) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(SENDER, myId);
                    hashMap.put(RECEIVER, userId);
                    hashMap.put(REFERENCE, "null");
                    hashMap.put(IS_SEEN, false);
                    hashMap.put(IS_DELIVERED, false);
                    hashMap.put(IS_SENT, false);
                    hashMap.put(LINK, "null");
                    hashMap.put(MESSAGE, message.getText().toString().trim());
                    sendMessage(hashMap, "null");
                    message.setText("");
                }
            }
        });
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
        clearReference();
    }

    private void init() {
        userId = getIntent().getStringExtra(KEY_ID);
        message = findViewById(R.id.message);
        sentButton = findViewById(R.id.btn_sent);
        status = findViewById(R.id.status);
        stateReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child(KEY_STATUS);
        stateReference.addListenerForSingleValueEvent(stateChangeListener);
        chatDBHelper = new ChatDBHelper(this, userId);
        showChats();
    }

    private void sendMessage(final HashMap<String, Object> message, String link) {
        SimpleDateFormat time = new SimpleDateFormat("hh mm aa", Locale.US);
        message.put(TIMESTAMP, time.format(new Date()));
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(KEY_CHATS);
        final String key = reference.push().getKey();
        assert key != null;
        message.put(REFERENCE, key);
        chatDBHelper.insertData(myId, userId, time.format(new Date()), key, link,
                false, false, false, this.message.getText().toString());
        reference.child(key).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    SQLiteDatabase db = chatDBHelper.getInstance();
                    db.execSQL("UPDATE " + userId + " SET isSent='true' WHERE reference=" + "'" + key + "'");
                }
            }
        });
    }

    private ValueEventListener chatListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                final Chats chat = snapshot.getValue(Chats.class);
                assert chat != null;
                if (chat.getReceiver().equals(userId) && chat.getSender().equals(myId) ||
                        chat.getReceiver().equals(myId) && chat.getSender().equals(userId)) {
                    if (chat.getReceiver().equals(myId) && !chat.getIsSeen()) {
                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference(KEY_CHATS).child(chat.getReference()).child(IS_SEEN);
                        reference.setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                chatDBHelper.insertData(chat.getSender(), chat.getReceiver(), chat.getTimestamp(),
                                        chat.getReference(), chat.getLink(), chat.getIsDelivered(), chat.getIsSeen(),
                                        true, chat.getMessage());
                            }
                        });
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void showChats() {
        Cursor cursor = chatDBHelper.getData();
        mChats = new ArrayList<>();
        mChats.clear();
        while (cursor.moveToNext()) {
            Chats chats = new Chats();
            chats.setSender(cursor.getString(ChatDBHelper.SENDER_INDEX));
            chats.setReceiver(cursor.getString(ChatDBHelper.RECEIVER_ID_INDEX));
            chats.setReference(cursor.getString(ChatDBHelper.REFERENCE_INDEX));
            chats.setIsDelivered(cursor.getInt(ChatDBHelper.IS_DELIVERED_INDEX) == 1);
            chats.setIsSeen(cursor.getInt(ChatDBHelper.IS_SEEN_INDEX) == 1);
            chats.setSent(cursor.getInt(ChatDBHelper.IS_SENT_INDEX) == 1);
            chats.setLink(cursor.getString(ChatDBHelper.LINK_INDEX));
            chats.setTimestamp(cursor.getString(ChatDBHelper.TIMESTAMP_INDEX));
            chats.setMessage(cursor.getString(ChatDBHelper.MESSAGE_INDEX));

            mChats.add(chats);
        }

        messageAdapter = new MessageAdapter(MessagingActivity.this, mChats);
        recyclerView.setAdapter(messageAdapter);
    }

    Runnable readMessage = new Runnable() {
        @Override
        public void run() {
            chatReference = FirebaseDatabase.getInstance().getReference(KEY_CHATS);
            chatReference.addValueEventListener(chatListener);
        }
    };


    private ValueEventListener stateChangeListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String state = dataSnapshot.getValue(String.class);
            status.setText(state);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    protected void onDestroy() {
        chatReference.removeEventListener(chatListener);
        stateReference.removeEventListener(stateChangeListener);
        clearReference();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.message_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

    private void clearReference() {
        Activity activity = myApp.getCurrentActivity();
        if (this.equals(activity)) {
            myApp.setCurrentActivity(this);
        }
    }

    @Override
    public void onDataChange() {
        showChats();
    }
}


