package com.dhn.peanut.util;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.dhn.peanut.PeanutApplication;

/**
 * Created by DHN on 2016/6/8.
 */
public class AuthoUtil {
    public  static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PeanutApplication.getContext());
    public static String getToken() {
        String token = preferences.getString(PeanutInfo.PREFERENCE_TOKEN, null);
        return token;
    }

    public static String getLikesUrl() {
        String token = preferences.getString(PeanutInfo.PREFERENCE_USER_LIKE_URL, null);
        return token;
    }
}
