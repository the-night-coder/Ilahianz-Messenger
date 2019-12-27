package com.nightcoder.ilahianz;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightcoder.ilahianz.Supports.MemorySupports;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LAST_SEEN_DATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_STATUS;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
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
        hashMap.put(KEY_LAST_SEEN_DATE, "Active");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(MemorySupports.getUserInfo(getCurrentActivity(), KEY_ID));
        reference.updateChildren(hashMap);
        Log.d("STATUS", "ACTIVE");
    }

    public void setOffline() {
        HashMap<String, Object> hashMap = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        SimpleDateFormat time = new SimpleDateFormat("hh mm aa", Locale.US);
        hashMap.put(KEY_STATUS, time.format(new Date()));
        hashMap.put(KEY_LAST_SEEN_DATE, simpleDateFormat.format(new Date()));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(MemorySupports.getUserInfo(getCurrentActivity(), KEY_ID));
        reference.updateChildren(hashMap);
        Log.d("STATUS", "OFFLINE");
    }

}
