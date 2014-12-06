package com.andy.music.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.andy.music.R;
import com.andy.music.entity.Music;
import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicPlayListener;
import com.andy.music.function.MusicPlayService;
import com.andy.music.utility.BroadCastHelper;
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
    private ImageButton playNext;

    private PlayStatusReceiver receiver;

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

            // 判断是否正在播放
            if (musicPlayService.isPlaying()) {
                playToggle.setChecked(true);
            } else {
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

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TagConstants.TAG, "Fragment-->onCreateView()");
        return inflater.inflate(R.layout.fragment_music_play_bar, container, false);
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
        playNext = (ImageButton) view.findViewById(R.id.btn_music_play_next);
        receiver = new PlayStatusReceiver();

        refreshPlayBar();

        // 设置监听事件
        MusicPlayListener listener = new MusicPlayListener();
        playToggle.setOnCheckedChangeListener(listener);
        playNext.setOnClickListener(listener);
        playBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TagConstants.TAG, "跳转到播放界面");
                startActivity(new Intent(getActivity(), PlayActivity.class));
                getActivity().overridePendingTransition(R.anim.top_to_bottom, R.anim.fade_out);   // 设置 activity动画
            }
        });

        // 动态注册广播接收(接收歌曲正在播放的广播)
        IntentFilter filter = new IntentFilter();
        filter.setPriority(0);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM);
        getActivity().registerReceiver(receiver, filter);

    }

    @Override
    public void onStart() {
        super.onStart();
        // 更新控制条视图
        refreshPlayBar();
    }

    @Override
    public void onDestroyView() {
        Log.d(TagConstants.TAG, "Fragment--->onDestroy()");
        super.onDestroyView();
        getActivity().unregisterReceiver(receiver);  // 注销广播
        getActivity().unbindService(conn);  // 解绑服务
    }


    /**
     * 更新播放控制条
     */
    public void refreshPlayBar() {
        Log.d(TagConstants.TAG, "refreshPlayBar()");
        Music music = MusicLocator.getLocatedMusic();
        if (music != null) {
            musicName.setText(music.getName());
            musicSinger.setText(music.getSinger());
        }
    }

    /**
     * 接收播放和播放下一首的广播
     */
    public class PlayStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean tag = false;
            if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY)) {
                tag = true;
            } else if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT)) {
                tag = true;
            } else if(action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM)) {
                tag = true;
            }

            if (tag) {
                if (!playToggle.isChecked()) {
                    playToggle.setChecked(true);
                }
                refreshPlayBar();  // 更新音乐播放控制条
            }
        }
    }


}
