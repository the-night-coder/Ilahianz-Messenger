package com.nightcoder.ilahianz;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.nightcoder.ilahianz.Adapters.MessageAdapter;
import com.nightcoder.ilahianz.Listeners.DatabaseListener.DataChangeCallbacks;
import com.nightcoder.ilahianz.Models.Chats;
import com.nightcoder.ilahianz.Models.UserData;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.nightcoder.ilahianz.Supports.Network;
import com.nightcoder.ilahianz.Supports.ViewSupports;
import com.nightcoder.ilahianz.Utils.FileUtils;
import com.squareup.picasso.Picasso;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.nightcoder.ilahianz.Literals.IntegerConstants.IMAGE_REQUEST;
import static com.nightcoder.ilahianz.Literals.StringConstants.IS_DELIVERED;
import static com.nightcoder.ilahianz.Literals.StringConstants.IS_SEEN;
import static com.nightcoder.ilahianz.Literals.StringConstants.IS_SENT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CHATS;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Literals.StringConstants.LINK;
import static com.nightcoder.ilahianz.Literals.StringConstants.MESSAGE;
import static com.nightcoder.ilahianz.Literals.StringConstants.RECEIVER;
import static com.nightcoder.ilahianz.Literals.StringConstants.REFERENCE;
import static com.nightcoder.ilahianz.Literals.StringConstants.SENDER;
import static com.nightcoder.ilahianz.Literals.StringConstants.TIMESTAMP;
import static com.nightcoder.ilahianz.Models.Notice.TYPE_AUDIO;
import static com.nightcoder.ilahianz.Models.Notice.TYPE_DOC;
import static com.nightcoder.ilahianz.Models.Notice.TYPE_IMAGE;
import static com.nightcoder.ilahianz.Models.Notice.TYPE_TEXT;

public class MessagingActivity extends AppCompatActivity implements DataChangeCallbacks {

