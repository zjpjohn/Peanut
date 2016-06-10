package com.dhn.peanut.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by DHN on 2016/6/10.
 */
public class ShareUtil {

    /**
     * 分享文字
     *
     * @param activity
     * @param text
     */
    public static void shareText(Context activity, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        activity.startActivity(intent);
    }
}
