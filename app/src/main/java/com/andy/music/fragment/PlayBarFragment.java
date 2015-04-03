package com.andy.music.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import com.andy.music.function.MusicPlayService;
import com.andy.music.listener.MusicPlayListener;
import com.andy.music.utility.BroadCastHelper;
import com.andy.music.utility.MusicLocator;
import com.andy.music.view.PlayActivity;

/**
 * 音乐播放控制条
 * Created by Andy on 2014/11/20.
 */
public class PlayBarFragment extends android.support.v4.app.Fragment {

    private LinearLayout playBar;
    private TextView musicName;
    private TextView musicSinger;
    private ToggleButton playToggle;
    private ImageButton playNext, mainMenu;

    private PlayStatusReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TagConstants.TAG, "Fragment-->onCreate()");

        //  绑定到 MusicPlayService 服务
        Intent intent = new Intent(getActivity(), MusicPlayService.class);
        getActivity().startService(intent);
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
        mainMenu = (ImageButton) view.findViewById(R.id.btn_main_menu);
        receiver = new PlayStatusReceiver();

        refreshPlayBar();

        // 设置监听事件
        MusicPlayListener listener = new MusicPlayListener();
        playToggle.setOnCheckedChangeListener(listener);
        playNext.setOnClickListener(listener);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 加载菜单模块
                MainMenuFragment fragment = new MainMenuFragment();
                fragment.setCancelable(true);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.setCustomAnimations(R.anim.bottom_to_top, R.anim.fade_out, R.anim.fade_out, R.anim.fade_out);
                transaction.setCustomAnimations(R.anim.bottom_to_top, 0, 0, R.anim.fade_out);
                transaction.add(R.id.frag_container_main_menu, fragment);
                transaction.addToBackStack(null).commit();

            }
        });
        playBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PlayActivity.class));
                getActivity().overridePendingTransition(R.anim.bottom_to_top, R.anim.fade_out);   // 设置 activity动画
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
    }


    /**
     * 更新播放控制条
     */
    public void refreshPlayBar() {
        Music music = MusicLocator.getCurrentMusic();
        if (music != null) {
            musicName.setText(music.getName());
            musicSinger.setText(music.getSinger());
        }
        if (MusicPlayService.isPlaying()) {
            playToggle.setChecked(true);
        } else {
            playToggle.setChecked(false);
        }

    }

    /**
     * 接收播放和播放下一首的广播，更新播放条
     */
    public class PlayStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY) ||
                    action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT) ||
                    action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS) ||
                    action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM)) {
                refreshPlayBar();  // 更新音乐播放控制条
            }

        }
    }


}
