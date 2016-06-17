package com.dhn.peanut.data.base;

import com.dhn.peanut.data.Shot;

import java.util.List;

public interface LoadShotsCallback {
    void onShotsLoaded(List<Shot> shots);
    void onDataNotAvailable();
}
