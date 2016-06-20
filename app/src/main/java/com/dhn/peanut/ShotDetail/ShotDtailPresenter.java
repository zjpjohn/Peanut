package com.dhn.peanut.shotdetail;

import android.widget.Toast;

import com.dhn.peanut.PeanutApplication;
import com.dhn.peanut.data.Comment;
import com.dhn.peanut.data.base.ShotDetailDataSource;
import com.dhn.peanut.util.AuthoUtil;

import java.util.List;

/**
 * Created by DHN on 2016/6/1.
 */
public class ShotDtailPresenter implements ShotDetailContract.Presenter {

    private ShotDetailContract.View mView;
    private ShotDetailDataSource mDatasource;
    private boolean mIsLiked;


    ShotDtailPresenter(ShotDetailContract.View view, ShotDetailDataSource dataSource) {
        mView = view;
        mView.setPresenter(this);
        mDatasource = dataSource;
    }

    @Override
    public void loadComment(int shotId) {
        mView.showProgress();

        mDatasource.getComment(shotId, new ShotDetailDataSource.LoadShotDetailCallBack() {
            @Override
            public void onCommentLoaded(List<Comment> comments) {
                mView.showComments(comments);
                mView.hideProgress();
            }

            @Override
            public void onFavorChecked(boolean isLiked) {

            }

            @Override
            public void onCommentNotAvailable() {
                mView.showToast("出错啦，请检查网络后重试");
            }
        });
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
                    mIsLiked = true;
                } else {
                    mView.showUnLike();
                    mIsLiked = false;
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
        if (mIsLiked) {
            mDatasource.changeLike(id, false);
            mView.showUnLike();
            Toast.makeText(PeanutApplication.getContext(), "添加到喜欢列表", Toast.LENGTH_SHORT).show();
        } else {
            mDatasource.changeLike(id, true);
            mView.showLike();
            Toast.makeText(PeanutApplication.getContext(), "从喜欢列表去除", Toast.LENGTH_SHORT).show();
        }
        mIsLiked  = !mIsLiked;

    }
}
