package com.dhn.peanut.shots;



import android.util.Log;

import com.dhn.peanut.data.Shot;
import com.dhn.peanut.data.base.ShotDataSource;

import java.util.List;

/**
 * Created by DHN on 2016/5/31.
 */
public class ShotPresenter implements ShotsContract.Presenter {

    public static final String TAG = "ShortPresenter";

    private ShotsContract.View mShotView;
    private ShotsContract.View mDebutsView;
    private ShotsContract.View mGifView;
    private ShotDataSource mShotDataSource;

    private int shotsPage = 1;
    private int debutsPage = 1;
    private int gifsPage = 1;

    public ShotPresenter(ShotsContract.View shotView,
                         ShotsContract.View debutsView,
                         ShotsContract.View gifView,
                         ShotDataSource dataSource) {

        mShotView = shotView;
        mDebutsView = debutsView;
        mGifView = gifView;

        mShotDataSource = dataSource;

        mShotView.setPresenter(this);
        mDebutsView.setPresenter(this);
        mGifView.setPresenter(this);
    }

    @Override
    public void loadShots(boolean isFirstPage) {
        if (isFirstPage == true) {
            shotsPage = 1;
        }

        if (shotsPage == 1) {
            mShotView.showLoading();
        }

        mShotDataSource.getShots(null, shotsPage, new ShotDataSource.LoadShotsCallback() {
            @Override
            public void onShotsLoaded(List<Shot> shots) {
                mShotView.hideLoading();
                mShotView.showLoadingIndicator(false);
                mShotView.showShots(shots);

                Log.e(TAG, "shots: " + shots);

            }

            @Override
            public void onDataNotAvailable() {

            }
        });

        shotsPage++;
    }

    @Override
    public void loadDebuts(boolean isFirstPage) {
        if (isFirstPage == true) {
            debutsPage = 1;
        }

        if (debutsPage == 1) {
            mDebutsView.showLoading();
        }

        mShotDataSource.getDebuts(null, debutsPage, new ShotDataSource.LoadShotsCallback() {
            @Override
            public void onShotsLoaded(List<Shot> debuts) {
                mDebutsView.hideLoading();
                mDebutsView.showLoadingIndicator(false);
                mDebutsView.showShots(debuts);

                Log.e(TAG, "debuts: " + debuts);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

        debutsPage++;
    }

    @Override
    public void loadGifs(boolean isFirstPage) {
        if (isFirstPage == true) {
            gifsPage = 1;
        }

        if (gifsPage == 1) {
            mGifView.showLoading();
        }

        mShotDataSource.getGifs(null, gifsPage, new ShotDataSource.LoadShotsCallback() {
            @Override
            public void onShotsLoaded(List<Shot> gifs) {
                mGifView.hideLoading();
                mGifView.showLoadingIndicator(false);
                mGifView.showShots(gifs);

                Log.e(TAG, "gifs: " + gifs);

            }

            @Override
            public void onDataNotAvailable() {

            }
        });

        gifsPage++;
    }


}
