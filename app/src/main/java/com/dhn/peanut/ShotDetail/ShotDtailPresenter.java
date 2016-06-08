package com.dhn.peanut.shotdetail;

import android.widget.Toast;

import com.dhn.peanut.PeanutApplication;
import com.dhn.peanut.data.Comment;
import com.dhn.peanut.data.base.ShotDetailDataSource;

import java.util.List;

/**
 * Created by DHN on 2016/6/1.
 */
public class ShotDtailPresenter implements ShotDetailContract.Presenter {

    private ShotDetailContract.View mView;
    private ShotDetailDataSource mDatasource;


    ShotDtailPresenter(ShotDetailContract.View view, ShotDetailDataSource dataSource) {
        mView = view;
        mView.setPresenter(this);
        mDatasource = dataSource;
    }

    @Override
    public void loadComment(int shotId) {

        mDatasource.getComment(shotId, new ShotDetailDataSource.LoadShotDetailCallBack() {
            @Override
            public void onCommentLoaded(List<Comment> comments) {
                mView.showComments(comments);
            }

            @Override
            public void onFavorChecked(boolean isLiked) {

            }

            @Override
            public void onCommentNotAvailable() {

            }
        });
    }

    @Override
    public void share() {
        //TODO
    }


    @Override
    public void checkLiked(int id) {
        mDatasource.checkIfLike(id, new ShotDetailDataSource.LoadShotDetailCallBack() {
            @Override
            public void onCommentLoaded(List<Comment> comments) {

            }

            @Override
            public void onFavorChecked(boolean isLiked) {
                if (isLiked) {
                    mView.showLike();
                } else {
                    mView.showUnLike();
                }
            }

            @Override
            public void onCommentNotAvailable() {

            }
        });
    }

    @Override
    public void changeLike(final int id) {
        //先判断是否喜欢
        mDatasource.checkIfLike(id, new ShotDetailDataSource.LoadShotDetailCallBack() {
            @Override
            public void onCommentLoaded(List<Comment> comments) {

            }

            @Override
            public void onFavorChecked(boolean isLiked) {
                if (isLiked) {
                    mDatasource.changeLike(id, false);
                    mView.showUnLike();
                    Toast.makeText(PeanutApplication.getContext(), "unlike", Toast.LENGTH_SHORT).show();
                } else {
                    mDatasource.changeLike(id, true);
                    mView.showLike();
                    Toast.makeText(PeanutApplication.getContext(), "like", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCommentNotAvailable() {

            }
        });
    }
}
