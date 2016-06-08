package com.dhn.peanut.data.base;

import com.dhn.peanut.data.Comment;

import java.util.List;

/**
 * Created by DHN on 2016/6/2.
 */
public interface ShotDetailDataSource {
    interface LoadShotDetailCallBack {
        void onCommentLoaded(List<Comment> comments);
        void onFavorChecked(boolean isLiked);
        void onCommentNotAvailable();
    }

    void getComment(int shotId, LoadShotDetailCallBack loadShotDetailCallBack);
    void checkIfLike(int shotId, LoadShotDetailCallBack loadShotDetailCallBack);
    void changeLike(int shotId, boolean isLike);
}
