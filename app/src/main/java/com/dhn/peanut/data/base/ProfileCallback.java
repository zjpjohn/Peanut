package com.dhn.peanut.data.base;

import com.dhn.peanut.data.Shot;

import java.util.List;

/**
 * Created by DHN on 2016/6/15.
 */
public interface ProfileCallback {
    void onLoadProfileLoaded(List<Shot> shots, boolean hasNext);
    void onFollowChecked(boolean isFollowed);
    void onFollowed(boolean follow);
}
