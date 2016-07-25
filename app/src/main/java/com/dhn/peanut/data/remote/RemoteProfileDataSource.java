package com.dhn.peanut.data.remote;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dhn.peanut.PeanutApplication;
import com.dhn.peanut.data.Shot;
import com.dhn.peanut.data.base.ProfileCallback;
import com.dhn.peanut.data.base.ProfileDataSource;
import com.dhn.peanut.util.AuthoUtil;
import com.dhn.peanut.util.Log;
import com.dhn.peanut.util.PeanutInfo;
import com.dhn.peanut.util.Request4Shots;
import com.dhn.peanut.util.RequestManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DHN on 2016/6/15.
 */
public class RemoteProfileDataSource implements ProfileDataSource {

    private String nextUrl;
    private boolean hasNext = false;
    private List<Shot> lists;
    private boolean isFollowed = false;
    private RequestQueue mRequestQueue;

    public RemoteProfileDataSource() {
        lists = new ArrayList<>();
        mRequestQueue = RequestManager.newInstance();

    }

    @Override
    public void getShot(int userId, boolean first, final ProfileCallback callback) {
        String url = null;
        if (first) {
            lists.clear();
            url = PeanutInfo.URL_BASE + "/users/" + userId + "/shots";
        } else if (hasNext){
            url = nextUrl;
        } else {
            //不会执行
            return;
        }

        Request4Shots request4Shots = new Request4Shots(
                url,
                new Response.Listener<ArrayList<Shot>>() {
                    @Override
                    public void onResponse(ArrayList<Shot> response) {

                        Log.e(response.toString());

                        lists.addAll(response);
                        callback.onLoadProfileLoaded(lists, hasNext);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PeanutApplication.getContext(), "请求失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Response<ArrayList<Shot>> parseNetworkResponse(NetworkResponse response) {
                //获取下一页url
                getNextUrl(response);
                return super.parseNetworkResponse(response);
            }
        };

        RequestManager.addRequest(mRequestQueue, request4Shots, null);
    }

    @Override
    public void checkFollow(int userId, final ProfileCallback callback) {
        String url = PeanutInfo.URL_CHECK_FOLLOWING_BASE + userId;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("checkFollow: onResponse");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFollowChecked(isFollowed);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put(PeanutInfo.HEAD_AUTH_FILED, PeanutInfo.HEAD_BEAR + AuthoUtil.getToken());
                header.putAll(super.getHeaders());
                return header;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                if (response.statusCode == 204 || response.statusCode == 201) {
                    Log.e("checkFollow: 已follow");
                    isFollowed = true;
                }
                return super.parseNetworkResponse(response);
            }
        };

        RequestManager.addRequest(mRequestQueue, request, null);
    }



    @Override
    public void changeFollowState(int userId, final boolean requestFollow, final ProfileCallback callback) {
        int method = requestFollow ? Request.Method.PUT : Request.Method.DELETE;
        String url = PeanutInfo.URL_FOLLOW_BASE + userId + "/follow";
        final JsonObjectRequest request = new JsonObjectRequest(
                method,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("chagneFollowState");
                        callback.onFollowed(requestFollow);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put(PeanutInfo.HEAD_AUTH_FILED, PeanutInfo.HEAD_BEAR + AuthoUtil.getToken());
                header.putAll(super.getHeaders());
                return header;
            }
        };

        RequestManager.addRequest(mRequestQueue, request, null);
    }

    /**
     * 从响应Link头部中获取获取下页url
     * @param response
     */
    private void getNextUrl(NetworkResponse response) {
        String link = response.headers.get("Link");

        Log.e("Link = " + link);

        //可能为空
        if (link != null) {
            String[] links = link.split(",");
            String next = null;
            for (String str :
                    links) {
                String url = str.substring(str.indexOf("<") + 1, str.lastIndexOf(">"));
                String flag = str.substring(str.indexOf("rel=\"") + 5, str.lastIndexOf("\""));
                if (flag.equals("next")) {
                    next = url;
                    break;
                }
            }
            if (next != null) {
                nextUrl = next;
                hasNext = true;
            } else {
                nextUrl = null;
                hasNext = false;
            }
        } else {
            nextUrl = null;
            hasNext = false;
        }
    }
}
