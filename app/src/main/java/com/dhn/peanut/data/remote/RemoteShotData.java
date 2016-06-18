package com.dhn.peanut.data.remote;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.dhn.peanut.PeanutApplication;
import com.dhn.peanut.data.Shot;
import com.dhn.peanut.data.base.LoadShotsCallback;
import com.dhn.peanut.data.base.ShotDataSource;
import com.dhn.peanut.util.Request4Shots;
import com.dhn.peanut.util.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DHN on 2016/5/31.
 */
public class RemoteShotData implements ShotDataSource {

    private static final String TAG = "RemoteShotData";
    public static RemoteShotData INSTANCE;
    List<Shot> shots;
    List<Shot> debuts;
    List<Shot> gifs;

    private RequestQueue mRequestQueue;

    private RemoteShotData() {
        shots = new ArrayList<>();
        debuts = new ArrayList<>();
        gifs = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(PeanutApplication.getContext());
    }

    //TODO 单例
    public static RemoteShotData getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteShotData();
        }
        return INSTANCE;
    }

    /**
     *
     * @param sort
     * @param page
     * @param callback
     */
    @Override
    public void getShots(String sort, int page, final LoadShotsCallback callback) {
        String url = Shot.BASE_URL + "?page=" + page;

        if (sort != null) {
            url += "&sort=" + sort;
        }

        if (page == 1) {
            shots.clear();
        }
        RequestManager.addRequest(mRequestQueue, new Request4Shots(
                url,
                new Response.Listener<ArrayList<Shot>>() {
                    @Override
                    public void onResponse(ArrayList<Shot> response) {
                        shots.addAll(response);
                        callback.onShotsLoaded(shots);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onDataNotAvailable();
                    }
                }
        ), null);
    }


    @Override
    public void getDebuts(String sort, int page, final LoadShotsCallback callback) {
        String url = Shot.BASE_URL  + "?list=" + "debuts" + "&page=" + page;

        if (sort != null) {
            url += "&sort=" + sort;
        }



        if (page == 1) {
            debuts.clear();
        }
        RequestManager.addRequest(mRequestQueue, new Request4Shots(
                url,
                new Response.Listener<ArrayList<Shot>>() {
                    @Override
                    public void onResponse(ArrayList<Shot> response) {

                        debuts.addAll(response);
                        callback.onShotsLoaded(debuts);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onDataNotAvailable();
                    }
                }
        ), null);
    }

    @Override
    public void getGifs(String sort, int page, final LoadShotsCallback callback) {
        String url = Shot.BASE_URL + "?list=" + "animated" + "&page=" + page;

        if (sort != null) {
            url += "&sort=" + sort;
        }


        if (page == 1) {
            gifs.clear();
        }
        RequestManager.addRequest(mRequestQueue, new Request4Shots(
                url,
                new Response.Listener<ArrayList<Shot>>() {
                    @Override
                    public void onResponse(ArrayList<Shot> response) {

                        gifs.addAll(response);
                        callback.onShotsLoaded(gifs);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onDataNotAvailable();
                    }
                }
        ), null);
    }


}
