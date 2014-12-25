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
import android.widget.Toast;

import com.andy.music.entity.Music;
import com.andy.music.entity.MusicList;
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
        MediaPlayer.OnPreparedListener{

    /**
     * 播放模式
     */
    public  static final int MUSIC_PLAY_SCHEMA_ORDER = 0;    // 顺序播放
    public static final int MUSIC_PLAY_SCHEMA_RANDOM = 1;    // 随机播放
    public static final int MUSIC_PLAY_SCHEMA_LIST_CIRCULATE = 2;    // 列表循环
    public static final int MUSIC_PLAY_SCHEMA_SINGLE_CIRCULATE = 3;    // 单曲循环

    /**
     * 要播放的音乐
     */
    private Music music;

    /**
     * 播放列表
     */
    private MusicList musicList;

    private MediaPlayer mediaPlayer;

    private MyBinder myBinder = new MyBinder();

    private Receiver receiver;

    public MusicPlayService() {
        mediaPlayer = new MediaPlayer();
    }


    @Override
    public void onCreate() {
        Log.d(TagConstants.TAG, "MusicService-->onCreate()");
        // 创建 MediaPlayer 对象
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        // 注册广播接收器
        registerReceiver();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TagConstants.TAG, "MusicService-->onStartCommand()");
        musicList = MusicList.getInstance(MusicList.MUSIC_LIST_LOCAL);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public void onDestroy() {
        Log.d(TagConstants.TAG, "MusicService-->onDestroy()");
        if (mediaPlayer!=null) {
            mediaPlayer.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TagConstants.TAG, "MusicService-->onBind()");

        // 做好播放的准备
        prepare();

        // 设置歌曲播放完成的监听事件
        mediaPlayer.setOnCompletionListener(this);
        return myBinder;

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // 歌曲播放完成，自动播放下一首
        Log.d(TagConstants.TAG, "歌曲播放完了~~~~~~~");
        SharedPreferences pref = getSharedPreferences("play_setting", Context.MODE_PRIVATE);
        int playSchema = pref.getInt("play_schema", MusicPlayService.MUSIC_PLAY_SCHEMA_ORDER);
        switch (playSchema) {
            case MusicPlayService.MUSIC_PLAY_SCHEMA_ORDER:
                //  顺序播放
                Log.d(TagConstants.TAG, "顺序播放");
                BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
                break;
            case MusicPlayService.MUSIC_PLAY_SCHEMA_RANDOM:
                // 随机播放
                Log.d(TagConstants.TAG, "随机播放");
                BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM);
                break;
            case MusicPlayService.MUSIC_PLAY_SCHEMA_LIST_CIRCULATE:
                //  TODO 循环播放
                Log.d(TagConstants.TAG, "循环播放");
                if (playNext()) return;

                break;
            case MusicPlayService.MUSIC_PLAY_SCHEMA_SINGLE_CIRCULATE:
                // 单曲循环
                Log.d(TagConstants.TAG, "单曲循环");
                BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY);
                break;
            default: break;
        }
    }

    /**
     * 为歌曲的播放做准备
     */
    public void prepare() {
        music = MusicLocator.getLocatedMusic();
        if (music==null) {
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
     * 播放下一首
     */
    public boolean playNext() {

        if (MusicLocator.toNext()) {
            // 准备并播放
            prepare();
            start();
        } else {
            return false;
        }
        return true;
    }

    /**
     * 播放上一首
     */
    public boolean playPrevious() {

        if (MusicLocator.toPrevious()) {
            // 准备并播放
            prepare();
            start();
        } else {
            return false;
        }
        return true;
    }

    /**
     * 随机播放
     */
    public boolean playRandom() {
        if (MusicLocator.toRandom()) {
            // 准备并播放
            prepare();
            start();
        } else {
            return false;
        }
        return true;
    }

    /**
     * 开始播放（从上次停止的地方开始）
     */
    public void start() {
        Log.d(TagConstants.TAG, "play");
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        Log.d(TagConstants.TAG, "pause");
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    /**
     * 判断是否正遭播放
     * @return 是否正在播放
     */
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     *  动态注册广播接收器
     */
    private void registerReceiver() {

        IntentFilter filter = new IntentFilter();
        filter.setPriority(1000);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_START);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PAUSE);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM);
        receiver = new Receiver();
        registerReceiver(receiver, filter);
    }

    private void addToRecent(Music music) {
        MusicList recentList = MusicList.getInstance(MusicList.MUSIC_LIST_RECENT);   // 获取最近列表
        if (recentList!=null) {
            recentList.add(music);
        }
    }

    public class MyBinder extends Binder {

        public MusicPlayService getService() {
            return MusicPlayService.this;
        }

    }

    /**
     * 接收与音乐播放有关的广播，然后做出相应操作
     */
    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(BroadCastHelper.ACTION_MUSIC_START)) {   // 开始播放音乐的广播（从断点开始）
                start();
            } else if (action.equals(BroadCastHelper.ACTION_MUSIC_PAUSE)) {  // 暂停音乐的广播
                pause();
            } else if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY)) {  // 播放音乐的广播（从头开始）
                play();
            } else if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS)) {  // 播放上一首的广播
                playPrevious();
            } else if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT)) {  // 播放下一首的广播
                SharedPreferences pref = getSharedPreferences("play_setting", Context.MODE_PRIVATE);
                // 读取播放模式
                int playSchema = pref.getInt("play_schema", MusicPlayService.MUSIC_PLAY_SCHEMA_ORDER);
                if (playSchema!=MUSIC_PLAY_SCHEMA_RANDOM) {
                    playNext();
                } else {
                    playRandom();
                }

            } else if( action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM)) {  // 随机播放的广播
                playRandom();
            }

        }
    }

}
