package com.nightcoder.ilahianz.Databases;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.nightcoder.ilahianz.Listeners.DatabaseListener.DataChangeCallbacks;

import static com.nightcoder.ilahianz.Literals.StringConstants.IS_DELIVERED;
import static com.nightcoder.ilahianz.Literals.StringConstants.IS_SEEN;
import static com.nightcoder.ilahianz.Literals.StringConstants.IS_SENT;
import static com.nightcoder.ilahianz.Literals.StringConstants.LINK;
import static com.nightcoder.ilahianz.Literals.StringConstants.MESSAGE;
import static com.nightcoder.ilahianz.Literals.StringConstants.RECEIVER;
import static com.nightcoder.ilahianz.Literals.StringConstants.REFERENCE;
import static com.nightcoder.ilahianz.Literals.StringConstants.SENDER;
import static com.nightcoder.ilahianz.Literals.StringConstants.TIMESTAMP;

public class ChatDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chats.db";
    private String TABLE_NAME;
    private static final String ID = "_id";
    private DataChangeCallbacks callbacks;

    public static final int SENDER_INDEX = 1;
    public static final int RECEIVER_ID_INDEX = 2;
    public static final int TIMESTAMP_INDEX = 3;
    public static final int REFERENCE_INDEX = 4;
    public static final int LINK_INDEX = 5;
    public static final int IS_DELIVERED_INDEX = 6;
    public static final int IS_SEEN_INDEX = 7;
    public static final int IS_SENT_INDEX = 8;
    public static final int MESSAGE_INDEX = 9;

    public ChatDBHelper(@Nullable Context context, String tableName) {
        super(context, DATABASE_NAME, null, 1);
        this.TABLE_NAME = tableName;
        Activity activity = (Activity) context;
        callbacks = (DataChangeCallbacks) activity;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SENDER + " TEXT," +
                RECEIVER + " TEXT," +
                TIMESTAMP + " TEXT," +
                REFERENCE + " TEXT," +
                LINK + " TEXT," +
                IS_DELIVERED + " BOOLEAN," +
                IS_SEEN + " BOOLEAN," +
                IS_SENT + " BOOLEAN," +
                MESSAGE + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        callbacks.onDataChange();
    }

    public boolean insertData(String sender_id, String receiver_id,
                              String timestamp, String reference, String link,
                              boolean isDelivered, boolean isSeen, boolean isSent, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SENDER, sender_id);
        contentValues.put(RECEIVER, receiver_id);
        contentValues.put(TIMESTAMP, timestamp);
        contentValues.put(REFERENCE, reference);
        contentValues.put(LINK, link);
        contentValues.put(IS_DELIVERED, isDelivered);
        contentValues.put(IS_SEEN, isSeen);
        contentValues.put(IS_SENT, isSent);
        contentValues.put(MESSAGE, message);
        long result = db.insert(TABLE_NAME, null, contentValues);
        callbacks.onDataChange();
        return result != -1;
    }

    public SQLiteDatabase getInstance() {
        return this.getWritableDatabase();
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
