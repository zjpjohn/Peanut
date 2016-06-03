package com.dhn.peanut.data;

import java.io.Serializable;

/**
 * Created by DHN on 2016/6/1.
 */
public class Comment implements Serializable {

    //base_url/shot_id/comments
    public static final String COMMENTS_BASE_URL = "https://api.dribbble.com/v1/shots/";

    private int id;
    private String body;
    private Shot.User user;

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
