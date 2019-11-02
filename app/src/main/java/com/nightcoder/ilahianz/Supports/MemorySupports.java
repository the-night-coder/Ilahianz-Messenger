package com.nightcoder.ilahianz.Supports;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.USER_INFO_SP;

public class MemorySupports {

    public static void setUserInfo(Context mContext, String key, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_INFO_SP, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getUserInfo(Context mContext, String key) {
        SharedPreferences preferences = mContext.getSharedPreferences(USER_INFO_SP, MODE_PRIVATE);
        return preferences.getString(key, "none");
    }

}
