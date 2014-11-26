package com.andy.music;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicPlayService;

/**
 * Created by Andy on 2014/11/23.
 */
public class MServiceConnection implements ServiceConnection {

    private MusicPlayService musicPlayService;

    public MServiceConnection() {
        Log.d(TagConstants.TAG, "创建一个MServiceConnection对象");
    }

    public MusicPlayService getService() {
        return musicPlayService;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TagConstants.TAG, "这是自定义类的MusicListActivity-->onServiceConnected()");
        MusicPlayService.MyBinder binder = (MusicPlayService.MyBinder)service;
        musicPlayService = binder.getService();
        Log.d(TagConstants.TAG, "musicService-->"+musicPlayService);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TagConstants.TAG, "MusicListActivity-->onServiceDisconnected()");
    }
}
