package com.dhn.peanut.data;

import java.io.Serializable;

/**
 * Created by DHN on 2016/6/8.
 */
public class LikedShot implements Serializable {
    private String created_at;
    private Shot shot;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Shot getShot() {
        return shot;
    }

    public void setShot(Shot shot) {
        this.shot = shot;
    }

    @Override
    public String toString() {
        return "LikedShot{" +
                "created_at='" + created_at + '\'' +
                ", shot=" + shot +
                '}';
    }
}
