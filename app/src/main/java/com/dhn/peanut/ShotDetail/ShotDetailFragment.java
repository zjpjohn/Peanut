package com.dhn.peanut.shotdetail;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dhn.peanut.PeanutApplication;
import com.dhn.peanut.R;
import com.dhn.peanut.data.Comment;
import com.dhn.peanut.data.Shot;
import com.dhn.peanut.util.ShareUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;

/**
 * Created by DHN on 2016/6/1.
 */
public class ShotDetailFragment extends Fragment implements ShotDetailContract.View{

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private Menu mMenu;

    private Shot mShot;
    private ShotDetailContract.Presenter mPresenter;
    private CommentAdapter commentAdapter;

    public ShotDetailFragment() {};

    public static ShotDetailFragment getInstance(Shot shot) {
        ShotDetailFragment fragment = new ShotDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("shot", shot);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mShot = (Shot) getArguments().get("shot");
        }

        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shot_detail, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.shot_detail_pb);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.detail_recyclerview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        //show data in view
        mPresenter.loadComment(mShot.getId());
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.checkLiked(mShot.getId());
            }
        }, 500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = PeanutApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentAdapter = new CommentAdapter(getContext(), mShot);
        mRecyclerView.setAdapter(commentAdapter);
    }

    @Override
    public void setPresenter(ShotDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showComments(List<Comment> comments) {
        commentAdapter.replaceComments(comments);
    }

    @Override
    public void showProgress() {
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }


    @Override
    public void showLike() {
        mMenu.findItem(R.id.menu_like).setIcon(R.drawable.heart);
    }

    @Override
    public void showUnLike() {
        mMenu.findItem(R.id.menu_like).setIcon(R.drawable.heart_outline);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_shot_detail, menu);
        mMenu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                //TODO
                ShareUtil.shareText(getActivity(), mShot.getHtml_url());
                break;
            case R.id.menu_like:
                mPresenter.changeLike(mShot.getId());
                break;
        }
        return true;

    }
}
