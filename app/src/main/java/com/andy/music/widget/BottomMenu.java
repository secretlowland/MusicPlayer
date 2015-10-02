package com.andy.music.widget;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.andy.music.R;
import com.andy.music.activity.ActivityManager;
import com.andy.music.activity.SettingActivity;
import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicListManager;
import com.andy.music.service.MusicPlayService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 底部菜单
 * Created by zy on 2015/9/17.
 */
public class BottomMenu extends BottomDialog implements View.OnClickListener {

    /**
     * 上次保存的播放模式
     */
    private String playSchema;
    
    private FragmentActivity activity;

    private RelativeLayout menuLayout;
    private Button scanMusicBtn;
    private Button playSchemaBtn;
    private Button changeThemeBtn;
    private Button sleepTimeBtn;
    private Button settingBtn;
    private Button exitBtn;
    private View view;

    private boolean sleeping;
    
    public BottomMenu(Context activity) {
        super(activity, R.style.BottomDialog);
        initView();
    }

    public BottomMenu(Context activity, int theme) {
        super(activity, theme);
        initView();
    }

    private void initView() {
        activity = ActivityManager.getCurrentActivity();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.bottom_menu, null);
        scanMusicBtn = (Button) view.findViewById(R.id.btn_menu_item_scan_music);
        playSchemaBtn = (Button) view.findViewById(R.id.btn_menu_item_play_schema);
        changeThemeBtn = (Button) view.findViewById(R.id.btn_menu_item_change_theme);
        sleepTimeBtn = (Button) view.findViewById(R.id.btn_menu_item_sleep_time);
        settingBtn = (Button) view.findViewById(R.id.btn_menu_item_setting);
        exitBtn = (Button) view.findViewById(R.id.btn_menu_item_exit);

        scanMusicBtn.setOnClickListener(this);
        playSchemaBtn.setOnClickListener(this);
        changeThemeBtn.setOnClickListener(this);
        sleepTimeBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        initPlaySchema();
        setContentView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_menu_item_scan_music:
                // 扫描歌曲
                MusicListManager.scanMusic();
                Toast.makeText(activity, "扫描完成！", Toast.LENGTH_SHORT).show();
                this.dismiss();
                break;
            case R.id.btn_menu_item_play_schema:
                // 播放方式
                setPlaySchema(v);
                break;
            case R.id.btn_menu_item_change_theme:
                // 更换背景
                Toast.makeText(activity, "暂未实现！", Toast.LENGTH_SHORT).show();
                this.dismiss();
                break;
            case R.id.btn_menu_item_sleep_time:
                // 睡眠
                setSleepTime();
                this.dismiss();
                break;
            case R.id.btn_menu_item_setting:
                // 设置
                activity.startActivity(new Intent(activity, SettingActivity.class));
                this.dismiss();
                break;
            case R.id.btn_menu_item_exit:
                // 退出
                Toast.makeText(activity, "退出", Toast.LENGTH_SHORT).show();
                this.dismiss();
                System.exit(0);
                break;
            default:
                break;
        }
    }

    /**
     * 设置睡眠时间
     */
    private void setSleepTime() {

        if (!sleeping) {
            final TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    long exitTime = hourOfDay * 60 + minute;
                    exitAtTime(exitTime * 60000);
                    Toast.makeText(activity, "将在" + exitTime + "分钟后退出！", Toast.LENGTH_SHORT).show();
                    Log.d(TagConstants.TAG, "hour-->" + hourOfDay + "; min-->" + minute + "; total->" + exitTime);
                }
            };
            TimePickerDialog dialog = new TimePickerDialog(activity, listener, 0, 30, true);
            dialog.show();
            sleeping = true;
        } else {
            Toast.makeText(activity, "取消睡眠", Toast.LENGTH_SHORT).show();
            sleeping = false;
        }

    }

    /**
     * 初始化播放模式
     */
    private void initPlaySchema() {
        SharedPreferences pref = activity.getSharedPreferences("play_setting", Context.MODE_PRIVATE);
        int lastSchema = pref.getInt("play_schema", MusicPlayService.MUSIC_PLAY_SCHEMA_ORDER);
        String schema = null;
        switch (lastSchema) {
            case MusicPlayService.MUSIC_PLAY_SCHEMA_ORDER:
                schema = "顺序播放";
                break;
            case MusicPlayService.MUSIC_PLAY_SCHEMA_RANDOM:
                schema = "随机播放";
                break;
            case MusicPlayService.MUSIC_PLAY_SCHEMA_LIST_CIRCULATE:
                schema = "循环播放";
                break;
            case MusicPlayService.MUSIC_PLAY_SCHEMA_SINGLE_CIRCULATE:
                schema = "单曲循环";
                break;
        }
        playSchemaBtn.setText(schema);
    }


    /**
     * 在设定时间后退出
     *
     * @param time 距离退出的时间
     */
    private void exitAtTime(long time) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        };
        if (time > 0) {
            new Timer().schedule(task, time);
        } else {
            Log.d(TagConstants.TAG, "时间小于0");
        }
    }

    /**
     * 设置播放模式
     *
     * @param view 视图
     */
    private void setPlaySchema(View view) {
        SharedPreferences pref = getContext().getSharedPreferences("play_setting", Context.MODE_PRIVATE);
        int lastSchema = pref.getInt("play_schema", MusicPlayService.MUSIC_PLAY_SCHEMA_ORDER);
        int currentSchema = 0;

        switch (lastSchema) {
            case MusicPlayService.MUSIC_PLAY_SCHEMA_ORDER:
                playSchema = "随机播放";
                currentSchema = MusicPlayService.MUSIC_PLAY_SCHEMA_RANDOM;
                break;
            case MusicPlayService.MUSIC_PLAY_SCHEMA_RANDOM:
                playSchema = "循环播放";
                currentSchema = MusicPlayService.MUSIC_PLAY_SCHEMA_LIST_CIRCULATE;
                break;
            case MusicPlayService.MUSIC_PLAY_SCHEMA_LIST_CIRCULATE:
                playSchema = "单曲循环";
                currentSchema = MusicPlayService.MUSIC_PLAY_SCHEMA_SINGLE_CIRCULATE;
                break;
            case MusicPlayService.MUSIC_PLAY_SCHEMA_SINGLE_CIRCULATE:
                playSchema = "顺序播放";
                currentSchema = MusicPlayService.MUSIC_PLAY_SCHEMA_ORDER;
                break;
            default:
                break;
        }
        ((TextView) view.findViewById(R.id.btn_menu_item_play_schema)).setText(playSchema);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("play_schema", currentSchema);
        editor.apply();
    }
}
