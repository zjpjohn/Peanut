package com.dhn.peanut.shotdetail;

import com.dhn.peanut.data.Comment;
import com.dhn.peanut.data.base.CommentDataSource;

import java.util.List;

/**
 * Created by DHN on 2016/6/1.
 */
public class ShotDtailPresenter implements ShotDetailContract.Presenter {

    private ShotDetailContract.View mView;
    private CommentDataSource mDatasource;


    ShotDtailPresenter(ShotDetailContract.View view, CommentDataSource dataSource) {
        mView = view;
        mView.setPresenter(this);
        mDatasource = dataSource;
    }

    @Override
    public void loadComment(int shotId) {

        mDatasource.getComment(shotId, new CommentDataSource.LoadCommentCallBack() {
            @Override
            public void onCommentLoaded(List<Comment> comments) {
                mView.showComments(comments);
            }

            @Override
            public void onCommentNotAvailable() {

            }
        });
    }
}
