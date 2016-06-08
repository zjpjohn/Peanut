package com.dhn.peanut.shotdetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.dhn.peanut.R;
import com.dhn.peanut.data.Shot;
import com.dhn.peanut.data.remote.RemoteCommentsData;

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
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setTitle(mShot.getTitle());
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.arrow_left);

        //添加引用关系
        mFragment = ShotDetailFragment.getInstance(mShot);
        mPresenter = new ShotDtailPresenter(mFragment, RemoteCommentsData.getInstance());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_content, mFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //左上角返回按钮
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}