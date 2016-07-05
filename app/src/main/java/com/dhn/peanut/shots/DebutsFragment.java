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
import com.dhn.peanut.data.Shot;
import com.dhn.peanut.shots.ShotsContract;
import com.dhn.peanut.view.AutoLoadRecyclerView;

import java.util.List;


public class DebutsFragment extends BaseFragment {
    public static final String TAG = "DebutsFragment";

    public DebutsFragment(){};
    public static DebutsFragment newInstance() {
        return new DebutsFragment();
    }

    @Override
    protected void initView() {
        super.initView();
        mRecyclerView.setLoadMoreListener(new AutoLoadRecyclerView.LoadMoreListener() {
            @Override
            public void loadMore() {
                mPresenter.loadDebuts(false);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadDebuts(true);
            }
        });

        mPresenter.loadDebuts(true);
    }


}
