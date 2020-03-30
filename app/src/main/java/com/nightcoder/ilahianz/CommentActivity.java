package com.nightcoder.ilahianz;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.Adapters.CommentAdapter;
import com.nightcoder.ilahianz.Models.Comment;
import com.nightcoder.ilahianz.Models.Notification;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.MaskTransformation;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_USERNAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.MESSAGE;
import static com.nightcoder.ilahianz.Literals.StringConstants.TIMESTAMP;

public class CommentActivity extends AppCompatActivity {

    private EmojiEditText message;
    private ImageButton emojiButton;
    private ImageButton sendButton;
    private EmojiPopup emojiPopup;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private MediaPlayer mediaPlayer;
    private Context mContext;
    private String TAG = "COMMENT_ACTIVITY";
    private String keyId;
    private String id;
    private String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        init();
        showComments();

        emojiButton.setOnClickListener(clickListener);
        sendButton.setOnClickListener(clickListener);
        message.setOnClickListener(clickListener);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sent:
                    composeComment();
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

            }
        }
    };

    private void showComments() {

        new Thread() {
            @Override
            public void run() {
                super.run();

                final ArrayList<Comment> comments = new ArrayList<>();
                comments.clear();
                DatabaseReference reference = FirebaseDatabase.getInstance()
                        .getReference("Notice").child(keyId).child("Thanks");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (comments.size() == 0) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Comment comment = snapshot.getValue(Comment.class);
                                comments.add(comment);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    commentAdapter = new CommentAdapter(mContext, comments);
                                    recyclerView.setAdapter(commentAdapter);
                                }
                            });
                        } else {
                            int startPos = comments.size();
                            int count = 0;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Comment comment = snapshot.getValue(Comment.class);
                                assert comment != null;
                                boolean same = true;
                                for (int i = 0; i <= startPos - 1; i++) {
                                    if (comments.get(i).getKey().equals(comment.getKey())) {
                                        same = true;
                                        break;
                                    } else {
                                        same = false;
                                    }

                                }

                                if (!same) {
                                    comments.add(comment);
                                    count++;
                                }

                            }
                            commentAdapter.notifyItemRangeInserted(startPos, count);
                            recyclerView.scrollToPosition(comments.size() - 1);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }.run();
    }
    private void composeComment() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Notice").child(keyId).child("Comments");
        String key = reference.push().getKey();
        HashMap<String, Object> hashMap = new HashMap<>();
        final String message = Objects.requireNonNull(this.message.getText()).toString().trim();
        if (!message.isEmpty()) {
            hashMap.put(KEY_ID, MemorySupports.getUserInfo(this, KEY_ID));
            hashMap.put(MESSAGE, message);
            hashMap.put(TIMESTAMP, ServerValue.TIMESTAMP);
            hashMap.put("key", key);
            this.message.setText("");
            assert key != null;
            reference.child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mediaPlayer = MediaPlayer.create(mContext, R.raw.tik);
                        mediaPlayer.start();
                        String messageStr;
                        if (subject.toLowerCase().contains("notice")) {
                            messageStr = " Commented on your : '" + subject + "' : " + message;
                        } else {
                            messageStr = " Commented on your Notice : '" + subject + "' : " + message;
                        }
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Notifications")
                                .child(id);

                        String key1 = reference1.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id", id);
                        hashMap.put("message", messageStr);
                        hashMap.put("type", Notification.TYPE_COMMENT);
                        hashMap.put("ref", keyId);
                        hashMap.put("timestamp", ServerValue.TIMESTAMP);
                        hashMap.put("seen", false);
                        hashMap.put("key", key1);
                        hashMap.put("username", MemorySupports.getUserInfo(mContext, KEY_USERNAME));
                        assert key1 != null;
                        reference1.child(key1).setValue(hashMap);
                    }
                }
            });
        }

    }
    private void init() {
        mContext = CommentActivity.this;
        message = findViewById(R.id.message);
        emojiButton = findViewById(R.id.emoji_btn);
        sendButton = findViewById(R.id.btn_sent);
        keyId = getIntent().getStringExtra("key");
        id = getIntent().getStringExtra("id");
        subject = getIntent().getStringExtra("subject");
        RelativeLayout root = findViewById(R.id.root_view);
        emojiPopup = EmojiPopup.Builder.fromRootView(root).build(message);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
