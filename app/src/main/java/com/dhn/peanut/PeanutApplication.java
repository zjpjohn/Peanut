package com.dhn.peanut;

import android.app.Application;
import android.content.Context;

import com.dhn.peanut.util.PeanutInfo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by DHN on 2016/6/1.
 */
public class PeanutApplication extends Application {

    private static Application mContext;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        Fresco.initialize(this);
        refWatcher = LeakCanary.install(this);
    }

    public static Context getContext() {
        return mContext;
    }

    public static RefWatcher getRefWatcher(Context context) {
        PeanutApplication application = (PeanutApplication) context.getApplicationContext();
        return application.refWatcher;
    }

}
