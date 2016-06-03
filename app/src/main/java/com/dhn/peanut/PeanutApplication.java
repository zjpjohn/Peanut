package com.dhn.peanut;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by DHN on 2016/6/1.
 */
public class PeanutApplication extends Application {

    private static Application mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        Fresco.initialize(this);
    }

    public static Context getContext() {
        return mContext;
    }
}
