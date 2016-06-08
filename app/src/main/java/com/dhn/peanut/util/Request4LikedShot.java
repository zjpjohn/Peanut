package com.dhn.peanut.util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.dhn.peanut.data.LikedShot;
import com.dhn.peanut.data.Shot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DHN on 2016/6/8.
 */
public class Request4LikedShot extends Request<ArrayList<LikedShot>> {

    private Response.Listener<ArrayList<LikedShot>> listener;
    private static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Accept", "application/vnd.dribbble.v1.param+json");
        headers.put("Authorization", "Bearer 4e3e676ce2881d166900f7f0ba4f1c0c599f3126ff426c78e61fd3fc233b2a32");
    }

    public Request4LikedShot(String url, Response.Listener<ArrayList<LikedShot>> listener , Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    protected Response<ArrayList<LikedShot>> parseNetworkResponse(NetworkResponse response) {
        try {
            String resultStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<LikedShot>>(){}.getType();
            ArrayList<LikedShot> shots = gson.fromJson(resultStr, collectionType);

            return Response.success(shots,  HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }


    }

    @Override
    protected void deliverResponse(ArrayList<LikedShot> response) {
        listener.onResponse(response);
    }
}
