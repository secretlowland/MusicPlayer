package com.andy.music.function;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.andy.music.entity.Music;
import com.andy.music.entity.TagConstants;
import com.andy.music.utility.BroadCastHelper;
import com.andy.music.utility.MusicLocator;

import java.io.IOException;

/**
 * 音乐播放服务
 * 注意 : 在OnBind（）方法中需返回一个IBinder实例，不然onServiceConnected方法不会调用。
 * Created by Andy on 2014/11/20.
 */
public class MusicPlayService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {

    /**
     * 播放模式
     */
    public static final int MUSIC_PLAY_SCHEMA_ORDER = 0;    // 顺序播放
    public static final int MUSIC_PLAY_SCHEMA_RANDOM = 1;    // 随机播放
    public static final int MUSIC_PLAY_SCHEMA_LIST_CIRCULATE = 2;    // 列表循环
    public static final int MUSIC_PLAY_SCHEMA_SINGLE_CIRCULATE = 3;    // 单曲循环
    private static boolean isPlaying = false;

    private Music music;
    private MediaPlayer mediaPlayer;


    @Override
    public void onCreate() {
        // 创建 MediaPlayer 对象
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        // 注册广播接收器
        registerReceiver();

        // 设置歌曲播放完成的监听事件
        mediaPlayer.setOnCompletionListener(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String playAction = null;
        if (intent!=null) {
            playAction = intent.getStringExtra("play_action");
        }
        if (playAction!=null) {
            Log.d(TagConstants.TAG, "next-->"+playAction);
            switch (playAction) {
                case "next":
                    MusicLocator.toNext();
                    BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
                    break;
                case "pause":
                    BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PAUSE);
                    break;
                case "start":
                    BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_START);
                    break;
                default:break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public void onDestroy() {
        Log.d(TagConstants.TAG, "MusicService-->onDestroy()");
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // 歌曲播放完成，自动播放下一首
        MusicLocator.toNext();
        BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
    }

    /**
     * 为歌曲的播放做准备
     */
    public void prepare() {
        music = MusicLocator.getCurrentMusic();
        if (music == null) {
            Log.d(TagConstants.TAG, "歌曲为空");
            return;
        }

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(music.getPath());
            mediaPlayer.prepare();
            addToRecent(music);   // 添加最近播放
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始播放（从歌曲开头开始）
     */
    public void play() {
        prepare();
        start();
    }

    /**
     * 开始播放（从上次停止的地方开始）
     */
    public void start() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    /**
     * 判断是否正在播放
     * @return 是否正在播放
     */
    public  static boolean isPlaying() {
        return isPlaying;
    }

    public static void setIsPlaying(boolean playing) {
        isPlaying = playing;
    }

    /**
     * 动态注册广播接收器
     */
    private void registerReceiver() {

        IntentFilter filter = new IntentFilter();
        filter.setPriority(1000);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_START);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PAUSE);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM);
        Receiver receiver = new Receiver();
        registerReceiver(receiver, filter);

    }

    /**
     * 将播放的歌曲添加到最近播放
     * @param music 正在播放的歌曲
     */
    private void addToRecent(Music music) {
        MusicListManager recentList = MusicListManager.getInstance(MusicListManager.MUSIC_LIST_RECENT);   // 获取最近列表
        if (recentList != null && !recentList.isMusicExist(music)) {
            recentList.add(music);
        }
    }


    /**
     * 接收与音乐播放有关的广播，然后做出相应操作
     */
    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            switch (action) {
                case BroadCastHelper.ACTION_MUSIC_START:
                    // 开始播放音乐的广播（从断点开始）
                    if (MusicLocator.getCurrentMusic()!=null) {
                        start();
                        setIsPlaying(true);
                    } else {
                        BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PAUSE);
                        setIsPlaying(false);
                    }

                    break;
                case BroadCastHelper.ACTION_MUSIC_PAUSE:
                    // 暂停音乐的广播
                    pause();
                    setIsPlaying(false);
                    break;
                case BroadCastHelper.ACTION_MUSIC_PLAY:
                    // 播放音乐的广播（从头开始）
                    play();
                    setIsPlaying(true);
                    break;
                case BroadCastHelper.ACTION_MUSIC_PLAY_NEXT:
                    // 播放下一首的广播
                    play();
                    setIsPlaying(true);
                    break;
                case BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS:
                    // 播放上一首的广播
                    play();
                    setIsPlaying(true);
                    break;
                case BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM:
                    // 随机播放的广播
                    play();
                    setIsPlaying(true);
                default: break;
            }

        }
    }

}
