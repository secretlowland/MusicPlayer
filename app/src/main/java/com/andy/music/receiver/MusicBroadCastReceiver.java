package com.andy.music.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicNotification;
import com.andy.music.function.MusicProgressManager;
import com.andy.music.utility.BroadCastHelper;
import com.andy.music.utility.ContextUtil;

/**
 * 广播接收器
 * Created by Andy on 2014/12/21.
 */
public class MusicBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TagConstants.TAG, "MusicBroadCastReceiver-->"+action);
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
