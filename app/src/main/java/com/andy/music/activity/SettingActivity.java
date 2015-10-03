package com.andy.music.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.andy.music.R;
import com.andy.music.entity.TagConstants;
import com.andy.music.fragment.TopBarFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;

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

        // 设置沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);   //透明导航栏
            //  改变状态栏颜色
            //  创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            //  给状态栏设置颜色
            tintManager.setStatusBarTintColor(0xee729939);
        }

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
