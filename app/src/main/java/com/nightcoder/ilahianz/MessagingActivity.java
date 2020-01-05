package com.nightcoder.ilahianz;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.Adapters.MessageAdapter;
import com.nightcoder.ilahianz.Databases.ChatDBHelper;
import com.nightcoder.ilahianz.Listeners.DatabaseListener.DataChangeCallbacks;
import com.nightcoder.ilahianz.Models.Chats;
import com.nightcoder.ilahianz.Services.BackService;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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

    public static final String USER_ID_BUFFER = "USER_BUFFER";
    private String userId;
    protected MyApp myApp;
    private String myId;
    private DatabaseReference chatReference, stateReference;
    private RecyclerView recyclerView;
    private TextView status;
    protected ChatDBHelper chatDBHelper;
    private ImageButton sentButton, emojiButton;
    private EmojiEditText message;
    private MediaPlayer mp;
    private final String TAG = "MESSAGE_ACTIVITY";
    protected Context mContext;
    private RelativeLayout toolbarContainer;
    public static String MESSAGE_LINK = "null";
    private EmojiPopup emojiPopup;
    private MessageAdapter messageAdapter;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.toolbar_container:
                    startActivity(new Intent(mContext, UserProfileActivity.class).putExtra(KEY_ID, userId));
                    break;
                case R.id.emoji_btn:
                    if (emojiPopup.isShowing()) {
                        emojiPopup.toggle();
                        emojiButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_insert_emoticon_black_24dp));
                    } else {
                        emojiPopup.toggle();
                        emojiButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_black_24dp));
                    }
                    break;
                case R.id.message:
                    emojiPopup.dismiss();
                    emojiButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_insert_emoticon_black_24dp));
                    break;
                case R.id.btn_sent: {
                    if (!String.valueOf(message.getText()).trim().isEmpty()) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put(SENDER, myId);
                        hashMap.put(RECEIVER, userId);
                        hashMap.put(REFERENCE, "null");
                        hashMap.put(IS_SEEN, false);
                        hashMap.put(IS_DELIVERED, false);
                        hashMap.put(IS_SENT, false);
                        hashMap.put(LINK, "null");
                        hashMap.put(MESSAGE, String.valueOf(message.getText()).trim());
                        sendMessage(hashMap, MESSAGE_LINK);
                    }
                    message.setText("");
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RelativeLayout rootView = findViewById(R.id.root_view);
        initViews();
        init();

        emojiPopup = EmojiPopup.Builder.fromRootView(rootView).build(message);
        message.setOnClickListener(clickListener);
        toolbarContainer.setOnClickListener(clickListener);
        emojiButton.setOnClickListener(clickListener);
        sentButton.setOnClickListener(clickListener);

    }

    private void initViews() {
        message = findViewById(R.id.message);
        sentButton = findViewById(R.id.btn_sent);
        emojiButton = findViewById(R.id.emoji_btn);
        status = findViewById(R.id.status);
        recyclerView = findViewById(R.id.recycler_view);
        toolbarContainer = findViewById(R.id.toolbar_container);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void init() {
        myApp = new MyApp();
        mContext = MessagingActivity.this;
        myApp.setCurrentActivity(this);
        myId = MemorySupports.getUserInfo(this, KEY_ID);
        myApp.setOnline();
        try {
            MemorySupports.setUserInfo(mContext, USER_ID_BUFFER, getIntent().getStringExtra(KEY_ID));
            userId = MemorySupports.getUserInfo(mContext, USER_ID_BUFFER);
        } catch (Exception e) {
            Log.d(TAG, "No Extras");
        }

        myId = MemorySupports.getUserInfo(this, KEY_ID);
        userId = MemorySupports.getUserInfo(mContext, USER_ID_BUFFER);

        stateReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child(KEY_STATUS);
        stateReference.addValueEventListener(stateChangeListener);
        chatDBHelper = new ChatDBHelper(MessagingActivity.this, userId);
        chatReference = FirebaseDatabase.getInstance().getReference(KEY_CHATS);
        updateChats(true, false);
        Log.d(TAG, "UPDATING INIT");
        chatReference = FirebaseDatabase.getInstance()
                .getReference(KEY_CHATS);
        chatReference.addValueEventListener(checkMessage);
    }

    private void sendMessage(final HashMap<String, Object> message, String link) {
        SimpleDateFormat time = new SimpleDateFormat("hh mm aa", Locale.US);
        message.put(TIMESTAMP, time.format(new Date()));
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(KEY_CHATS);
        final String key = reference.push().getKey();
        assert key != null;
        message.put(REFERENCE, key);
        chatDBHelper.insertData(myId, userId, time.format(new Date()), key, link,
                false, false, false, String.valueOf(this.message.getText()));
        updateChats(false, true);
        reference.child(key).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    SQLiteDatabase db = chatDBHelper.getInstance();
                    db.execSQL("UPDATE " + userId + " SET isSent=1 WHERE reference=" + "'" + key + "'");
                    Log.d(TAG, "MESSAGE SENT");
                    updateChats(false, false);
                    Log.d(TAG, "UPDATING SEND_MESSAGE");
                    mp = MediaPlayer.create(MessagingActivity.this, R.raw.tik);
                    mp.start();
                }
            }
        });
    }

    private ValueEventListener checkMessage = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Chats chat = snapshot.getValue(Chats.class);
                        assert chat != null;
                        if (chat.getReceiver().equals(userId) && chat.getSender().equals(myId) ||
                                chat.getReceiver().equals(myId) && chat.getSender().equals(userId)) {

                            if (chat.getReceiver().equals(myId) && !chat.getIsSeen() && chat.getSender().equals(userId)) {
                                chat.setIsSeen(true);
                                DatabaseReference reference = FirebaseDatabase.getInstance()
                                        .getReference(KEY_CHATS).child(chat.getReference());
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put(REFERENCE, chat.getReference());
                                hashMap.put(RECEIVER, chat.getReceiver());
                                hashMap.put(SENDER, chat.getSender());
                                hashMap.put(TIMESTAMP, chat.getTimestamp());
                                hashMap.put(IS_DELIVERED, true);
                                hashMap.put(IS_SENT, true);
                                hashMap.put(IS_SEEN, true);
                                hashMap.put(MESSAGE, chat.getMessage());
                                hashMap.put(LINK, chat.getLink());
                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        chat.setIsSeen(true);
                                        chatDBHelper.insertData(chat.getSender(), chat.getReceiver(), chat.getTimestamp(),
                                                chat.getReference(), chat.getLink(), true, true,
                                                true, chat.getMessage());
                                        Log.d(TAG, "Inserted");
                                        updateChats(false, true);
                                    }
                                });

                            } else if (chat.getReceiver().equals(userId)) {
                                if (chat.getIsDelivered()) {
                                    SQLiteDatabase db = chatDBHelper.getInstance();
                                    db.execSQL("UPDATE " + userId + " SET isDelivered=1 WHERE reference="
                                            + "'" + chat.getReference() + "'");
                                    updateChats(false, false);
                                }
                                if (chat.getIsSeen()) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance()
                                            .getReference(KEY_CHATS).child(chat.getReference());
                                    reference.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                SQLiteDatabase db = chatDBHelper.getInstance();
                                                db.execSQL("UPDATE " + userId + " SET isSeen=1 WHERE reference=" +
                                                        "'" + chat.getReference() + "'");
                                                updateChats(false, false);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }.run();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void updateChats(final boolean animationEnable, final boolean lastAnimation) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "UPDATING CHATS");
                    Cursor cursor = chatDBHelper.getData();
                    List<Chats> mChats = new ArrayList<>();
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

                    messageAdapter = new MessageAdapter(MessagingActivity.this,
                            mChats, animationEnable, lastAnimation);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(messageAdapter);
                        }
                    });
                } catch (SQLiteException e) {
                    Log.d(TAG, "NO TABLE FOUND " + userId);
                }
            }
        });


    }

    private ValueEventListener stateChangeListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String state = dataSnapshot.getValue(String.class);
            int colorFrom = getResources().getColor(R.color.white);
            int colorTo = getResources().getColor(R.color.blue_dark);
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(500);
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    status.setTextColor((int) animation.getAnimatedValue());
                }
            });
            colorAnimation.start();
            status.setText(state);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.message_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear_chat:
                SQLiteDatabase db = chatDBHelper.getInstance();
                db.execSQL("DELETE FROM " + userId);
                chatReference.setValue(null);
                updateChats(false, false);
                break;
            case R.id.menu_info:
                break;
        }
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "RESUMED");
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "RESTARTED");
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "PAUSED");
        myApp.setOffline();
        clearReference();
        chatReference.removeEventListener(checkMessage);
        stateReference.removeEventListener(stateChangeListener);
    }

    @Override
    protected void onDestroy() {
        chatReference.removeEventListener(checkMessage);
        stateReference.removeEventListener(stateChangeListener);
        clearReference();
        myApp.setOffline();
        super.onDestroy();
        Log.d(TAG, "DESTROYED");
    }


//    private void setNotification(String title, String content) {
//        Intent intent = new Intent(mContext, MessagingActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
//                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder b = new NotificationCompat.Builder(mContext);
//        b.setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setTicker("Notification")
//                .setContentTitle(title)
//                .setContentText(content)
//                .setDefaults(Notification.DEFAULT_LIGHTS)
//                .setContentIntent(pendingIntent)
//                .setContentInfo("Info");
//        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        assert notificationManager != null;
//        notificationManager.notify(1, b.build());
//    }
}


