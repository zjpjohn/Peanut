package com.dhn.peanut.setting;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dhn.peanut.R;


/**
 * Created by DHN on 2016/3/12.
 */
public class SettingFragment extends PreferenceFragment {

    public static final String ABOUT_APP = "about_app";
    public static final String APP_VERSION = "app_version";

    private Preference aboutMe;
    private Preference version;
    private Preference.OnPreferenceClickListener onPreferenceClickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //添加选项
        addPreferencesFromResource(R.xml.preferences);

        aboutMe = findPreference(ABOUT_APP);
        version = findPreference(APP_VERSION);

        String versionName = "";
        versionName = getVersionName();
        version.setSummary(versionName);

        onPreferenceClickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                if (preference.getKey().equals(ABOUT_APP)) {
                    new MaterialDialog.Builder(getActivity())
                            .title("Dribble")
                            .content("by 戴浩男")
                            .positiveText("github")
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/gatsbydhn/Peanut")));
                                    dialog.dismiss();
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    super.onNegative(dialog);
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.cnblogs.com/gatsbydhn/")));
                                    dialog.dismiss();
                                }
                            })
                            .negativeText("blog")
                            .show();

                }

                return true;
            }
        };


        aboutMe.setOnPreferenceClickListener(onPreferenceClickListener);
    }

    private String getVersionName()
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getActivity().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getActivity().getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

}
