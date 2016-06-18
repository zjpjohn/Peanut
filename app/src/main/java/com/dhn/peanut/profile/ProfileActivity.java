package com.dhn.peanut.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dhn.peanut.R;
import com.dhn.peanut.data.Shot;
import com.dhn.peanut.data.base.ProfileCallback;
import com.dhn.peanut.data.base.ProfileDataSource;
import com.dhn.peanut.data.remote.RemoteProfileDataSource;
import com.dhn.peanut.util.AuthoUtil;
import com.dhn.peanut.util.Log;
import com.dhn.peanut.view.AutoLoadRecyclerView;
import com.victor.loading.rotate.RotateLoading;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.user_info_recyclerview)
    AutoLoadRecyclerView mRecyclerView;
    @BindView(R.id.rotateloading)
    RotateLoading mLoading;

    private Menu mMenu;
    private Shot.User user;
    private ProfileAdapter adapter;
    private ProfileDataSource dataSource;
    private boolean mHasNext;
    private boolean mIsFollowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        user = (Shot.User) intent.getSerializableExtra("user");

        dataSource = new RemoteProfileDataSource();
    }

    private void initView() {
        //Loading
        mLoading.start();
        mRecyclerView.setVisibility(View.INVISIBLE);

        //menu
        if (AuthoUtil.isLogined()) {
            //已登录，查看是否follow
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dataSource.checkFollow(user.getId(), new ProfileCallback() {
                        @Override
                        public void onLoadProfileLoaded(List<Shot> shots, boolean hasNext) {

                        }

                        @Override
                        public void onFollowChecked(boolean isFollowed) {
                            if (isFollowed) {
                                mMenu.findItem(R.id.menu_follow).setTitle("取消关注");
                                mIsFollowed = true;
                            } else {
                                mMenu.findItem(R.id.menu_follow).setTitle("关注");
                                mIsFollowed = false;
                            }
                        }

                        @Override
                        public void onFollowed(boolean isFollow) {

                        }
                    });
                }
            }, 500);
        } else {
            //未登录
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMenu.findItem(R.id.menu_follow).setTitle("未登录");
                }
            }, 500);
        }

        //ToolBar
        mToolbar.setTitle(user.getUsername());
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.arrow_left);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        //RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ProfileAdapter(this, user);
        mRecyclerView.setAdapter(adapter);

        //首次加载
        dataSource.getShot(user.getId(), true, new ProfileCallback() {
            @Override
            public void onLoadProfileLoaded(List<Shot> shots, boolean hasNext) {

                //Log.e("hasnext= " + hasNext + ", profile loaded: " + shots.toString());
                mRecyclerView.setVisibility(View.VISIBLE);
                mHasNext = hasNext;
                if (!hasNext) {
                    adapter.setShowFooter(false);
                }
                adapter.replaceData(shots);

                mLoading.stop();

            }

            @Override
            public void onFollowChecked(boolean isFollowd) {

            }

            @Override
            public void onFollowed(boolean isFollow) {

            }
        });


        mRecyclerView.setLoadMoreListener(new AutoLoadRecyclerView.LoadMoreListener() {
            //滑动到底部时调用
            @Override
            public void loadMore() {
                if (mHasNext) {
                    dataSource.getShot(user.getId(), false, new ProfileCallback() {
                        @Override
                        public void onLoadProfileLoaded(List<Shot> shots, boolean hasNext) {
                            mHasNext = hasNext;
                            if (!hasNext) {
                                adapter.setShowFooter(false);
                            }
                            adapter.replaceData(shots);
                        }

                        @Override
                        public void onFollowChecked(boolean isFollowd) {

                        }

                        @Override
                        public void onFollowed(boolean isFollow) {

                        }
                    });
                } else {
                    Toast.makeText(ProfileActivity.this, "no more data", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_follow, menu);
        mMenu = menu;
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.menu_follow) {

            if (!AuthoUtil.isLogined()) {
                Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            } else {

                dataSource.changeFollowState(user.getId(), !mIsFollowed, new ProfileCallback() {

                    @Override
                    public void onLoadProfileLoaded(List<Shot> shots, boolean hasNext) {

                    }

                    @Override
                    public void onFollowChecked(boolean isFollowed) {
                    }

                    @Override
                    public void onFollowed(boolean requestFollow) {
                        if (requestFollow) {
                            Toast.makeText(ProfileActivity.this, "已关注", Toast.LENGTH_SHORT).show();
                            mIsFollowed = true;
                            item.setTitle("取消关注");
                        } else {
                            Toast.makeText(ProfileActivity.this, "已取消关注", Toast.LENGTH_SHORT).show();
                            mIsFollowed = false;
                            item.setTitle("关注");
                        }
                    }
                });

            }
            return true;
        }else {
            return false;
        }
    }

}
