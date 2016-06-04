package com.dhn.peanut.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dhn.peanut.R;
import com.dhn.peanut.util.Log;
import com.dhn.peanut.util.PeanutInfo;
import com.dhn.peanut.util.RequestManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    public static final String DRIBBLE_SHARE = "com.dhn.dribble.share";
    public static final String DRIBBLE_TOKEN = "com.dhn.dribble.token";

    @BindView(R.id.login_webview)
    WebView mWebView;
    @BindView(R.id.login_progress)
    ProgressBar mLoading;

    private SharedPreferences mTokenShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        mTokenShare = getSharedPreferences(DRIBBLE_SHARE, Context.MODE_PRIVATE);
        String token = mTokenShare.getString(DRIBBLE_TOKEN, null);

        if (TextUtils.isEmpty(token)) {
            //进行验证
            if (Log.DBG) {
                Log.e("token is null, need authorization");
            }

            mLoading.setVisibility(View.VISIBLE);

            CookieManager.getInstance().removeAllCookie();

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
                            Toast.makeText(LoginActivity.this, "code is empty", Toast.LENGTH_SHORT).show();
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
                        Log.i(response.toString());
                        try {
                            //保存token
                            String accessToken = (String) response.get("access_token");
                            SharedPreferences.Editor editor = mTokenShare.edit();
                            editor.putString(PeanutInfo.TOKEN_FIELD, accessToken);
                            editor.commit();

                            if (Log.DBG) {
                                Log.e("token:" + accessToken);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(LoginActivity.this, "Authorization success", Toast.LENGTH_LONG).show();
                        CookieManager.getInstance().removeAllCookie();
                        onCompleteAuth();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "get token error", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestManager.addRequest(jsonObjectRequest, null);
    }

    private void onCompleteAuth() {
        fetchUserInfo();
        mLoading.setVisibility(View.INVISIBLE);
    }

    private void fetchUserInfo() {
        final String token = mTokenShare.getString(PeanutInfo.TOKEN_FIELD, null);
        if (token == null) {
            Toast.makeText(LoginActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
        } else {
            String url = PeanutInfo.MY_INFO_URL;

            JsonObjectRequest profileRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            saveProfile(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

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

            RequestManager.addRequest(profileRequest, null);
        }
    }

    private void saveProfile(JSONObject response) {
        try {
            int id = response.getInt("id");
            String userName = response.getString("username");

            if (Log.DBG) {
                Log.i("id=" + id + " username=" + userName);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
