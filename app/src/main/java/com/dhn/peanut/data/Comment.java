package com.dhn.peanut.data;

import android.preference.PreferenceActivity;

import java.io.Serializable;

/**
 * Created by DHN on 2016/6/1.
 */
public class Comment implements Serializable {

    //base_url/shot_id/comments
    public static final String COMMENTS_BASE_URL = "https://api.dribbble.com/v1/shots/";

    public static final int NORNAL = 0;
    public static final int HEADER = 1;

    private int id;
    private String body;
    private Shot.User user;
    private int type = NORNAL;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Shot.User getUser() {
        return user;
    }

    public void setUser(Shot.User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", user=" + user +
                '}';
    }
}
