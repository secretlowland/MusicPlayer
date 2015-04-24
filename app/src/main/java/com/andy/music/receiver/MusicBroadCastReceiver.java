package com.andy.music.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.andy.music.function.MusicProgressManager;
import com.andy.music.util.BroadCastHelper;

/**
 * 广播接收器，用于接收音乐播放发出的各种广播
 * 此广播注册在 AndroidManifest 中，全局有效
 * Created by Andy on 2014/12/21.
 */
public class MusicBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case BroadCastHelper.ACTION_MUSIC_START:
                MusicProgressManager.start();
                break;
            case BroadCastHelper.ACTION_MUSIC_PAUSE:
                MusicProgressManager.stop();
                break;
            case BroadCastHelper.ACTION_MUSIC_PLAY:
                MusicProgressManager.init();
                MusicProgressManager.start();
                break;
            case BroadCastHelper.ACTION_MUSIC_PLAY_NEXT:
                MusicProgressManager.init();
                MusicProgressManager.start();
                break;
            case BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS:
                MusicProgressManager.init();
                MusicProgressManager.start();
                break;
            case BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM:
                MusicProgressManager.init();
                MusicProgressManager.start();
                break;
            default: break;
        }
    }
}
