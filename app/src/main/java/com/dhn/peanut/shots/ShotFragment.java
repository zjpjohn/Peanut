package com.dhn.peanut.shots;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhn.peanut.view.AutoLoadRecyclerView;

/**
 * Created by DHN on 2016/6/6.
 */
public class ShotFragment extends BaseFragment {
    public static final String TAG = "ShotFragment";

    public ShotFragment() {
    };

    public static BaseFragment newInstance() {
        ShotFragment fragment = new ShotFragment();
        return fragment;
    }

    @Override
    protected void initView() {
        super.initView();

        mRecyclerView.setLoadMoreListener(new AutoLoadRecyclerView.LoadMoreListener() {
            @Override
            public void loadMore() {
                mPresenter.loadShots(false);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadShots(true);
            }
        });

        //加载第一页数据
        mPresenter.loadShots(true);
    }


}
