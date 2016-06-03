package com.dhn.peanut.data.base;

import com.dhn.peanut.data.Comment;

import java.util.List;

/**
 * Created by DHN on 2016/6/2.
 */
public interface CommentDataSource {
    interface LoadCommentCallBack {
        void onCommentLoaded(List<Comment> comments);
        void onCommentNotAvailable();
    }

    void getComment(int shotId, LoadCommentCallBack commentCallBack);
}
