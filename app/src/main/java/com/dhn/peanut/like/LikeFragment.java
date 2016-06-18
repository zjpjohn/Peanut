package com.dhn.peanut.like;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhn.library.HourglassLoadingView;
import com.dhn.peanut.PeanutApplication;
import com.dhn.peanut.R;
import com.dhn.peanut.data.LikedShot;
import com.dhn.peanut.data.Shot;
import com.dhn.peanut.shotdetail.ShotDetailActivity;
import com.dhn.peanut.util.Log;
import com.dhn.peanut.view.AutoLoadRecyclerView;
import com.squareup.leakcanary.RefWatcher;
import com.victor.loading.rotate.RotateLoading;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LikeFragment extends Fragment implements LikeContract.View{

    @BindView(R.id.refreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.rotateloading)
    RotateLoading mLoading;
    @BindView(R.id.like_nolike_tv)
    TextView mNoContentTv;

    LikeContract.Presenter mPresenter;
    LikeAdapter likedapter;

    public static LikeFragment newInstance() {
        LikeFragment fragment = new LikeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_like, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        mPresenter.loadLikes();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = PeanutApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    private void initView() {
        //RecyclerView
        likedapter = new LikeAdapter(getActivity(), this);
        if (mRecyclerView == null) {
            Log.e("recyclerview == null");
        } else {
            mRecyclerView.setAdapter(likedapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        //SwiperefreshLayout
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadLikes();
            }
        });
    }

    @Override
    public void setPresenter(LikeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLikes(List<LikedShot> shots) {
        likedapter.replaceData(shots);
    }

    @Override
    public void showLoadingIndicator(boolean isLoading) {
        mSwipeRefreshLayout.setRefreshing(isLoading);
    }

    @Override
    public void showShotDetails(LikedShot shot) {
        Intent intent = new Intent(getActivity(), ShotDetailActivity.class);
        intent.putExtra("shot", shot.getShot());
        startActivity(intent);
    }

    @Override
    public void showLoading() {
        mLoading.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mLoading.start();
    }

    @Override
    public void hideLoading() {
        mLoading.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoading.stop();
    }

    @Override
    public void showNoContent() {
        mRecyclerView.setVisibility(View.GONE);
        mLoading.setVisibility(View.GONE);
        mNoContentTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNeedAutho() {
        mRecyclerView.setVisibility(View.GONE);
        mLoading.setVisibility(View.GONE);
        mNoContentTv.setText("请先登录");
        mNoContentTv.setVisibility(View.VISIBLE);
    }
}
