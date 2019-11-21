package com.nightcoder.ilahianz.Databases;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    private static final String DATABASE_NAME = "chats";
    private String TABLE_NAME;
    private static final String ID = "_id";
    private DataChangeCallbacks callbacks;
    private final String TAG = "CHAT_DB";

    public static final int SENDER_INDEX = 1;
    public static final int RECEIVER_ID_INDEX = 2;
    public static final int TIMESTAMP_INDEX = 3;
    public static final int REFERENCE_INDEX = 4;
    public static final int LINK_INDEX = 4;
    public static final int IS_DELIVERED_INDEX = 5;
    public static final int IS_SEEN_INDEX = 6;
    public static final int IS_SENT_INDEX = 7;
    public static final int MESSAGE_INDEX = 8;

    public ChatDBHelper(@Nullable Context context, String tableName) {
        super(context, DATABASE_NAME, null, 1);
        this.TABLE_NAME = tableName;
        Activity activity = (Activity) context;
        callbacks = (DataChangeCallbacks) activity;
        Log.d(TAG, "CONSTRUCTOR CREATED");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS" + "'" + TABLE_NAME + "'" +
                " (" +
                REFERENCE + " TEXT PRIMARY KEY UNIQUE," +
                SENDER + " TEXT," +
                RECEIVER + " TEXT," +
                TIMESTAMP + " TEXT," +
                LINK + " TEXT," +
                IS_DELIVERED + " BOOLEAN," +
                IS_SEEN + " BOOLEAN," +
                IS_SENT + " BOOLEAN," +
                MESSAGE + " TEXT);"
        );
        Log.d(TAG, "TABLE CREATED " + TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "'" + TABLE_NAME + "'");
        onCreate(db);
        callbacks.onDataChange();
        Log.d(TAG, "TABLE UPDATED " + TABLE_NAME);
    }

    public void insertData(String sender_id, String receiver_id,
                           String timestamp, String reference, String link,
                           boolean isDelivered, boolean isSeen, boolean isSent, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT OR REPLACE INTO " + TABLE_NAME
                + "(sender,receiver,timestamp,reference,link,isDelivered,isSeen,isSent,message) " +
                "VALUES(" +
                "'" + sender_id + "'" + "," +
                "'" + receiver_id + "'" + "," +
                "'" + timestamp + "'" + "," +
                "'" + reference + "'" + "," +
                "'" + link + "'" + "," +
                getInt(isDelivered) + "," +
                getInt(isSeen) + "," +
                getInt(isSent) + "," +
                "'" + message + "'" +
                ");");
        callbacks.onDataChange();
        db.close();
    }

    public SQLiteDatabase getInstance() {
        return this.getWritableDatabase();
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    private int getInt(boolean value) {
        return value ? 1 : 0;
    }
}
