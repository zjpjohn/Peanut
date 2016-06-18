package com.dhn.peanut.data;

import java.io.Serializable;

/**
 * Created by DHN on 2016/6/17.
 */
public class Following implements Serializable {
    private Shot.User followee;

    public Shot.User getFollowee() {
        return followee;
    }

    public void setFollowee(Shot.User followee) {
        this.followee = followee;
    }

    @Override
    public String toString() {
        return "Following{" +
                "followee=" + followee +
                '}';
    }
}
