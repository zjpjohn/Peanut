package com.dhn.peanut.data;

import java.io.Serializable;

/**
 * Created by DHN on 2016/5/31.
 */
public class Shot implements Serializable{

    public static final String TOKEN = "0ae9635125e0dce4cdec4ba13634f6e2f829edf06d775a330dd3c0d185302468";
    public static final String BASE_URL = "https://api.dribbble.com/v1/shots";

    private int id;
    private String title;
    private String description;
    private Images images;
    private int views_count;
    private int likes_count;
    private int comments_count;
    private String created_at;

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    private String html_url;
    private boolean animated;
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public int getViews_count() {
        return views_count;
    }

    public void setViews_count(int views_count) {
        this.views_count = views_count;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Shot{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", images=" + images +
                ", views_count=" + views_count +
                ", likes_count=" + likes_count +
                ", comments_count=" + comments_count +
                ", created_at='" + created_at + '\'' +
                ", animated=" + animated +
                ", user=" + user +
                '}';
    }


    public static class Images implements Serializable {
        private String hidpi;
        private String normal;
        private String teaser;

        public String getHidpi() {
            return hidpi;
        }

        public void setHidpi(String hidpi) {
            this.hidpi = hidpi;
        }

        public String getNormal() {
            return normal;
        }

        public void setNormal(String normal) {
            this.normal = normal;
        }

        public String getTeaser() {
            return teaser;
        }

        public void setTeaser(String teaser) {
            this.teaser = teaser;
        }
    }

    public static class User implements Serializable {
        private int id;
        private String username;
        private String avatar_url;

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public User(String username, int id) {
            this.username = username;
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", avatar_url='" + avatar_url + '\'' +
                    '}';
        }
    }

}
