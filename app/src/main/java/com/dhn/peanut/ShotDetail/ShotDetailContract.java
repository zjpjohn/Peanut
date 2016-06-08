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
    }

    interface Presenter {
        void loadComment(int shotId);
    }
}
