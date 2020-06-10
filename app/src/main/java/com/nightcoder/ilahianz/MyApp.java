package com.nightcoder.ilahianz;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.nightcoder.ilahianz.Databases.NotificationDBHelper;
import com.nightcoder.ilahianz.Models.Chats;
import com.nightcoder.ilahianz.Models.Notice;
import com.nightcoder.ilahianz.Models.Notification;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.nightcoder.ilahianz.Supports.Network;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import java.util.HashMap;
import java.util.Objects;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CHATS;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LAST_SEEN_DATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_STATUS;
import static com.nightcoder.ilahianz.Models.Notice.TYPE_AUDIO;
import static com.nightcoder.ilahianz.Models.Notice.TYPE_DOC;
import static com.nightcoder.ilahianz.Models.Notice.TYPE_IMAGE;

public class MyApp extends Application {
    NotificationDBHelper notificationDBHelper;
    String id;
    Handler handler = new Handler(Looper.getMainLooper());

    private Activity mCurrentActivity = null;

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationDBHelper = new NotificationDBHelper(this);
        id = MemorySupports.getUserInfo(this, KEY_ID);
        syncNotifications();
    }

    public void setOnline() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(KEY_STATUS, "Active");
        hashMap.put(KEY_LAST_SEEN_DATE, ServerValue.TIMESTAMP);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(MemorySupports.getUserInfo(getCurrentActivity(), KEY_ID));
        reference.child("status").onDisconnect().setValue("Offline");
        reference.child("lastSeen").onDisconnect().setValue(ServerValue.TIMESTAMP);
        reference.updateChildren(hashMap);
        Log.d("STATUS", "ACTIVE");
    }

    public void syncNotifications() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                syncNotification(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void syncNotification(final DataSnapshot dataSnapshot) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notification notification = snapshot.getValue(Notification.class);
                    assert notification != null;
                    notificationDBHelper.insertData(notification);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(id);
                    reference.child(notification.getKey()).setValue(null);
                }
            }
        });
//        ViewSupports.materialSnackBar(getCurrentActivity(), "You may have new Notification",
//                1000, R.drawable.heart);
    }

    public void composeTextNotice(HashMap<String, Object> hashMap, String key) {
        if (Network.Connected(this)) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notice");
            final Dialog dialog = ViewSupports.materialSnackBarDialog(getCurrentActivity(), R.layout.upload_task_progress);
            Button cancel = dialog.findViewById(R.id.option_close);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    if (getCurrentActivity() instanceof ComposeNoticeActivity) {
                        getCurrentActivity().onBackPressed();
                    }
                }
            });
            dialog.setCancelable(false);
            dialog.show();

            reference.child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("Notice", "Sent");
                    dialog.cancel();
                    if (task.isSuccessful()) {
                        if (getCurrentActivity() instanceof ComposeNoticeActivity) {
                            getCurrentActivity().onBackPressed();
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final Dialog dialog = ViewSupports.materialSnackBar(getCurrentActivity(),
                                        "Notice Uploaded.", R.drawable.ic_check_circle_green_24dp);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();
                                    }
                                }, 3000);
                            }
                        }, 1000);
                    } else {
                        final Dialog dialog = ViewSupports.materialSnackBar(getCurrentActivity(),
                                "Notice Failed to Upload", R.drawable.ic_info_black_24dp);
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                            }
                        });
                    }
                }
            });
        }
    }

    public void uploadMedia(Uri uri) {
        if (Network.Connected(this)) {
            final Dialog dialog = ViewSupports.materialSnackBarDialog(getCurrentActivity(), R.layout.upload_task_progress);
            Button cancel = dialog.findViewById(R.id.option_close);
            dialog.setCancelable(false);
            cancel.setVisibility(View.GONE);
            dialog.setCancelable(false);
            dialog.show();
            final String id = MemorySupports.getUserInfo(getCurrentActivity(), Notice.KEY_ID);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("Profile");
            final StorageReference fileReference = storageReference.child(id).child(id + ".jpg");
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(id);
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
                        reference.child("thumbnailURL").setValue(mUri);
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

    public void uploadNotice(Uri uri, final int type, final String subject, final String target, final String content) {
        if (Network.Connected(this)) {
            final Dialog dialog = ViewSupports.materialSnackBarDialog(getCurrentActivity(), R.layout.upload_task_progress);
            Button cancel = dialog.findViewById(R.id.option_close);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    if (getCurrentActivity() instanceof ComposeNoticeActivity) {
                        getCurrentActivity().onBackPressed();
                    }
                }
            });
            dialog.setCancelable(false);
            dialog.show();

            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notice");
            final String key = reference.push().getKey();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("Notice");
            final StorageReference fileReference;
            if (type == TYPE_IMAGE) {
                fileReference = storageReference.child(id).child(key + ".jpg");
            } else if (type == TYPE_AUDIO){
                fileReference = storageReference.child(id).child(key + ".3gp");
            } else if (type == TYPE_DOC){
                fileReference = storageReference.child(id).child(key + ".pdf");
            } else {
                fileReference = storageReference.child(id).child(key + ".jpg");
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
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put(Notice.KEY_COMPOSER_ID, id);
                        hashMap.put(Notice.KEY_CONTENT_PATH, mUri);
                        hashMap.put(Notice.KEY_CONTENT_TYPE, type);
                        hashMap.put(Notice.KEY_SUBJECT, subject);
                        hashMap.put(Notice.KEY_TEXT, content);
                        hashMap.put(Notice.KEY_TARGET, target);
                        hashMap.put(Notice.KEY_TIMESTAMP, ServerValue.TIMESTAMP);
                        hashMap.put(Notice.KEY_ID, key);
                        assert key != null;
                        reference.child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    final Dialog dialog = ViewSupports.materialSnackBar(getCurrentActivity(),
                                            "Notice Uploaded.", R.drawable.ic_check_circle_green_24dp);
                                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            if (getCurrentActivity() instanceof ComposeNoticeActivity) {
                                                getCurrentActivity().onBackPressed();
                                            }
                                        }
                                    });
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.cancel();
                                            if (getCurrentActivity() instanceof ComposeNoticeActivity) {
                                                getCurrentActivity().onBackPressed();
                                            }
                                        }
                                    }, 3000);
                                } else {
                                    final Dialog dialog = ViewSupports.materialSnackBar(getCurrentActivity(),
                                            "Notice failed to Upload.", R.drawable.ic_info_black_24dp);
                                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            if (getCurrentActivity() instanceof ComposeNoticeActivity) {
                                                getCurrentActivity().onBackPressed();
                                            }
                                        }
                                    });
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

}
