package com.dhn.peanut.shots;

import com.dhn.peanut.data.Shot;

import java.util.List;

/**
 * Created by DHN on 2016/5/31.
 */
public interface ShotsContract {
    interface  View {
        void setPresenter(Presenter presenter);
        void showShots(List<Shot> shots);
        void showLoadingIndicator(boolean isLoading);
        void showShotDetails(Shot shot);
        void showLoading();
        void hideLoading();
    }

    interface Presenter {
        void loadShots(boolean isFirstPage);
        void loadDebuts(boolean isFirstPage);
        void loadGifs(boolean isFirstPage);
        
    }
}
