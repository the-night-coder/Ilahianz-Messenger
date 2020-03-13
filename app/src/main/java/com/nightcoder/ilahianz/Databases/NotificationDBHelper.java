package com.nightcoder.ilahianz.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.nightcoder.ilahianz.Models.Notification;

public class NotificationDBHelper extends SQLiteOpenHelper {

    public static final int INDEX_KEY = 0;
    public static final int INDEX_MESSAGE = 1;
    public static final int INDEX_ID = 2;
    public static final int INDEX_TYPE = 3;
    public static final int INDEX_REF = 4;
    public static final int INDEX_TIME = 5;
    public static final int INDEX_SEEN = 6;
    public static final int INDEX_USERNAME = 7;

    private Context mContext;

    public NotificationDBHelper(@Nullable Context context) {
        super(context, "Notifications", null, 1);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS"
                + " Notification(keyid TEXT PRIMARY KEY UNIQUE," +
                "message TEXT, id TEXT, type INT, ref TEXT, timestamp DOUBLE, seen BOOLEAN, username TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Notification");
        onCreate(db);
    }

    public void deleteData(String keyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = db.delete("Notification", "keyid='" + keyId + "'", null) > 0;
        if (result) {
            Log.d("ROW", "DELETED");
        } else {
            Log.d("ROW", "NOT DELETED");
        }

    }

    public SQLiteDatabase getInstance() {
        return this.getWritableDatabase();
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM Notification", null);
    }

    public void insertData(Notification notification) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        contentValues.put("keyid", notification.getKey());
        contentValues.put("message", notification.getMessage());
        contentValues.put("id", notification.getId());
        contentValues.put("type", notification.getType());
        contentValues.put("ref", notification.getRef());
        contentValues.put("timestamp", notification.getTimestamp());
        contentValues.put("seen", getInt(notification.isSeen()));
        contentValues.put("username", notification.getUsername());
        db.insertWithOnConflict("Notification", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void setSeen(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE Notification SET seen=1 WHERE keyid='" + id + "'");
    }

    private int getInt(boolean value) {
        return value ? 1 : 0;
    }
}
