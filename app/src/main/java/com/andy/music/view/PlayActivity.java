package com.andy.music.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.gesture.GestureOverlayView;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.andy.music.R;
import com.andy.music.entity.Music;
import com.andy.music.entity.MusicList;
import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicPlayListener;
import com.andy.music.function.MusicPlayService;
import com.andy.music.utility.BroadCastHelper;
import com.andy.music.utility.MusicLocator;

/**
 * 音乐播放界面
 * Created by Andy on 2014/11/16.
 */
public class PlayActivity extends Activity implements GestureDetector.OnGestureListener {

    private TextView musicName, musicSinger;
    private ImageButton playPre, playNext;
    private ToggleButton playToggle, addToFavor;
    private PlayStatusReceiver receiver;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TagConstants.TAG, "MusicListActivity-->onServiceConnected()");
            MusicPlayService.MyBinder binder = (MusicPlayService.MyBinder) service;
            MusicPlayService musicPlayService = binder.getService();

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

        // 初始化
        initView();

        // 设置状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }

        // 绑定到 MusicPlayService 服务
        Intent intent = new Intent(this, MusicPlayService.class);
        this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        this.startService(intent);

        // 设置监听事件（监听歌曲位置是否改变）
        MusicPlayListener listener = new MusicPlayListener();
        playToggle.setOnCheckedChangeListener(listener);
        playNext.setOnClickListener(listener);
        playPre.setOnClickListener(listener);

        // 动态注册广播接收(接收歌曲正在播放的广播)
        IntentFilter filter = new IntentFilter();
        filter.setPriority(0);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PAUSE);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_START);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS);
        this.registerReceiver(receiver, filter);

        addToFavor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MusicList list = MusicList.getInstance(MusicList.MUSIC_LIST_FAVORITE);
                Music music = MusicLocator.getLocatedMusic();
                if (isChecked) {
                    if (list.isMusicExist(music)) return;
                    list.add(music);
                    Toast.makeText(getApplicationContext(), "已添加到收藏！", Toast.LENGTH_SHORT).show();
                } else {
                    if (!list.isMusicExist(music)) return;
                    list.remove(music);
                    Toast.makeText(getApplicationContext(), "已取消收藏！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // 更新播放控制区域
        refreshPlayPanel();
    }

    @Override
    protected void onDestroy() {
        Log.d(TagConstants.TAG, "PlayActivity-->onDestroy()");
        super.onDestroy();
        // 解绑服务
        unbindService(conn);
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Toast.makeText(this, "haha", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void initView() {
        // 初始化成员变量
        musicName = (TextView) this.findViewById(R.id.tv_music_play_name);
        musicSinger = (TextView) this.findViewById(R.id.tv_music_play_singer);
        playPre = (ImageButton) this.findViewById(R.id.btn_music_play_pre);
        playNext = (ImageButton) this.findViewById(R.id.btn_music_play_next);
        playToggle = (ToggleButton) this.findViewById(R.id.tb_music_play_toggle);
        addToFavor = (ToggleButton) this.findViewById(R.id.tb_add_to_favorite_toggle);
        receiver = new PlayStatusReceiver();

    }

    public void refreshPlayPanel() {
        Music music = MusicLocator.getLocatedMusic();
        MusicList favouriteList = MusicList.getInstance(MusicList.MUSIC_LIST_FAVORITE);
        if (music==null) return;
        musicName.setText(music.getName());
        musicSinger.setText(music.getSinger());
        if (favouriteList!=null) {
            if (!favouriteList.isMusicExist(music)) {
                addToFavor.setChecked(false);
            } else {
                addToFavor.setChecked(true);
            }
        }
    }

    public class PlayStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean tag = false;
            if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT)) {
               tag = true;
            } else if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS)) {
                tag = true;
            }

            if (tag) {
                if (!playToggle.isChecked()) {
                    playToggle.setChecked(true);
                }
                refreshPlayPanel();
            }
        }
    }


}
