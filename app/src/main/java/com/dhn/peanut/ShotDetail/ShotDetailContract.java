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
        void showPicture();
        void showPicInfor();
        void showLike();
        void showUnLike();
    }

    interface Presenter {
        void loadComment(int shotId);
        void share();
        void checkLiked(int id);         //根据是否喜欢该shot，设置样式
        void changeLike(int id);

    }
}
