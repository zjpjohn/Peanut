package com.dhn.peanut.util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.dhn.peanut.data.Shot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DHN on 2016/6/1.
 */
public class Request4Shots extends Request<ArrayList<Shot>> {

    private Response.Listener<ArrayList<Shot>> listener;
    private static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Accept", "application/vnd.dribbble.v1.param+json");
        headers.put("Authorization", "Bearer " + PeanutInfo.CLIENT_TOLEN);
    }

    public Request4Shots(String url, Response.Listener<ArrayList<Shot>> listener , Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    protected Response<ArrayList<Shot>> parseNetworkResponse(NetworkResponse response) {
        try {
            String resultStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<Shot>>(){}.getType();
            ArrayList<Shot> shots = gson.fromJson(resultStr, collectionType);

            return Response.success(shots,  HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }


    }

    @Override
    protected void deliverResponse(ArrayList<Shot> response) {
        listener.onResponse(response);
    }
}
