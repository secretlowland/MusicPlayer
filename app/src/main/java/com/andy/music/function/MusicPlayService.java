package com.andy.music.function;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.andy.music.entity.Music;
import com.andy.music.entity.MusicList;
import com.andy.music.entity.TagConstants;
import com.andy.music.utility.MusicLocator;

import java.io.IOException;

/**
 * 音乐播放服务
 * 注意 : 在OnBind（）方法中需返回一个IBinder实例，不然onServiceConnected方法不会调用。
 * Created by Andy on 2014/11/20.
 */
public class MusicPlayService extends Service implements MediaPlayer.OnCompletionListener {

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

    /**
     * 上次播放歌曲的位置信息
     */
    private Bundle musicLocation;

    public MusicPlayService() {
        mediaPlayer = new MediaPlayer();
    }


    @Override
    public void onCreate() {
        Log.d(TagConstants.TAG, "MusicService-->onCreate()");
        // 创建 MediaPlayer 对象
        mediaPlayer = new MediaPlayer();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TagConstants.TAG, "MusicService-->onStartCommand()");
        musicList = MusicList.getInstance(MusicList.MUSIC_LIST_LOCAL);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TagConstants.TAG, "MusicService-->onDestroy()");
        mediaPlayer.release();
        super.onDestroy();
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
    public void onRebind(Intent intent) {
        Log.d(TagConstants.TAG, "MusicService-->onRebind()");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TagConstants.TAG, "MusicService-->onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // 歌曲播放完成
        Log.d(TagConstants.TAG, "歌曲播放完了呢！");
        // 自动播放下一首
        playNext();
    }

    public void prepare() {
        music = MusicLocator.getLocatedMusic();
        if (music==null) {
            return;
        }

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(music.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void play() {
        Log.d(TagConstants.TAG, "播放当前歌曲");
        prepare();
        start();
    }

    public void playNext() {

        Log.d(TagConstants.TAG, "播放下一首");

        if (MusicLocator.toNext()) {
            // 准备并播放
            prepare();
            start();
        } else {
            Log.d(TagConstants.TAG, "已经到 了最后一首");
        }

    }

    public void playPrevious() {

        Log.d(TagConstants.TAG, "播放上一首");

        if (MusicLocator.toPrevious()) {
            // 准备并播放
            prepare();
            start();
        } else {
            Log.d(TagConstants.TAG, "已经到 了第一首");
        }

    }

    public void start() {
        Log.d(TagConstants.TAG, "play");
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        Log.d(TagConstants.TAG, "pause");
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    public class MyBinder extends Binder {

        public MusicPlayService getService() {
            return MusicPlayService.this;
        }

    }

}
