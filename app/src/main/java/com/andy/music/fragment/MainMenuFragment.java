package com.andy.music.fragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.andy.music.R;
import com.andy.music.data.CursorAdapter;
import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicListManager;
import com.andy.music.function.MusicPlayService;
import com.andy.music.view.SettingActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 主菜单模块
 * Created by Andy on 2014/12/16.
 */
public class MainMenuFragment extends DialogFragment implements View.OnClickListener {

    /**
     * 上次保存的播放模式
     */
    private String playSchema;

    private RelativeLayout menuLayout;
    private Button scanMusicBtn;
    private Button playSchemaBtn;
    private Button changeThemeBtn;
    private Button sleepTimeBtn;
    private Button settingBtn;
    private Button exitBtn;

    private boolean sleeping;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuLayout = (RelativeLayout) view.findViewById(R.id.rl_menu);
        scanMusicBtn = (Button) view.findViewById(R.id.btn_menu_item_scan_music);
        playSchemaBtn = (Button) view.findViewById(R.id.btn_menu_item_play_schema);
        changeThemeBtn = (Button) view.findViewById(R.id.btn_menu_item_change_theme);
        sleepTimeBtn = (Button) view.findViewById(R.id.btn_menu_item_sleep_time);
        settingBtn = (Button) view.findViewById(R.id.btn_menu_item_setting);
        exitBtn = (Button) view.findViewById(R.id.btn_menu_item_exit);

        menuLayout.setOnClickListener(this);
        scanMusicBtn.setOnClickListener(this);
        playSchemaBtn.setOnClickListener(this);
        changeThemeBtn.setOnClickListener(this);
        sleepTimeBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);

        initPlaySchema();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_menu_item_scan_music:
                // 扫描歌曲
                scanMusic();
                getActivity().onBackPressed();
                break;
            case R.id.btn_menu_item_play_schema:
                // 播放方式
                setPlaySchema(v);
                break;
            case R.id.btn_menu_item_change_theme:
                // 更换背景
                Toast.makeText(getActivity(), "更换背景", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
                break;
            case R.id.btn_menu_item_sleep_time:
                // 睡眠
                setSleepTime();
                break;
            case R.id.btn_menu_item_setting:
                // 设置
                Toast.makeText(getActivity(), "设置", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), SettingActivity.class));
                getActivity().onBackPressed();
                break;
            case R.id.btn_menu_item_exit:
                // 退出
                Toast.makeText(getActivity(), "退出", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
                System.exit(0);
                break;
            case R.id.rl_menu:
                getActivity().onBackPressed();
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
                    exitAtTime(exitTime*60000);
                    Toast.makeText(getActivity(), "将在"+exitTime+"分钟后退出！", Toast.LENGTH_SHORT).show();
                    Log.d(TagConstants.TAG, "hour-->"+hourOfDay+"; min-->"+minute+"; total->"+exitTime);
                }
            };
            TimePickerDialog dialog = new TimePickerDialog(getActivity(), listener, 0, 30, true );
            dialog.show();
            sleeping = true;
        } else {
            Toast.makeText(getActivity(), "取消睡眠", Toast.LENGTH_SHORT).show();
            sleeping = false;
        }

    }
    /**
     * 初始化播放模式
     */
    private void initPlaySchema() {
        SharedPreferences pref = getActivity().getSharedPreferences("play_setting", Context.MODE_PRIVATE);
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
     * 扫描音乐
     */
    private void scanMusic() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 由于在数据库中建立列表的的时间比较长，故单独放在一个线程中
                // 获得查询游标
                Cursor searchCursor = CursorAdapter.get(null,null);

                // 将游标中的数据存到数据库
                MusicListManager localMusic = MusicListManager.getInstance(MusicListManager.MUSIC_LIST_LOCAL);
                if (localMusic!=null) {
                    localMusic.setList(searchCursor);
                }

                // 关闭 Cursor，释放资源
                searchCursor.close();
            }
        }).start();
        Toast.makeText(getActivity(), "扫描完成！", Toast.LENGTH_SHORT).show();
    }
    /**
     * 在设定时间后退出
     * @param time  距离退出的时间
     */
    private void exitAtTime(long time) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        };
        if (time>0) {
            new Timer().schedule(task, time);
        } else {
            Log.d(TagConstants.TAG, "时间小于0");
        }
    }

    /**
     * 设置播放模式
     * @param view 视图
     */
    private void setPlaySchema(View view) {
        SharedPreferences pref = getActivity().getSharedPreferences("play_setting", Context.MODE_PRIVATE);
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
            default: break;
        }
        ((TextView) view.findViewById(R.id.btn_menu_item_play_schema)).setText(playSchema);
        SharedPreferences.Editor  editor = pref.edit();
        editor.putInt("play_schema", currentSchema);
        editor.apply();
    }
}
