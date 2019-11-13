package com.nightcoder.ilahianz.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ChatDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chats.db";
    private static final String TABLE_NAME = "chats_table";
    private static final String ID = "_id";
    private static final String UID = "u_id";
    private static final String SENDER_ID = "sender_id";
    private static final String RECEIVER_ID = "receiver_id";
    private static final String TIMESTAMP = "timestamp";
    private static final String MESSAGE = "message";

    public ChatDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UID + " TEXT," +
                SENDER_ID + " TEXT," +
                RECEIVER_ID + " TEXT," +
                TIMESTAMP + " TEXT," +
                MESSAGE + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String u_id,
                              String sender_id, String receiver_id,
                              String timestamp, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UID, u_id);
        contentValues.put(SENDER_ID, sender_id);
        contentValues.put(RECEIVER_ID, receiver_id);
        contentValues.put(TIMESTAMP, timestamp);
        contentValues.put(MESSAGE, message);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
