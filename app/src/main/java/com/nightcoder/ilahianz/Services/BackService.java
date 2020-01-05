package com.nightcoder.ilahianz.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class BackService extends Service {

    HashMap<String, Object> mHashMap;

    BackService(HashMap<String, Object> hashMap){
        this.mHashMap = hashMap;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    public static void sentMessage(HashMap<String, Object> hashMap){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
