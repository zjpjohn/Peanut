package com.dhn.peanut.util;

/**
 * Created by DHN on 2016/6/4.
 */
public class Log {
    public static final boolean DBG = false;

    public static final String TAG = "PEANUT";

    public static void i(String s) {
        android.util.Log.i(TAG, s);
    }

    public static void e(String s) {
        android.util.Log.e(TAG, s);
    }
}
