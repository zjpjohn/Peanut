package com.dhn.peanut.util;

/**
 * Created by DHN on 2016/6/4.
 */
public class PeanutInfo {
    public static final String CALLBACK_URL = "http://www.google.com";
    public static final String LOGIN_URL_BASE = "https://dribbble.com/oauth/authorize";
    public static final String MY_INFO_URL = "https://api.dribbble.com/v1/user";
    public static final String TOKEN_URL = "https://dribbble.com/oauth/token";

    public static final String CLIENT_ID = "19188df67f0204b0057b74882021900319ecd4c79cb2fcbb78c0b72b4642fdf9";
    public static final String CLIENT_SECRET = "a09ea1f8d8f6d8e99162719bbc65fc75052b9e43a339c59529ac0f97e447de11";


    public static final String TOKEN_FIELD = "token";
    public static final String HEAD_AUTH_FILED = "Authorization";
    public static String mState;

    public static final String LOGIN_URL = LOGIN_URL_BASE + "?" +
                    "client_id=" + CLIENT_ID +
                    "&redirect_uri=" + CALLBACK_URL +
                    "&scope=" + "public write comment upload" +
                    "&state=" + mState;
}
