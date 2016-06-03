package com.dhn.peanut.data.base;

import com.dhn.peanut.data.Shot;

import java.util.List;

/**
 * Created by DHN on 2016/5/31.
 */
public interface ShotDataSource {
    interface LoadShotsCallback {
        void onShotsLoaded(List<Shot> shots);
        void onDataNotAvailable();
    }

    void getShots(String sort, int page, LoadShotsCallback callback);
    void getDebuts(String sort, int page, LoadShotsCallback callback);
    void getGifs(String sort, int page, LoadShotsCallback callback);

}
