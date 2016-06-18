package com.dhn.peanut.util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.dhn.peanut.data.Following;
import com.dhn.peanut.data.Shot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DHN on 2016/6/17.
 */
public class Request4Following extends Request<ArrayList<Following>> {

    private Response.Listener<ArrayList<Following>> listener;
    private static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Accept", "application/vnd.dribbble.v1.param+json");
        headers.put("Authorization", "Bearer " + AuthoUtil.getToken());
    }

    public Request4Following(String url, Response.Listener<ArrayList<Following>> listener , Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    protected Response<ArrayList<Following>> parseNetworkResponse(NetworkResponse response) {
        String resultStr = null;
        try {
            resultStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<Following>>(){}.getType();
        ArrayList<Following> shots = gson.fromJson(resultStr, collectionType);

        return Response.success(shots,  HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(ArrayList<Following> response) {
        listener.onResponse(response);
    }
}
