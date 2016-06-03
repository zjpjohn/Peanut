package com.dhn.peanut.shots;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dhn.library.HourglassLoadingView;
import com.dhn.peanut.PictrueDetail.PictureDetailActivity;
import com.dhn.peanut.R;
import com.dhn.peanut.ShotDetail.ShotDtailActivity;
import com.dhn.peanut.data.remote.RemoteShotData;
import com.dhn.peanut.view.AutoLoadRecyclerView;
import com.dhn.peanut.data.Shot;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShotFragment extends Fragment implements ShotsContract.View {

    public static final String TAG = "ShotFragment";

    enum ListType {SHOTS, DEBUTS, GIFS};

    @BindView(R.id.refreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview)
    AutoLoadRecyclerView mRecyclerView;
    @BindView(R.id.loading)
    HourglassLoadingView mLoadingView;

    private ShotAdapter shotAdapter;
    private ShotsContract.Presenter mPresenter;
    private ListType listType = ListType.SHOTS;

    public ShotFragment() {
    };

    public static ShotFragment newInstance() {
        ShotFragment fragment = new ShotFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shots, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        mPresenter.loadShots(true);

    }

    private void initView() {
        //RecyclerView
        shotAdapter = new ShotAdapter(getActivity(), this);
        mRecyclerView.setAdapter(shotAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setLoadMoreListener(new AutoLoadRecyclerView.LoadMoreListener() {
            @Override
            public void loadMore() {
                if (listType == ListType.SHOTS) {
                    mPresenter.loadShots(false);
                } else if (listType == ListType.DEBUTS) {
                    mPresenter.loadDebuts(false);
                } else {
                    mPresenter.loadGifs(false);
                }
            }
        });

        //SwiperefreshLayout
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (listType == ListType.SHOTS) {
                    mPresenter.loadShots(true);
                } else if (listType == ListType.DEBUTS) {
                    mPresenter.loadDebuts(true);
                } else {
                    mPresenter.loadGifs(true);
                }
            }
        });

        setHasOptionsMenu(true);

    }

    @Override
    public void setPresenter(ShotsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showShots(List<Shot> shots) {
        shotAdapter.relpaceData(shots);
    }

    @Override
    public void showLoadingIndicator(boolean isLoading) {
        mSwipeRefreshLayout.setRefreshing(isLoading);
    }

    @Override
    public void showShotDetails(Shot shot) {
        //TODO 跳转到图片详情页
        Intent intent = new Intent(getContext(), PictureDetailActivity.class);
        intent.putExtra("shot", shot);
        startActivity(intent);
    }

    @Override
    public void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mLoadingView.start();
    }

    @Override
    public void hideLoading() {
        mLoadingView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingView.stop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_shots:
                if (listType != ListType.SHOTS) {
                    mPresenter.loadShots(true);
                    listType = ListType.SHOTS;
                }
                return true;
            case R.id.menu_debuts:
                if (listType != ListType.DEBUTS) {
                    mPresenter.loadDebuts(true);
                    listType = ListType.DEBUTS;
                }
                Log.e(TAG, "type is debuts");
                return true;
            case R.id.menu_gifs:
                if (listType != ListType.GIFS) {
                    mPresenter.loadGifs(true);
                    listType = ListType.GIFS;
                }
                Log.e(TAG, "type is gifs");
                return true;
        }
        return false;
    }
}
