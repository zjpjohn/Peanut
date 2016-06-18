package com.dhn.peanut.data.base;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.dhn.peanut.PeanutApplication;
import com.dhn.peanut.data.LikedShot;
import com.dhn.peanut.data.Shot;
import com.dhn.peanut.util.AuthoUtil;
import com.dhn.peanut.util.Log;
import com.dhn.peanut.util.Request4LikedShot;
import com.dhn.peanut.util.Request4Shots;
import com.dhn.peanut.util.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DHN on 2016/6/8.
 */
public class LikeDataSource {
    public interface LoadLikeCallback {
        void onShotsLoaded(List<LikedShot> shots);
        void onDataNotAvailable();
    }

    private RequestQueue requestQueue = Volley.newRequestQueue(PeanutApplication.getContext());

    public void getLikes(final LoadLikeCallback loadLikeCallback) {
        String url = AuthoUtil.getLikesUrl() + "?per_page=100";

        if (Log.DBG) {
            Log.e("liked url = " + url);
        }
        if (url == null) {
            loadLikeCallback.onDataNotAvailable();
        } else {
            Request4LikedShot request4LikedShot = new Request4LikedShot(
                    url,
                    new Response.Listener<ArrayList<LikedShot>>() {
                        @Override
                        public void onResponse(ArrayList<LikedShot> response) {
                            if (Log.DBG) {
                                Log.e("liked shot = " + response.toString());
                            }
                            loadLikeCallback.onShotsLoaded(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loadLikeCallback.onDataNotAvailable();
                        }
                    }
            );

            RequestManager.addRequest(requestQueue, request4LikedShot, null);
        }

    }
}
