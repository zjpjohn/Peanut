package com.dhn.peanut;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dhn.peanut.data.remote.RemoteShotData;
import com.dhn.peanut.following.FollowingActivity;
import com.dhn.peanut.like.LikeActivity;
import com.dhn.peanut.login.LoginActivity;
import com.dhn.peanut.profile.MeActivity;
import com.dhn.peanut.shots.DebutsFragment;
import com.dhn.peanut.shots.GifFragment;
import com.dhn.peanut.shots.ShotFragment;
import com.dhn.peanut.shots.ShotPresenter;
import com.dhn.peanut.shots.ShotsContract;
import com.dhn.peanut.util.AuthoUtil;
import com.dhn.peanut.util.Log;
import com.dhn.peanut.util.PeanutInfo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;

    @BindView(R.id.drawerlayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigationview)
    NavigationView mNavigationView;
    @BindView(R.id.main_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.main_viewpager)
    ViewPager mViewPager;

    private SimpleDraweeView mLeftDraweeView;
    private TextView mLeftNameTv;
    private View mHeader;
    private ShotsContract.View shotFragment;
    private ShotsContract.View debutsFragment;
    private ShotsContract.View gifFragment;
    private ShotsContract.Presenter mPresenter;
    private ActionBarDrawerToggle drawerToggle;

    private SharedPreferences sharePre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();

        initToolbar();
        initNavigation();
        initViewPager();
        initTabLayout();


        if (Log.DBG) {
            Log.e("returned username: " + AuthoUtil.getUserName());
            Log.e("like url = " + AuthoUtil.getLikesUrl());
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String username = data.getStringExtra(PeanutInfo.USER_USERNAME);
                String avatar_url = data.getStringExtra(PeanutInfo.USER_AVATAR);

                if (Log.DBG) {
                    Log.e("returned username: " + AuthoUtil.getUserName());
                    Log.e("like url = " + AuthoUtil.getLikesUrl());
                }

                mLeftNameTv.setText(username);
                Uri uri = Uri.parse(avatar_url);
                mLeftDraweeView.setImageURI(uri);
            }
        }
    }

    private void initData() {
        //添加引用
        shotFragment = ShotFragment.newInstance();
        debutsFragment = DebutsFragment.newInstance();
        gifFragment = GifFragment.newInstance();
        mPresenter = new ShotPresenter(shotFragment, debutsFragment, gifFragment, RemoteShotData.getInstance());


    }

    private void initToolbar() {
        //ToolBar
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        drawerToggle = setupDrawerToggle();
        mDrawerLayout.setDrawerListener(drawerToggle);
    }

    private void initNavigation() {
        //NavigationView
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.menu_like:
                                Intent intent = new Intent(MainActivity.this, LikeActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.menu_following:
                                startActivity(new Intent(MainActivity.this, FollowingActivity.class));
                                break;
                            case R.id.menu_me:
                                startActivity(new Intent(MainActivity.this, MeActivity.class));
                                break;
                            default:
                                break;
                        }

                        item.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        //header
        mHeader = mNavigationView.getHeaderView(0);
        mLeftNameTv = (TextView) mHeader.findViewById(R.id.header_username);
        mLeftDraweeView = (SimpleDraweeView) mHeader.findViewById(R.id.header_draweeview);

        if (AuthoUtil.isLogined()) {
            String username = AuthoUtil.getUserName();
            String avatar_url = AuthoUtil.getUserAvatar();
            mLeftNameTv.setText(username);
            mLeftDraweeView.setImageURI(Uri.parse(avatar_url));
        } else {
            mLeftNameTv.setText("请登录");
            Uri uri = Uri.parse("res://com.dhn.peanut/" + R.drawable.avatar);
            mLeftDraweeView.setImageURI(uri);
        }

        mHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!AuthoUtil.isLogined()) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    //TODO
                    Log.e("isLogined: " + AuthoUtil.isLogined());
                    Log.e("用户名：" + AuthoUtil.getUserName());
                    Log.e("token: " + AuthoUtil.getToken());
                    Log.e("like url" + AuthoUtil.getLikesUrl());
                }

            }
        });
    }

    private void initViewPager() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment((Fragment) shotFragment);
        pagerAdapter.addFragment((Fragment) debutsFragment);
        pagerAdapter.addFragment((Fragment) gifFragment);

        mViewPager.setAdapter(pagerAdapter);
    }

    private void initTabLayout() {
        mTabLayout.addTab(mTabLayout.newTab().setText("SHOTS"));
        mTabLayout.addTab(mTabLayout.newTab().setText("DEBUTS"));
        mTabLayout.addTab(mTabLayout.newTab().setText("GIF"));

        mTabLayout.setupWithViewPager(mViewPager);

        //否则无法显示title
        mTabLayout.getTabAt(0).setText("SHOTS");
        mTabLayout.getTabAt(1).setText("DEBUTS");
        mTabLayout.getTabAt(2).setText("GIF");
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawers();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menu_search:
                Toast.makeText(this, "in develop", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentList = new ArrayList<>();
        }

        public void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }


}
