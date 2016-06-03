package com.dhn.peanut.ShotDetail;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dhn.peanut.R;
import com.dhn.peanut.data.Shot;
import com.dhn.peanut.data.remote.RemoteCommentsData;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShotDtailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Shot mShot;
    private ShotDetailFragment mFragment;
    private ShotDtailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shot_dtail);
        initData();
        initView();
    }


    private void initData() {
        mShot = (Shot) getIntent().getSerializableExtra("shot");
    }

    private void initView() {
        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //添加引用关系
        mFragment = ShotDetailFragment.getInstance(mShot);
        mPresenter = new ShotDtailPresenter(mFragment, RemoteCommentsData.getInstance());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_content, mFragment)
                .commit();
    }



}
