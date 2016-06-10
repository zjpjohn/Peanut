package com.dhn.peanut.like;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dhn.peanut.R;
import com.dhn.peanut.data.base.LikeDataSource;

public class LikeActivity extends AppCompatActivity {

    LikeContract.View mView;
    LikePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        toolbar.setTitle("My like");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_left);

        mView = LikeFragment.newInstance();
        mPresenter = new LikePresenter(mView, new LikeDataSource());

        getSupportFragmentManager().beginTransaction().replace(R.id.like_content, (Fragment) mView).commit();
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
