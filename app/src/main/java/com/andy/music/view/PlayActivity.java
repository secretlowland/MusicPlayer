package com.andy.music.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.andy.music.R;
import com.andy.music.entity.Music;
import com.andy.music.entity.MusicList;
import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicChangedListener;
import com.andy.music.function.MusicPlayListener;
import com.andy.music.function.MusicPlayService;
import com.andy.music.utility.MusicLocator;

/**
 * 音乐播放界面
 * Created by Andy on 2014/11/16.
 */
public class PlayActivity extends Activity {

    private TextView musicName, musicSinger;
    private ImageButton playPre, playNext;
    private ToggleButton playToggle, addToFavor;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TagConstants.TAG, "MusicListActivity-->onServiceConnected()");
            MusicPlayService.MyBinder binder = (MusicPlayService.MyBinder) service;
            MusicPlayService musicPlayService = binder.getService();

            // 设置监听事件
            MusicPlayListener listener = new MusicPlayListener(musicPlayService);
            playToggle.setOnCheckedChangeListener(listener);
            playPre.setOnClickListener(listener);
            playNext.setOnClickListener(listener);

            // 判断是否正在播放
            if (musicPlayService.isPlaying()) {
                playToggle.setChecked(true);
            } else {
                playToggle.setChecked(false);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TagConstants.TAG, "MusicListActivity-->onServiceDisconnected()");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        initView();

        // 设置状态栏透明
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        // 绑定到 MusicPlayService 服务
        Intent intent = new Intent(this, MusicPlayService.class);
        this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        this.startService(intent);

        // 设置监听事件（监听歌曲位置是否改变）
        new MusicChangedListener().setOnMusicChangedListener(
                new MusicChangedListener.OnMusicChangedListener() {
                    @Override
                    public void onMusicChanged() {
                        Log.d(TagConstants.TAG, "位置改变了一次呢!!!");
                        updateView();
                    }
                }
        );
        addToFavor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MusicList list = MusicList.getInstance(MusicList.MUSIC_LIST_FAVORITE);
                Music music = MusicLocator.getLocatedMusic();
                if (isChecked) {
                    list.getList().add(music);
                } else {
                    list.getList().remove(music);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        Log.d(TagConstants.TAG, "PlayActivity-->onDestroy()");
        super.onDestroy();
        // 解绑服务
        unbindService(conn);
    }

    private void initView() {
        // 初始化成员变量
        musicName = (TextView) this.findViewById(R.id.tv_music_play_name);
        musicSinger = (TextView) this.findViewById(R.id.tv_music_play_singer);
        playPre = (ImageButton) this.findViewById(R.id.btn_music_play_pre);
        playNext = (ImageButton) this.findViewById(R.id.btn_music_play_next);
        playToggle = (ToggleButton) this.findViewById(R.id.tb_music_play_toggle);
        addToFavor = (ToggleButton) this.findViewById(R.id.tb_add_to_favorite_toggle);
    }

    public void updateView() {
        Music music = MusicLocator.getLocatedMusic();
        musicName.setText(music.getName());
        musicSinger.setText(music.getSinger());
    }

}
