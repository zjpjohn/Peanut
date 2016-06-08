package com.dhn.peanut.like;

import android.text.TextUtils;

import com.dhn.peanut.data.LikedShot;
import com.dhn.peanut.data.Shot;
import com.dhn.peanut.data.base.LikeDataSource;

import java.util.List;

/**
 * Created by DHN on 2016/6/8.
 */
public class LikePresenter implements LikeContract.Presenter {
    private LikeContract.View mView;
    private LikeDataSource mData;

    public LikePresenter(LikeContract.View view, LikeDataSource data) {
        mView = view;
        mData = data;
        mView.setPresenter(this);
    }


    @Override
    public void loadLikes() {
        mView.showLoading();

        mData.getLikes(new LikeDataSource.LoadLikeCallback() {
            @Override
            public void onShotsLoaded(List<LikedShot> shots) {
                mView.hideLoading();
                mView.showLoadingIndicator(false);

                if (shots == null && shots.size() == 0) {
                    mView.showNoContent();
                } else {
                    mView.showLikes(shots);
                }
            }

            @Override
            public void onDataNotAvailable() {
                mView.showNoContent();
            }
        });
    }
}
