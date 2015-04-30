package com.andy.music.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.andy.music.R;
import com.andy.music.fragment.TopBarFragment;

/**
 * 设置界面
 * Created by Andy on 2014/12/12.
 */
public class SettingActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.ll_top_bar_container, new TopBarFragment(), "topBar");
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TopBarFragment topBar = (TopBarFragment)getSupportFragmentManager().findFragmentByTag("topBar");
        if (topBar!=null) {
            topBar.setCustomTitle("设置");
            topBar.hideSearchIcon();
        }
    }
}
