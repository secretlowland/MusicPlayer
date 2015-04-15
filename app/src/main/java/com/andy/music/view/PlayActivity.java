package com.andy.music.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.gesture.GestureOverlayView;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.andy.music.R;
import com.andy.music.entity.Music;
import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicListManager;
import com.andy.music.function.MusicPlayService;
import com.andy.music.function.MusicProgressManager;
import com.andy.music.listener.MusicPlayListener;
import com.andy.music.utility.BroadCastHelper;
import com.andy.music.utility.MusicLocator;
import com.andy.music.utility.TimeHelper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 音乐播放界面
 * Created by Andy on 2014/11/16.
 */
public class PlayActivity extends Activity implements View.OnTouchListener, GestureDetector.OnGestureListener {

    private LinearLayout layout;
    private TextView musicName, musicSinger, currentTime, totalTime;
    private ImageButton playPre, playNext;
    private ToggleButton playToggle, addToFavor;
    private SeekBar seekBar;
    private PlayStatusReceiver receiver;

    private PlayProgressThread playProgressThread;
    private PlayProgressHandler playProgressHandler;

    private GestureDetector detector;


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

        // 设置监听事件（监听歌曲位置是否改变）
        MusicPlayListener listener = new MusicPlayListener();
        playToggle.setOnCheckedChangeListener(listener);
        playNext.setOnClickListener(listener);
        playPre.setOnClickListener(listener);
        layout.setOnTouchListener(this);
        layout.setLongClickable(true);
        detector.setIsLongpressEnabled(true);

        // 动态注册广播接收(接收歌曲正在播放的广播)
        registerService();

        addToFavor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MusicListManager list = MusicListManager.getInstance(MusicListManager.MUSIC_LIST_FAVORITE);
                Music music = MusicLocator.getCurrentMusic();
                Log.d(TagConstants.TAG, "添加到最爱");
                if (isChecked && music!=null) {
                    Log.d(TagConstants.TAG, "添加到最爱");
                    if (list.isMusicExist(music)) return;
                    Log.d(TagConstants.TAG, "添加到最爱, meifanhui");
                    list.add(music);
                } else {
                    if (!list.isMusicExist(music)) return;
                    list.remove(music);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // 更新播放控制面板
        refreshPlayPanel();

        // 开始更新音乐播放进度的线程
        playProgressThread.start(1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 停止更新音乐播放进度的线程
        playProgressThread.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    /**
     * 初始化
     */
    private void initView() {
        // 初始化成员变量
        musicName = (TextView) this.findViewById(R.id.tv_music_play_name);
        musicSinger = (TextView) this.findViewById(R.id.tv_music_play_singer);
        currentTime = (TextView) this.findViewById(R.id.tv_music_current_time);
        totalTime = (TextView) this.findViewById(R.id.tv_music_total_time);
        playPre = (ImageButton) this.findViewById(R.id.btn_music_play_pre);
        playNext = (ImageButton) this.findViewById(R.id.btn_music_play_next);
        playToggle = (ToggleButton) this.findViewById(R.id.tb_music_play_toggle);
        addToFavor = (ToggleButton) this.findViewById(R.id.tb_add_to_favorite_toggle);
        seekBar = (SeekBar) this.findViewById(R.id.sb_music_play_progress);
        layout = (LinearLayout) this.findViewById(R.id.ll_up);

        detector = new GestureDetector(this);
        receiver = new PlayStatusReceiver();
        playProgressThread = new PlayProgressThread();
        playProgressHandler = new PlayProgressHandler();

    }

    /**
     * 动态注册广播
     */
    private void registerService() {
        IntentFilter filter = new IntentFilter();
        filter.setPriority(0);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PAUSE);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_START);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS);
        this.registerReceiver(receiver, filter);
    }

    /**
     * 刷新播放面板
     */
    private void refreshPlayPanel() {

        Music music = MusicLocator.getCurrentMusic();
        MusicListManager favouriteList = MusicListManager.getInstance(MusicListManager.MUSIC_LIST_FAVORITE);

        if (MusicPlayService.isPlaying()) {
            playToggle.setChecked(true);
        } else {
            playToggle.setChecked(false);
        }
        if (music == null) return;
        musicName.setText(music.getName());
        musicSinger.setText(music.getSinger());
        seekBar.setMax(music.getDuration());
        seekBar.setProgress(MusicProgressManager.getProgress());
        currentTime.setText(TimeHelper.timeFormat(MusicProgressManager.getProgress()));
        totalTime.setText(TimeHelper.timeFormat(music.getDuration()));

        if (favouriteList != null) {
            if (!favouriteList.isMusicExist(music)) {
                addToFavor.setChecked(false);
            } else {
                addToFavor.setChecked(true);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return detector.onTouchEvent(event);
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
//        Log.d(TagConstants.TAG, "onFling()-->"+velocityX);
//        if (velocityY>0) {
//            Intent intent = new Intent(PlayActivity.this, MainActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.fade_in, R.anim.top_to_bottom);
//        }
        return true;
    }

    public class PlayStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY) ||
                    action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT) ||
                    action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS) ||
                    action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM)) {
                refreshPlayPanel();
            }
        }
    }

    public class PlayProgressHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 此处更新 UI（当前播放时间）
            if (msg.what == 0) {
                int progress = MusicProgressManager.getProgress();
                seekBar.setProgress(progress);
                currentTime.setText(TimeHelper.timeFormat(progress));
            }
        }
    }

    public class PlayProgressThread {

        Timer timer;
        TimerTask task;

        public void start(int interval) {

            if (task != null) task = null;
            if (timer != null) {
                timer.cancel();
                timer.purge();
                timer = null;
            }

            timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    // 发送一次更新播放时间的消息
                    playProgressHandler.sendEmptyMessage(0);
                }
            };

            timer.schedule(task, 0, interval);
        }

        public void stop() {
            if (task != null) task = null;
            if (timer != null) {
                timer.cancel();
                timer.purge();
                timer = null;
            }
        }
    }

}
