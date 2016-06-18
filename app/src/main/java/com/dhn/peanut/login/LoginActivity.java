package com.dhn.peanut.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dhn.peanut.R;
import com.dhn.peanut.util.AuthoUtil;
import com.dhn.peanut.util.Log;
import com.dhn.peanut.util.PeanutInfo;
import com.dhn.peanut.util.RequestManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.login_webview)
    WebView mWebView;
    @BindView(R.id.login_progress)
    ProgressBar mLoading;

    private RequestQueue mRequestQueue = RequestManager.newInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //获取token
        String token = AuthoUtil.getToken();

        if (TextUtils.isEmpty(token)) {
            //进行验证
            if (Log.DBG) {
                Log.e("token is null, need authorization");
            }

            mLoading.setVisibility(View.VISIBLE);

            //配置webview属性
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (Log.DBG) {
                        Log.i("redirect url = " + url);
                    }
                    String code = null;
                    //从回调url中提取code信息
                    if (url.startsWith(PeanutInfo.CALLBACK_URL)) {
                        if (url.contains("code")) {
                            code = extractCode(url);
                        }

                        if (!TextUtils.isEmpty(code)) {
                            //获取token
                            requestForAccessToken(code);
                        } else {
                            Toast.makeText(LoginActivity.this, "出错啦，code为空", Toast.LENGTH_SHORT).show();
                        }

                        //忽略发送从定向url的请求
                        return true;
                    }

                    return false;

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    mLoading.setVisibility(View.INVISIBLE);
                }
            });

            if (Log.DBG) {
                Log.e("login url = " + PeanutInfo.LOGIN_URL);
            }
            mWebView.loadUrl(PeanutInfo.LOGIN_URL);
        } else {
            //已获得token,直接使用
            if (Log.DBG) {
                Log.e("already get token");
            }
            onCompleteAuth();
        }

    }

    private String extractCode(String url) {
        int startIndex = url.indexOf("code=") + "code=".length();
        int endIndex = url.indexOf("&state");
        String code = url.substring(startIndex, endIndex);
        Log.i("code=" + code);
        return code;
    }

    /**
     * 利用code获取token
     * @param code
     */
    private void requestForAccessToken(String code) {
        final JSONObject requestJson = new JSONObject();
        try {
            requestJson.put("client_id", PeanutInfo.CLIENT_ID);
            requestJson.put("client_secret", PeanutInfo.CLIENT_SECRET);
            requestJson.put("code", code);
            requestJson.put("state", PeanutInfo.mState);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                PeanutInfo.TOKEN_URL,
                requestJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //保存token
                            String accessToken = (String) response.get("access_token");
                            AuthoUtil.putToken(accessToken);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(LoginActivity.this, "授权成功", Toast.LENGTH_LONG).show();
                        CookieManager.getInstance().removeAllCookie();
                        onCompleteAuth();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "出错啦，获取token失败", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestManager.addRequest(mRequestQueue, jsonObjectRequest, null);
    }

    private void onCompleteAuth() {
        fetchUserInfo();
        mLoading.setVisibility(View.INVISIBLE);
    }

    private void fetchUserInfo() {
        final String token = AuthoUtil.getToken();

        if (token == null) {
            Toast.makeText(LoginActivity.this, "出错啦，token为空", Toast.LENGTH_SHORT).show();
        } else {
            String url = PeanutInfo.URL_MY_INFO;

            JsonObjectRequest profileRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //保存用户信息
                            saveProfile(response);

                            //返回数据
                            String username = "";
                            String avatar_url = "";
                            try {
                                username = response.getString(PeanutInfo.USER_USERNAME);
                                avatar_url = response.getString(PeanutInfo.USER_AVATAR);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //返回用户名和头像地址
                            returnValue(username, avatar_url);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, "获取token失败", Toast.LENGTH_SHORT).show();

                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(PeanutInfo.HEAD_AUTH_FILED, "Bearer " + token);
                    params.putAll(super.getHeaders());
                    return params;

                }
            };

            RequestManager.addRequest(mRequestQueue, profileRequest, null);
        }
    }

    private void returnValue(String username, String avatar_url) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(PeanutInfo.USER_USERNAME, username);
        returnIntent.putExtra(PeanutInfo.USER_AVATAR, avatar_url);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void saveProfile(JSONObject response) {
        try {
            String userName = response.getString(PeanutInfo.USER_USERNAME);

            AuthoUtil.putId(response.getInt("id"));
            AuthoUtil.putUserName(response.getString("username"));
            AuthoUtil.putUserAvatar(response.getString("avatar_url"));
            AuthoUtil.putLikeUrl(response.getString("likes_url"));
            AuthoUtil.putLikesCount(response.getInt("likes_count"));

            Log.e("id=" + AuthoUtil.getId() + " username=" + userName);
            Log.e("isLogined = " + AuthoUtil.isLogined());
            Log.e("avatar_url = " + AuthoUtil.getUserAvatar());
            Log.e(("likes_count = " + AuthoUtil.getLikesCount()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}
