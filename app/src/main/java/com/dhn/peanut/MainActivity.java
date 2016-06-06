package com.dhn.peanut;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dhn.peanut.data.remote.RemoteShotData;
import com.dhn.peanut.shots.DebutsFragment;
import com.dhn.peanut.shots.GifFragment;
import com.dhn.peanut.shots.ShotFragment;
import com.dhn.peanut.shots.ShotPresenter;
import com.dhn.peanut.shots.ShotsContract;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

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

    private ShotsContract.View shotFragment;
    private ShotsContract.View debutsFragment;
    private ShotsContract.View gifFragment;
    private ShotsContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //replace fragment
        shotFragment = ShotFragment.newInstance();
        debutsFragment = DebutsFragment.newInstance();
        gifFragment = GifFragment.newInstance();

        //TODO 添加引用
        mPresenter = new ShotPresenter(shotFragment, debutsFragment, gifFragment, RemoteShotData.getInstance());


        initToolbar();
        initNavigation();
        initViewPager();
        initTabLayout();



    }

    private void initToolbar() {
        //ToolBar
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void initNavigation() {
        //NavigationView
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT);

                        switch (item.getItemId()) {
                            case R.id.menu_main:
                                Toast.makeText(getApplicationContext(), "shots", Toast.LENGTH_SHORT);
                                break;
                            case R.id.menu_like:
                                Toast.makeText(getApplicationContext(), "liek", Toast.LENGTH_SHORT);
                                break;
                            case R.id.menu_me:
                                Toast.makeText(getApplicationContext(), "me", Toast.LENGTH_SHORT);
                                break;
                            default:
                                break;
                        }

                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        //header
        View header = mNavigationView.getHeaderView(0);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "move to profile", Toast.LENGTH_SHORT).show();
            }
        });
        SimpleDraweeView profileView = (SimpleDraweeView) header.findViewById(R.id.header_draweeview);
        Uri uri = Uri.parse("res://com.dhn.peanut/" + R.drawable.header_profile);
        profileView.setImageURI(uri);
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
