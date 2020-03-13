package com.nightcoder.ilahianz.MainActivityFragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.nightcoder.ilahianz.Adapters.NotificationAdapter;
import com.nightcoder.ilahianz.Databases.NotificationDBHelper;
import com.nightcoder.ilahianz.Models.Notification;
import com.nightcoder.ilahianz.R;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private Context mContext;
    //private String id;
    private NotificationDBHelper notificationDBHelper;
    private Handler handler = new Handler();
    private NotificationAdapter notificationAdapter;
    public NotificationFragment(Context mContext) {
        this.mContext = mContext;
        notificationDBHelper = new NotificationDBHelper(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        init(view);
        //id = MemorySupports.getUserInfo(mContext, KEY_ID);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                notificationAdapter.notify();
                Log.d("ss", "Scheduler");
            }
        }, 10, 30, TimeUnit.SECONDS);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        setNotifications();
    }

    private void setNotifications() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Notification> notifications = new ArrayList<>();
                    Cursor cursor = notificationDBHelper.getData();
                    notifications.clear();
                    while (cursor.moveToNext()) {
                        Notification notification = new Notification();

                        notification.setId(cursor.getString(NotificationDBHelper.INDEX_ID));
                        notification.setKey(cursor.getString(NotificationDBHelper.INDEX_KEY));
                        notification.setMessage(cursor.getString(NotificationDBHelper.INDEX_MESSAGE));
                        notification.setRef(cursor.getString(NotificationDBHelper.INDEX_REF));
                        notification.setType(cursor.getInt(NotificationDBHelper.INDEX_TYPE));
                        notification.setTimestamp(cursor.getDouble(NotificationDBHelper.INDEX_TIME));
                        notification.setSeen(cursor.getInt(NotificationDBHelper.INDEX_SEEN) == 1);
                        notification.setUsername(cursor.getString(NotificationDBHelper.INDEX_USERNAME));

                        if (!notification.isSeen())
                            notificationDBHelper.setSeen(notification.getKey());

                        notifications.add(notification);
                    }

                    notificationAdapter = new NotificationAdapter(mContext, notifications);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(notificationAdapter);
                        }
                    });

                } catch (SQLiteException e) {
                    Log.d("DATABASE", "Not found");
                }
            }
        });
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
    }
}