    public static final String USER_ID_BUFFER = "USER_BUFFER";
    private static final int DOC_REQUEST = 123;
    private String userId;
    protected MyApp myApp;
    private String myId;
    private DatabaseReference chatReference, stateReference;
    private RecyclerView recyclerView;
    private TextView status, username;
    private CircleImageView profileImage;
    private ImageButton sentButton, backButton,
            emojiButton, cameraButton, attachButton;
    private EmojiEditText message;
    private MediaPlayer mp;
    private final String TAG = "MESSAGE_ACTIVITY";
    protected Context mContext;
    private RelativeLayout toolbarContainer;
    public static String MESSAGE_LINK = "null";
    private EmojiPopup emojiPopup;
    private ArrayList<Chats> chats;
    private MessageAdapter messageAdapter;
    private int type = TYPE_TEXT;
    private ImageView mediaImage;
    private ImageButton closeMedia;
    private TextView mediaName;
    private TextView mediaSize;
    private Uri attachUri;
    private RelativeLayout mediaContainer;
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
                case R.id.btn_sent:
                    getMessage();
                    break;
                case R.id.back_btn:
                    onBackPressed();
                    break;
                case R.id.camera_btn:
                    openGallery();
                    break;
                case R.id.attach_btn:
                    openDocument();
                    break;
                case R.id.media_close:
                    type = TYPE_TEXT;
                    mediaContainer.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_down));
                    mediaContainer.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private void openDocument() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, DOC_REQUEST);
            } else ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, DOC_REQUEST);
        } else {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, DOC_REQUEST);
        }
    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGE_REQUEST);
            } else ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_REQUEST);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, IMAGE_REQUEST);
        }
    }

    private void getMessage() {
        if (type == TYPE_TEXT) {
            if (!String.valueOf(message.getText()).trim().isEmpty()) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(SENDER, myId);
                hashMap.put(RECEIVER, userId);
                hashMap.put(REFERENCE, "null");
                hashMap.put(IS_SEEN, 1);
                hashMap.put(IS_DELIVERED, 1);
                hashMap.put(IS_SENT, 1);
                hashMap.put("url", "null");
                hashMap.put("type", type);
                hashMap.put(LINK, "null");
                hashMap.put(MESSAGE, String.valueOf(message.getText()).trim());
                sendMessage(hashMap);
            }
            message.setText("");
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(SENDER, myId);
            hashMap.put(RECEIVER, userId);
            hashMap.put(REFERENCE, "null");
            hashMap.put(IS_SEEN, 1);
            hashMap.put(IS_DELIVERED, 1);
            hashMap.put(IS_SENT, 1);
            hashMap.put("url", "null");
            hashMap.put("type", type);
            hashMap.put(LINK, "null");
            hashMap.put(MESSAGE, String.valueOf(message.getText()).trim());
            sendMedia(hashMap);
        }
    }

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
        attachButton.setOnClickListener(clickListener);
        cameraButton.setOnClickListener(clickListener);
        backButton.setOnClickListener(clickListener);
        closeMedia.setOnClickListener(clickListener);

    }

    private void initViews() {
        message = findViewById(R.id.message);
        sentButton = findViewById(R.id.btn_sent);
        emojiButton = findViewById(R.id.emoji_btn);
        status = findViewById(R.id.status);
        recyclerView = findViewById(R.id.recycler_view);
        toolbarContainer = findViewById(R.id.toolbar_container);
        cameraButton = findViewById(R.id.camera_btn);
        attachButton = findViewById(R.id.attach_btn);
        backButton = findViewById(R.id.back_btn);
        username = findViewById(R.id.username);
        profileImage = findViewById(R.id.profile_image);
        mediaImage = findViewById(R.id.media_image);
        mediaName = findViewById(R.id.media_name);
        mediaSize = findViewById(R.id.media_size);
        closeMedia = findViewById(R.id.media_close);
        mediaContainer = findViewById(R.id.media_container);
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
        chats = new ArrayList<>();
        try {
            MemorySupports.setUserInfo(mContext, USER_ID_BUFFER, getIntent().getStringExtra(KEY_ID));
            userId = MemorySupports.getUserInfo(mContext, USER_ID_BUFFER);
        } catch (Exception e) {
            Log.d(TAG, "No Extras");
        }

        myId = MemorySupports.getUserInfo(this, KEY_ID);
        userId = MemorySupports.getUserInfo(mContext, USER_ID_BUFFER);

        stateReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        stateReference.addValueEventListener(stateChangeListener);
        chatReference = FirebaseDatabase.getInstance().getReference(KEY_CHATS);
        Log.d(TAG, "UPDATING INIT");
        chatReference = FirebaseDatabase.getInstance()
                .getReference(KEY_CHATS);
        chatReference.addValueEventListener(checkMessage);

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TYPE_TEXT == type) {
                    if (count == 0 && s.toString().trim().isEmpty()) {
                        sentButtonAnimation(R.drawable.ic_mic_black_24dp);
                        cameraButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
                        cameraButton.setVisibility(View.VISIBLE);
                        attachButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
                        attachButton.setVisibility(View.VISIBLE);
                    } else if (before == 0) {
                        sentButtonAnimation(R.drawable.ic_send_black_24dp);
                        cameraButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_exit_animation));
                        cameraButton.setVisibility(View.GONE);
                        attachButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_exit_animation));
                        attachButton.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void sentButtonAnimation(int drawable) {
        sentButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_exit_animation));
        sentButton.setVisibility(View.INVISIBLE);
        sentButton.setImageDrawable(getResources().getDrawable(drawable));
        sentButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
        sentButton.setVisibility(View.VISIBLE);
    }

    private void sendMessage(final HashMap<String, Object> message) {
        SimpleDateFormat time = new SimpleDateFormat("hh mm aa", Locale.US);
        message.put(TIMESTAMP, time.format(new Date()));
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(KEY_CHATS);
        final String key = reference.push().getKey();
        assert key != null;
        message.put(REFERENCE, key);
        reference.child(key).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "MESSAGE SENT");
                    mp = MediaPlayer.create(MessagingActivity.this, R.raw.tik);
                    mp.start();
                    createChatList(userId, myId, Objects.requireNonNull(message.get(MESSAGE)).toString());
                    reference.child(key).child("isSent").setValue(ServerValue.TIMESTAMP);
                }
            }
        });
    }

    private void sendMedia(final HashMap<String, Object> message) {
        SimpleDateFormat time = new SimpleDateFormat("hh mm aa", Locale.US);
        message.put(TIMESTAMP, time.format(new Date()));
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(KEY_CHATS);
        final String key = reference.push().getKey();
        assert key != null;
        message.put(REFERENCE, key);
        uploadMedia(attachUri, message, key, reference);
    }

    public void uploadMedia(Uri uri, final HashMap<String, Object> message, final String key, final DatabaseReference reference) {
        if (Network.Connected(this)) {
            final Dialog dialog = ViewSupports.materialSnackBarDialog(mContext, R.layout.upload_task_progress);
            Button cancel = dialog.findViewById(R.id.option_close);
            dialog.setCancelable(false);
            cancel.setVisibility(View.GONE);
            dialog.setCancelable(false);
            dialog.show();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference("Chats");
            final StorageReference fileReference;
            if (type == TYPE_IMAGE) {
                fileReference = storageReference.child(myId).child(key + ".jpg");
                message.put("type", TYPE_IMAGE);
            } else if (type == TYPE_AUDIO) {
                fileReference = storageReference.child(myId).child(key + ".3gp");
                message.put("type", TYPE_AUDIO);
            } else if (type == TYPE_DOC) {
                fileReference = storageReference.child(myId).child(key + ".pdf");
                message.put("type", TYPE_DOC);
            } else {
                fileReference = storageReference.child(myId).child(key + ".jpg");
                message.put("type", TYPE_IMAGE);
            }
            StorageTask uploadTask = fileReference.putFile(uri);

            //noinspection unchecked
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = (Uri) task.getResult();
                        assert downloadUri != null;
                        String mUri = downloadUri.toString();
                        message.put("url", mUri);
                        assert key != null;
                        reference.child(key).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    reference.child(key).child("isSent").setValue(ServerValue.TIMESTAMP);
                                    mp = MediaPlayer.create(MessagingActivity.this, R.raw.tik);
                                    mp.start();
                                    createChatList(userId, myId, Objects.requireNonNull(message.get(MESSAGE)).toString());
                                }
                            }
                        });
                        dialog.cancel();
                    } else {
                        dialog.cancel();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.cancel();
                }
            });
        } else {
            Toast.makeText(this, "Connect to the Internet !", Toast.LENGTH_SHORT).show();
        }
    }



    private void createChatList(String uid, String id, String lMessage) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatList").child(uid);
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ChatList").child(id);
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("time", ServerValue.TIMESTAMP);
        hashMap.put("id", id);
        hashMap.put("lastMessage", lMessage);
        HashMap<String, Object> hashMap1 = new HashMap<>();
        hashMap1.put("time", ServerValue.TIMESTAMP);
        hashMap1.put("id", uid);
        hashMap1.put("lastMessage", lMessage);

        reference.child(id).setValue(hashMap);
        reference2.child(uid).setValue(hashMap1);
    }

    private ValueEventListener checkMessage = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference(KEY_CHATS);
                    if (chats.isEmpty()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final Chats chat = snapshot.getValue(Chats.class);
                            assert chat != null;
                            if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId) ||
                                    chat.getReceiver().equals(userId) && chat.getSender().equals(myId)) {
                                chats.add(chat);
                                if (chat.getSender().equals(userId))
                                    reference.child(chat.getReference()).child("isSeen").setValue(ServerValue.TIMESTAMP);
                            }
                            if (!chats.isEmpty()) {
                                messageAdapter = new MessageAdapter(mContext, chats);
                                recyclerView.setAdapter(messageAdapter);
                            }
                        }
                    } else {
                        int startPos = chats.size();
                        int count = 0;
                        ArrayList<Chats> chats1 = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Chats chat = snapshot.getValue(Chats.class);
                            assert chat != null;
                            if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId) ||
                                    chat.getReceiver().equals(userId) && chat.getSender().equals(myId)) {
                                chats1.add(chat);
                            }
                        }
                        if (chats1.size() == chats.size()) {
                            chats.clear();
                            chats.addAll(chats1);
                            messageAdapter.notifyDataSetChanged();
                        } else {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Chats chat = snapshot.getValue(Chats.class);
                                assert chat != null;
                                boolean same = true;
                                for (int i = 0; i <= startPos - 1; i++) {
                                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId) ||
                                            chat.getReceiver().equals(userId) && chat.getSender().equals(myId)) {
                                        if (chats.get(i).getReference().equals(chat.getReference())) {
                                            same = true;
                                            break;
                                        } else {
                                            same = false;
                                        }
                                    }

                                }
                                if (!same) {
                                    chats.add(chat);
                                    if (chat.getSender().equals(userId))
                                        reference.child(chat.getReference()).child("isSeen").setValue(ServerValue.TIMESTAMP);
                                    count++;
                                }
                            }
                            messageAdapter.notifyItemRangeInserted(startPos, count);
                            recyclerView.scrollToPosition(chats.size() - 1);
                        }
                    }
                }
            }.run();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private ValueEventListener stateChangeListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            UserData userData = dataSnapshot.getValue(UserData.class);
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
            assert userData != null;
            status.setText(userData.getStatus());
            username.setText(userData.getUsername());
            if (userData.getThumbnailURL().equals("default")) {
                profileImage.setImageResource(R.drawable.ic_person);
            } else {
                Picasso.with(mContext).load(userData.getThumbnailURL()).into(profileImage);
            }
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
        clearReference();
        chatReference.removeEventListener(checkMessage);
        stateReference.removeEventListener(stateChangeListener);
    }

    @Override
    protected void onDestroy() {
        chatReference.removeEventListener(checkMessage);
        stateReference.removeEventListener(stateChangeListener);
        clearReference();
        super.onDestroy();
        Log.d(TAG, "DESTROYED");
    }

    private boolean checkPermission(String permission) {
        return (ContextCompat.checkSelfPermission(mContext, permission)
                == PackageManager.PERMISSION_GRANTED);

    }

    private void askPermission(int requestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Uri uri = data.getData();
                assert uri != null;
                try {
                    compressImage(FileUtils.from(mContext, uri));
                    setAttach(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Attachment failed !", Toast.LENGTH_SHORT).show();
                }
            } else {
                askPermission(IMAGE_REQUEST);
            }
        } else if (requestCode == DOC_REQUEST && resultCode == RESULT_OK && data != null) {
            type = TYPE_DOC;
            setAttach(data.getData());
            attachUri = data.getData();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void compressImage(File actualImage) {
        if (actualImage != null) {
            final ProgressDialog pd = new ProgressDialog(mContext);
            pd.setMessage("Compressing...");
            pd.show();
            new Compressor(this)
                    .setQuality(50)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFileAsFlowable(actualImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            pd.dismiss();
                            type = TYPE_IMAGE;
                            setAttach(Uri.fromFile(file));
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                            pd.dismiss();
                        }
                    });

        }
    }

    private void setAttach(Uri fromFile) {
        if (type == TYPE_IMAGE) {
            mediaContainer.setVisibility(View.VISIBLE);
            mediaContainer.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_up));
            Picasso.with(mContext).load(fromFile).into(mediaImage);
            FileUtils.FileDetail fileDetail = FileUtils.getFileDetailFromUri(mContext, fromFile);
            mediaName.setText(fileDetail.fileName);
            mediaSize.setText(FileUtils.getFileSize(fileDetail.fileSize));
            attachUri = fromFile;
            sentButtonAnimation(R.drawable.ic_send_black_24dp);
            cameraButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_exit_animation));
            cameraButton.setVisibility(View.GONE);
            attachButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_exit_animation));
            attachButton.setVisibility(View.GONE);
        } else if (type == TYPE_DOC) {
            mediaContainer.setVisibility(View.VISIBLE);
            mediaContainer.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_up));
            mediaImage.setImageDrawable(getResources().getDrawable(R.mipmap.doc));
            FileUtils.FileDetail fileDetail = FileUtils.getFileDetailFromUri(mContext, fromFile);
            mediaName.setText(fileDetail.fileName);
            mediaSize.setText(FileUtils.getFileSize(fileDetail.fileSize));
            attachUri = fromFile;
            sentButtonAnimation(R.drawable.ic_send_black_24dp);
            cameraButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_exit_animation));
            cameraButton.setVisibility(View.GONE);
            attachButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_exit_animation));
            attachButton.setVisibility(View.GONE);
        }
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

