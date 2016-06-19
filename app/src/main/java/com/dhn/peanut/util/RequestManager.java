package com.dhn.peanut.util;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dhn.peanut.PeanutApplication;

/**
 * Created by DHN on 2016/6/1.
 */
public class RequestManager {

    public static final int OUT_TIME = 10000;
    public static final int TIMES_OF_RETRY = 5;

    private RequestManager() {
    }

    public static RequestQueue newInstance() {
        return Volley.newRequestQueue(PeanutApplication.getContext());
    }

    public static void addRequest(RequestQueue requestQueue, Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        request.setRetryPolicy(new DefaultRetryPolicy(
                OUT_TIME,
                TIMES_OF_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);

    }

    public static void cancelAll(RequestQueue requestQueue, Object tag) {
        requestQueue.cancelAll(tag);
    }
}
