package com.dhn.peanut.shotdetail;

import com.dhn.peanut.data.Comment;

import java.util.List;

/**
 * Created by DHN on 2016/6/1.
 */
public interface ShotDetailContract {
    interface View {
        void setPresenter(Presenter presenter);
        void showComments(List<Comment> comments);
        void showProgress();
        void hideProgress();
        void showLike();
        void showUnLike();
        void showToast(String text);
    }

    interface Presenter {
        void loadComment(int shotId);
        void checkLiked(int id);         //根据是否喜欢该shot，设置样式
        void changeLike(int id);

    }
}
