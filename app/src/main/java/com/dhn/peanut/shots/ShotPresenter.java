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

    private ShotsContract.View mView;
    private ShotDataSource mShotDataSource;

    private int shotsPage = 1;
    private int debutsPage = 1;
    private int gifsPage = 1;

    public ShotPresenter(ShotsContract.View view, ShotDataSource dataSource) {

        mView = view;
        mShotDataSource = dataSource;
        mView.setPresenter(this);

    }


    @Override
    public void loadShots(boolean isFirstPage) {
        if (isFirstPage == true) {
            shotsPage = 1;
        }

        if (shotsPage == 1) {
            mView.showLoading();
        }

        mShotDataSource.getShots(null, shotsPage, new ShotDataSource.LoadShotsCallback() {
            @Override
            public void onShotsLoaded(List<Shot> shots) {
                mView.hideLoading();
                mView.showLoadingIndicator(false);
                mView.showShots(shots);

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
            mView.showLoading();
        }

        mShotDataSource.getDebuts(null, debutsPage, new ShotDataSource.LoadShotsCallback() {
            @Override
            public void onShotsLoaded(List<Shot> debuts) {
                mView.hideLoading();
                mView.showLoadingIndicator(false);
                mView.showShots(debuts);

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
            mView.showLoading();
        }

        mShotDataSource.getGifs(null, gifsPage, new ShotDataSource.LoadShotsCallback() {
            @Override
            public void onShotsLoaded(List<Shot> gifs) {
                mView.hideLoading();
                mView.showLoadingIndicator(false);
                mView.showShots(gifs);

                Log.e(TAG, "gifs: " + gifs);

            }

            @Override
            public void onDataNotAvailable() {

            }
        });

        gifsPage++;
    }


}
