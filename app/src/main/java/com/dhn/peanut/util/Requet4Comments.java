package com.dhn.peanut.util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.dhn.peanut.data.Comment;
import com.dhn.peanut.data.Shot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DHN on 2016/6/2.
 */
public class Requet4Comments extends Request<ArrayList<Comment>> {

    private String url;
    private Response.Listener<ArrayList<Comment>> listener;

    public Requet4Comments(String url, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.url = url;
        this.listener = listener;
    }

    private static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Accept", "application/vnd.dribbble.v1.param+json");
        headers.put("Authorization", "Bearer " + PeanutInfo.CLIENT_TOLEN);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    protected Response<ArrayList<Comment>> parseNetworkResponse(NetworkResponse response) {
        String resultStr = null;
        try {
            resultStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<Comment>>() {}.getType();
            ArrayList<Comment> comments = gson.fromJson(resultStr, collectionType);

            return Response.success(comments, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));

        }

    }

    @Override
    protected void deliverResponse(ArrayList<Comment> response) {
        listener.onResponse(response);
    }
}
