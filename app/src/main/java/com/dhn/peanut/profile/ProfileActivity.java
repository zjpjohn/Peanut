package com.dhn.peanut.profile;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dhn.peanut.R;
import com.dhn.peanut.util.PeanutInfo;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.profile_name)
    TextView mTvName;
    @BindView(R.id.profile_pic)
    SimpleDraweeView mDraweeView;

    private SharedPreferences sp;
    private String avatar;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        avatar = sp.getString(PeanutInfo.PREFERENCE_USER_AVATAR, null);
        name = sp.getString(PeanutInfo.PREFERENCE_USER_NAME, null);

        if (avatar != null) {
            Uri uri = Uri.parse(avatar);
            mDraweeView.setImageURI(uri);
        }

        if (name != null) {
            mTvName.setText(name);
        }

    }

}
