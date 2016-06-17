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

    public static void putToken(String accessToken) {
        preferences.edit().putString(PeanutInfo.PREFERENCE_TOKEN, accessToken).commit();
    }

    public static void putId(int id) {
        preferences.edit().putInt(PeanutInfo.PREFERENCE_USER_ID, id).commit();
    }

    public static int getId() {
        return preferences.getInt(PeanutInfo.PREFERENCE_USER_ID, 0);
    }

    public static String getLikesUrl() {
        String token = preferences.getString(PeanutInfo.PREFERENCE_USER_LIKE_URL, "");
        return token;
    }

    public static String getUserName() {
        return preferences.getString(PeanutInfo.PREFERENCE_USER_NAME, "");
    }
    public static void putUserName(String username) {
        preferences.edit().putString(PeanutInfo.PREFERENCE_USER_NAME, username).commit();
    }

    public static String getUserAvatar() {
        return preferences.getString(PeanutInfo.PREFERENCE_USER_AVATAR, "");
    }
    public static void putUserAvatar(String userAvatar) {
        preferences.edit().putString(PeanutInfo.PREFERENCE_USER_AVATAR, userAvatar).commit();
    }

    public static void putLikeUrl(String likeUrl) {
        preferences.edit().putString(PeanutInfo.PREFERENCE_USER_LIKE_URL, likeUrl).commit();
    }

    public static int getLikesCount() {
        return preferences.getInt(PeanutInfo.PREFERENCE_USER_LIKES, 0);
    }
    public static void putLikesCount(int likesCount) {
        preferences.edit().putInt(PeanutInfo.PREFERENCE_USER_LIKES, likesCount).commit();
    }

    public static boolean isLogined() {
        return getToken() != null;
    }
}
