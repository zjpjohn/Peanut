package com.dhn.peanut.following;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dhn.peanut.R;
import com.dhn.peanut.data.Following;
import com.dhn.peanut.util.AuthoUtil;
import com.dhn.peanut.util.PeanutInfo;
import com.dhn.peanut.util.Request4Following;
import com.dhn.peanut.util.RequestManager;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowingActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_following)
    RecyclerView mRecyclerView;
    @BindView(R.id.rotateloading)
    RotateLoading mLoading;
    @BindView(R.id.tv_no_data)
    TextView mTvNoData;

    private FollowingAdapter mAdapter;
    private RequestQueue mRequestQueue = RequestManager.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        ButterKnife.bind(this);

        mToolbar.setTitle("关注者");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.arrow_left);

        if (!AuthoUtil.isLogined()) {
            showNeedAutho();
        } else {
            mLoading.start();
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mAdapter = new FollowingAdapter(this);

            Request4Following request4Following = new Request4Following(
                    PeanutInfo.URL_FOLLOWINGS,
                    new Response.Listener<ArrayList<Following>>() {
                        @Override
                        public void onResponse(ArrayList<Following> response) {
                            if (response.isEmpty()) {
                                showNoData();
                            } else {
                                mRecyclerView.setAdapter(mAdapter);
                                mAdapter.replaceData(response);
                                mLoading.stop();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );

            RequestManager.addRequest(mRequestQueue, request4Following, this);
        }
    }

    public void showNoData() {
        mTvNoData.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mLoading.setVisibility(View.GONE);
    }

    public void showNeedAutho() {
        mTvNoData.setVisibility(View.VISIBLE);
        mTvNoData.setText("请先登录");
        mRecyclerView.setVisibility(View.GONE);
        mLoading.setVisibility(View.GONE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(mRequestQueue, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
