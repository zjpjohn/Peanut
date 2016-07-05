package com.dhn.peanut.shots;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhn.peanut.R;
import com.dhn.peanut.view.AutoLoadRecyclerView;


public class GifFragment extends BaseFragment {
    public static final String TAG = "GifFragment";

    public GifFragment(){};
    public static GifFragment newInstance() {
        return new GifFragment();
    }

    @Override
    protected void initView() {
        super.initView();
        mRecyclerView.setLoadMoreListener(new AutoLoadRecyclerView.LoadMoreListener() {
            @Override
            public void loadMore() {
                mPresenter.loadGifs(false);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadGifs(true);
            }
        });

        mPresenter.loadGifs(true);
    }




}
