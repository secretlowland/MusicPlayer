package com.andy.music.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.andy.music.R;
import com.andy.music.entity.TagConstants;
import com.andy.music.fragment.TopBarFragment;

/**
 * 设置界面
 * Created by Andy on 2014/12/12.
 */
public class SettingActivity extends FragmentActivity implements CompoundButton.OnCheckedChangeListener {

    private ToggleButton shaking, autoPause, autoStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();

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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences pref = getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        switch (buttonView.getId()) {
            case R.id.tb_setting_shaking:
                Log.d(TagConstants.TAG, "checked-->"+isChecked);
                editor.putBoolean("shaking", isChecked).commit();
                break;
            case R.id.tb_setting_auto_pause:
                editor.putBoolean("auto_pause", isChecked).commit();
                break;
            case R.id.tb_setting_auto_start:
                editor.putBoolean("auto_start", isChecked).commit();
                break;
            default: break;
        }
    }

    private void init() {
        shaking = (ToggleButton)this.findViewById(R.id.tb_setting_shaking);
        autoPause = (ToggleButton)this.findViewById(R.id.tb_setting_auto_pause);
        autoStart = (ToggleButton)this.findViewById(R.id.tb_setting_auto_start);

        shaking.setOnCheckedChangeListener(this);
        autoPause.setOnCheckedChangeListener(this);
        autoStart.setOnCheckedChangeListener(this);

        SharedPreferences pref = getSharedPreferences("settings", Context.MODE_PRIVATE);
        shaking.setChecked(pref.getBoolean("shaking", false));
        autoPause.setChecked(pref.getBoolean("auto_pause", false));
        autoStart.setChecked(pref.getBoolean("auto_start", false));
    }
}
