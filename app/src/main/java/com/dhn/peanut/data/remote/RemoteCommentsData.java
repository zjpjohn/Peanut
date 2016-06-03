package com.dhn.peanut.data.remote;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dhn.peanut.data.Comment;
import com.dhn.peanut.data.base.CommentDataSource;
import com.dhn.peanut.util.RequestManager;
import com.dhn.peanut.util.Requet4Comments;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DHN on 2016/6/2.
 */
public class RemoteCommentsData implements CommentDataSource{

    public static RemoteCommentsData INSTANCE;

    public static RemoteCommentsData getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteCommentsData();
        }
        return INSTANCE;
    }


    @Override
    public void getComment(int shotId, final LoadCommentCallBack commentCallBack) {
        //发起网络请求
        String url = Comment.COMMENTS_BASE_URL + shotId + "/" + "comments";
        RequestManager.addRequest(new Requet4Comments(url, new Response.Listener<List<Comment>>() {
            @Override
            public void onResponse(List<Comment> response) {
                Log.e("remote", response.toString());
                commentCallBack.onCommentLoaded(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }), null);
    }
}
