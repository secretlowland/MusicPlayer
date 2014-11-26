package com.andy.music.widget;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.andy.music.R;
import com.andy.music.entity.Music;
import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicChangedListener;
import com.andy.music.function.MusicPlayListener;
import com.andy.music.function.MusicPlayService;
import com.andy.music.utility.MusicLocator;
import com.andy.music.view.PlayActivity;

/**
 * 音乐播放控制条
 * Created by Andy on 2014/11/20.
 */
public class PlayBarFragment extends Fragment {

    private LinearLayout playBar;
    private TextView musicName;
    private TextView musicSinger;
    private ToggleButton playToggle;
    private Button playNext;

    private MusicPlayService musicPlayService;

    /**
     * ServiceConnection 对象，用于 Activity 与服务进行连接，连接成功后返回一个服务类实例，通过该实例可以
     * 访问服务实例的方法
     */
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlayService.MyBinder binder = (MusicPlayService.MyBinder) service;
            musicPlayService = binder.getService();

            // 设置监听事件
            MusicPlayListener listener = new MusicPlayListener(musicPlayService);
            playNext.setOnClickListener(listener);
            playToggle.setOnCheckedChangeListener(listener);

            // 判断是否正在播放
            if (musicPlayService.isPlaying()) {
                Log.d(TagConstants.TAG, "正在播放");
                playToggle.setChecked(true);
            } else {
                Log.d(TagConstants.TAG, "暂停状态");
                playToggle.setChecked(false);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TagConstants.TAG, "Fragment-->onCreate()");

        //  绑定到 MusicPlayService 服务
        Intent intent = new Intent(getActivity(), MusicPlayService.class);
        getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        super.onCreate(savedInstanceState);


        // TODO 监听事件
        new MusicChangedListener().setOnMusicChangedListener(
                new MusicChangedListener.OnMusicChangedListener() {
                    @Override
                    public void onMusicChanged() {
                        updateView();
                    }
                }
        );


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TagConstants.TAG, "Fragment-->onCreateView()");
        return inflater.inflate(R.layout.fragment_music_control_bar, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TagConstants.TAG, "Fragment-->onViewCreated()");
        super.onViewCreated(view, savedInstanceState);

        // 初始化成员变量
        playBar = (LinearLayout) view.findViewById(R.id.ll_music_control_bar);
        musicName = (TextView) view.findViewById(R.id.tv_music_play_name);
        musicSinger = (TextView) view.findViewById(R.id.tv_music_play_singer);
        playToggle = (ToggleButton) view.findViewById(R.id.tb_music_play_toggle);
        playNext = (Button) view.findViewById(R.id.btn_music_play_next);

        // 设置监听事件
        playBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TagConstants.TAG, "跳转到播放界面");
                startActivity(new Intent(getActivity(), PlayActivity.class));
            }
        });

        // 初始化视图
        updateView();

    }

    @Override
    public void onDestroyView() {
        Log.d(TagConstants.TAG, "Fragment--->onDestroy()");
        super.onDestroyView();
        getActivity().unbindService(conn);  // 解绑服务
    }


    public void updateView() {
        Music music = MusicLocator.getLocatedMusic();
        musicName.setText(music.getName());
        musicSinger.setText(music.getSinger());
    }

}
