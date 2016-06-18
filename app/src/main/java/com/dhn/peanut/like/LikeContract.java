package com.dhn.peanut.like;

import com.dhn.peanut.data.LikedShot;
import com.dhn.peanut.data.Shot;

import java.util.List;

/**
 * Created by DHN on 2016/6/8.
 */
public interface LikeContract {
    interface View {
        void setPresenter(Presenter presenter);
        void showLikes(List<LikedShot> shots);
        void showLoadingIndicator(boolean isLoading);
        void showShotDetails(LikedShot shot);
        void showLoading();
        void hideLoading();
        void showNoContent();
        void showNeedAutho();
    }

    interface Presenter {
        void loadLikes();
    }
}
