package com.dhn.peanut.data.base;

/**
 * Created by DHN on 2016/6/15.
 */
public interface ProfileDataSource {

    void getShot(int userId, boolean first, final ProfileCallback callback);
    void checkFollow(int userId, ProfileCallback callback);
    void changeFollowState(int userId, boolean isFollow, ProfileCallback callback);
}
