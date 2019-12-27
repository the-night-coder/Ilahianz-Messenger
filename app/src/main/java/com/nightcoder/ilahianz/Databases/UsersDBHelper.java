package com.nightcoder.ilahianz.Databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.nightcoder.ilahianz.Databases.Model.UserModel;
import com.nightcoder.ilahianz.Models.UserData;

import java.util.ArrayList;

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

    private static final String DATABASE_NAME = "users";
    private static final String TABLE_NAME = "user_table";

    //index
    private static final int INDEX_ID = 0;
    private static final int INDEX_USERNAME = 1;
    private static final int INDEX_PH_NUMBER = 2;
    private static final int INDEX_GENDER = 3;
    private static final int INDEX_CLASS_NAME = 4;
    private static final int INDEX_EMAIL = 5;
    private static final int INDEX_BIRTH_DAY = 6;
    private static final int INDEX_BIRTH_MONTH = 7;
    private static final int INDEX_BIRTH_YEAR = 8;
    private static final int INDEX_NICKNAME = 9;
    private static final int INDEX_CATEGORY = 10;
    private static final int INDEX_ABOUT = 11;
    private static final int INDEX_LONGITUDE = 12;
    private static final int INDEX_LATITUDE = 13;
    private static final int INDEX_LAST_SEEN_PRIVACY = 14;
    private static final int INDEX_BIRTHDAY_PRIVACY = 15;
    private static final int INDEX_EMAIL_PRIVACY = 16;
    private static final int INDEX_PHONE_PRIVACY = 17;
    private static final int INDEX_LOCATION_PRIVACY = 18;
    private static final int INDEX_PROFILE_PRIVACY = 19;
    private static final int INDEX_CITY = 20;
    private static final int INDEX_DISTRICT = 21;
    private static final int INDEX_DEPARTMENT = 22;
    private static final int INDEX_BIO = 23;
    private static final int INDEX_SEARCH = 24;
    private static final int INDEX_THUMBNAIL = 25;
    private static final int INDEX_IMAGE_URL = 26;
    private static final int INDEX_BLOOD_DONATE = 27;
    private static final int INDEX_BLOOD_TYPE = 28;
    private static final int INDEX_ACADEMIC_YEAR_FROM = 29;
    private static final int INDEX_ACADEMIC_YEAR_TO = 30;
    private static final int INDEX_JOIN_DATE = 31;
    private static final int INDEX_ID_NUMBER = 32;

    public UsersDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" +
                KEY_ID + " TEXT PRIMARY KEY UNIQUE," +
                KEY_USERNAME + " TEXT," + KEY_PH_NUMBER + " TEXT," +
                KEY_GENDER + " TEXT," + KEY_CLASS_NAME + " TEXT," +
                KEY_EMAIL + " TEXT," + KEY_BIRTH_DAY + " TEXT," +
                KEY_BIRTH_MONTH + " TEXT," + KEY_BIRTH_YEAR + " TEXT," + KEY_NICKNAME + " TEXT," +
                KEY_CATEGORY + " TEXT," + KEY_ABOUT + " TEXT," +
                KEY_LONGITUDE + " TEXT," + KEY_LATITUDE + " TEXT," +
                KEY_LAST_SEEN_PRIVACY + " TEXT," + KEY_BIRTHDAY_PRIVACY + " TEXT," +
                KEY_EMAIL_PRIVACY + " TEXT," + KEY_PHONE_PRIVACY + " TEXT," +
                KEY_LOCATION_PRIVACY + " TEXT," + KEY_PROFILE_PRIVACY + " TEXT," +
                KEY_CITY + " TEXT," + KEY_DISTRICT + " TEXT," +
                KEY_DEPARTMENT + " TEXT," + KEY_BIO + " TEXT," +
                KEY_SEARCH + " TEXT," + KEY_THUMBNAIL + " TEXT," +
                KEY_IMAGE_URL + " TEXT," + KEY_BLOOD_DONATE + " TEXT," +
                KEY_BLOOD_TYPE + " TEXT," + KEY_ACADEMIC_YEAR_FROM + " TEXT," +
                KEY_ACADEMIC_YEAR_TO + " TEXT," + KEY_JOIN_DATE + " TEXT," +
                KEY_ID_NUMBER + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addUser(UserData userData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_ID, userData.getId());
        contentValues.put(KEY_USERNAME, userData.getUsername());
        contentValues.put(KEY_PH_NUMBER, userData.getPhoneNumber());
        contentValues.put(KEY_GENDER, userData.getGender());
        contentValues.put(KEY_CLASS_NAME, userData.getClassName());
        contentValues.put(KEY_EMAIL, userData.getEmail());
        contentValues.put(KEY_BIRTH_DAY, userData.getBirthday());
        contentValues.put(KEY_BIRTH_MONTH, userData.getBirthMonth());
        contentValues.put(KEY_BIRTH_YEAR, userData.getBirthYear());
        contentValues.put(KEY_NICKNAME, userData.getNickname());
        contentValues.put(KEY_CATEGORY, userData.getCategory());
        contentValues.put(KEY_ABOUT, userData.getDescription());
        contentValues.put(KEY_LONGITUDE, userData.getLongitude());
        contentValues.put(KEY_LATITUDE, userData.getLatitude());
        contentValues.put(KEY_LAST_SEEN_PRIVACY, userData.getLastSeenPrivacy());
        contentValues.put(KEY_BIRTHDAY_PRIVACY, userData.getBirthdayPrivacy());
        contentValues.put(KEY_EMAIL_PRIVACY, userData.getEmailPrivacy());
        contentValues.put(KEY_PHONE_PRIVACY, userData.getPhonePrivacy());
        contentValues.put(KEY_LOCATION_PRIVACY, userData.getLocationPrivacy());
        contentValues.put(KEY_PROFILE_PRIVACY, userData.getProfilePrivacy());
        contentValues.put(KEY_CITY, userData.getCity());
        contentValues.put(KEY_DISTRICT, userData.getDistrict());
        contentValues.put(KEY_DEPARTMENT, userData.getDepartment());
        contentValues.put(KEY_BIO, userData.getBio());
        contentValues.put(KEY_SEARCH, userData.getSearch());
        contentValues.put(KEY_THUMBNAIL, userData.getThumbnailURL());
        contentValues.put(KEY_IMAGE_URL, userData.getImageURL());
        contentValues.put(KEY_BLOOD_DONATE, userData.getBloodDonate());
        contentValues.put(KEY_BLOOD_TYPE, userData.getBloodType());
        contentValues.put(KEY_ACADEMIC_YEAR_FROM, userData.getAcademicYearFrom());
        contentValues.put(KEY_ACADEMIC_YEAR_TO, userData.getAcademicYearTo());
        contentValues.put(KEY_JOIN_DATE, userData.getJoinDate());
        contentValues.put(KEY_ID_NUMBER, userData.getIdNumber());

        long result = db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public UserModel getUser(String id) {
        UserModel user;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            @SuppressLint("Recycle")
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + "WHERE id='" + id + "'", null);
            user = new UserModel();
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    user = new UserModel(
                            cursor.getString(INDEX_USERNAME), cursor.getString(INDEX_ID),
                            cursor.getString(INDEX_IMAGE_URL), cursor.getString(INDEX_CLASS_NAME),
                            cursor.getString(INDEX_GENDER), cursor.getString(INDEX_NICKNAME),
                            cursor.getString(INDEX_CATEGORY), cursor.getString(INDEX_SEARCH),
                            cursor.getString(INDEX_EMAIL), cursor.getString(INDEX_BIRTH_DAY),
                            cursor.getString(INDEX_BIRTH_YEAR), cursor.getString(INDEX_BIRTH_MONTH),
                            cursor.getString(INDEX_ABOUT), cursor.getString(INDEX_LAST_SEEN_PRIVACY),
                            cursor.getString(INDEX_LOCATION_PRIVACY), cursor.getString(INDEX_PHONE_PRIVACY),
                            cursor.getString(INDEX_EMAIL_PRIVACY), cursor.getString(INDEX_PROFILE_PRIVACY),
                            cursor.getString(INDEX_PH_NUMBER), cursor.getString(INDEX_BIRTHDAY_PRIVACY),
                            cursor.getString(INDEX_LATITUDE), cursor.getString(INDEX_LONGITUDE),
                            cursor.getString(INDEX_THUMBNAIL), cursor.getString(INDEX_CITY),
                            cursor.getString(INDEX_DISTRICT), cursor.getString(INDEX_BIO),
                            cursor.getString(INDEX_DEPARTMENT), cursor.getString(INDEX_ID_NUMBER),
                            cursor.getString(INDEX_BLOOD_DONATE), cursor.getString(INDEX_ACADEMIC_YEAR_FROM),
                            cursor.getString(INDEX_ACADEMIC_YEAR_TO), cursor.getString(INDEX_JOIN_DATE),
                            cursor.getString(INDEX_BLOOD_TYPE)
                    );
                }
            }
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    public ArrayList<UserModel> getUsers() {
        ArrayList<UserModel> mUsers = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase()) {

            @SuppressLint("Recycle")
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            mUsers.clear();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    UserModel user = new UserModel(
                            cursor.getString(INDEX_USERNAME), cursor.getString(INDEX_ID),
                            cursor.getString(INDEX_IMAGE_URL), cursor.getString(INDEX_CLASS_NAME),
                            cursor.getString(INDEX_GENDER), cursor.getString(INDEX_NICKNAME),
                            cursor.getString(INDEX_CATEGORY), cursor.getString(INDEX_SEARCH),
                            cursor.getString(INDEX_EMAIL), cursor.getString(INDEX_BIRTH_DAY),
                            cursor.getString(INDEX_BIRTH_YEAR), cursor.getString(INDEX_BIRTH_MONTH),
                            cursor.getString(INDEX_ABOUT), cursor.getString(INDEX_LAST_SEEN_PRIVACY),
                            cursor.getString(INDEX_LOCATION_PRIVACY), cursor.getString(INDEX_PHONE_PRIVACY),
                            cursor.getString(INDEX_EMAIL_PRIVACY), cursor.getString(INDEX_PROFILE_PRIVACY),
                            cursor.getString(INDEX_PH_NUMBER), cursor.getString(INDEX_BIRTHDAY_PRIVACY),
                            cursor.getString(INDEX_LATITUDE), cursor.getString(INDEX_LONGITUDE),
                            cursor.getString(INDEX_THUMBNAIL), cursor.getString(INDEX_CITY),
                            cursor.getString(INDEX_DISTRICT), cursor.getString(INDEX_BIO),
                            cursor.getString(INDEX_DEPARTMENT), cursor.getString(INDEX_ID_NUMBER),
                            cursor.getString(INDEX_BLOOD_DONATE), cursor.getString(INDEX_ACADEMIC_YEAR_FROM),
                            cursor.getString(INDEX_ACADEMIC_YEAR_TO), cursor.getString(INDEX_JOIN_DATE),
                            cursor.getString(INDEX_BLOOD_TYPE)
                    );
                    mUsers.add(user);
                }
            }
        }
        return mUsers;
    }

}
