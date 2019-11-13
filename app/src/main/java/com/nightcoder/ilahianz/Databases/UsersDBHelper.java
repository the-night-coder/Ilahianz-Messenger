package com.nightcoder.ilahianz.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ABOUT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ACADEMIC_YEAR_FROM;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ACADEMIC_YEAR_TO;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIO;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTHDAY_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_DAY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_MONTH;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_YEAR;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BLOOD_DONATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BLOOD_TYPE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CATEGORY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CITY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CLASS_NAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_DEPARTMENT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_DISTRICT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_EMAIL;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_EMAIL_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_GENDER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID_NUMBER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_IMAGE_URL;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_JOIN_DATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LAST_SEEN_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LATITUDE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LOCATION_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LONGITUDE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_NICKNAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PHONE_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PH_NUMBER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PROFILE_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_SEARCH;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_THUMBNAIL;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_USERNAME;

public class UsersDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final String TABLE_NAME = "chats_table";
    private static final String ID = "_id";

    public UsersDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_ID + " TEXT," +
                KEY_USERNAME + " TEXT," +
                KEY_PH_NUMBER + " TEXT," +
                KEY_GENDER + " TEXT," +
                KEY_CLASS_NAME + " TEXT," +
                KEY_EMAIL + " TEXT," +
                KEY_BIRTH_DAY + " TEXT," +
                KEY_BIRTH_MONTH + " TEXT," +
                KEY_BIRTH_YEAR + " TEXT," +
                KEY_NICKNAME + " TEXT," +
                KEY_CATEGORY + " TEXT," +
                KEY_ABOUT + " TEXT," +
                KEY_LONGITUDE + " TEXT," +
                KEY_LATITUDE + " TEXT," +
                KEY_LAST_SEEN_PRIVACY + " TEXT," +
                KEY_BIRTHDAY_PRIVACY + " TEXT," +
                KEY_EMAIL_PRIVACY + " TEXT," +
                KEY_PHONE_PRIVACY + " TEXT," +
                KEY_LOCATION_PRIVACY + " TEXT," +
                KEY_PROFILE_PRIVACY + " TEXT," +
                KEY_CITY + " TEXT," +
                KEY_DISTRICT + " TEXT," +
                KEY_DEPARTMENT + " TEXT," +
                KEY_BIO + " TEXT," +
                KEY_SEARCH + " TEXT," +
                KEY_THUMBNAIL + " TEXT," +
                KEY_IMAGE_URL + " TEXT," +
                KEY_BLOOD_DONATE + " TEXT," +
                KEY_BLOOD_TYPE + " TEXT," +
                KEY_ACADEMIC_YEAR_FROM + " TEXT," +
                KEY_ACADEMIC_YEAR_TO + " TEXT," +
                KEY_JOIN_DATE + " TEXT," +
                KEY_ID_NUMBER + " TEXT);"
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
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
