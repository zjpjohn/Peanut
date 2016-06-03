package com.dhn.peanut;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dhn.peanut.data.remote.RemoteShotData;
import com.dhn.peanut.shots.ShotFragment;
import com.dhn.peanut.shots.ShotPresenter;
import com.dhn.peanut.shots.ShotsContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawerlayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigationview)
    NavigationView mNavigationView;

    private ShotsContract.View shotFragment;
    private ShotsContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //ToolBar
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        //NavigationView
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_shots:
                                Toast.makeText(getApplicationContext(), "shots", Toast.LENGTH_SHORT);
                                break;
                            case R.id.menu_debuts:
                                Toast.makeText(getApplicationContext(), "shots", Toast.LENGTH_SHORT);
                                break;
                            case R.id.menu_gifs:
                                Toast.makeText(getApplicationContext(), "shots", Toast.LENGTH_SHORT);
                                break;
                            default:
                                break;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        shotFragment = ShotFragment.newInstance();
        mPresenter = new ShotPresenter(shotFragment, RemoteShotData.getInstance());
        getSupportFragmentManager().beginTransaction().replace(R.id.content, (Fragment) shotFragment).commit();

    }


}
