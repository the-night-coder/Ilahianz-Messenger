package com.nightcoder.ilahianz;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.Databases.NotificationDBHelper;
import com.nightcoder.ilahianz.Models.Notification;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import java.util.HashMap;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LAST_SEEN_DATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_STATUS;

public class MyApp extends Application {
    NotificationDBHelper notificationDBHelper;
    String id;
    @Override
    public void onCreate() {
        super.onCreate();
        notificationDBHelper = new NotificationDBHelper(this);
        id = MemorySupports.getUserInfo(this, KEY_ID);
        syncNotifications();

    }

    private Activity mCurrentActivity = null;

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
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


}
