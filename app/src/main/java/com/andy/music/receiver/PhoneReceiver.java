package com.andy.music.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.andy.music.service.MusicPlayService;
import com.andy.music.util.BroadCastHelper;

/**
 * 接收来电和去电的广播
 * Created by Andy on 2014/12/10.
 */
public class PhoneReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {  // 广播为拨打电话
            // 发送暂停播放的广播
            BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PAUSE);
            MusicPlayService.setIsPlaying(true);
        } else {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PAUSE);
                    MusicPlayService.setIsPlaying(true);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PAUSE);
                    MusicPlayService.setIsPlaying(true);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (MusicPlayService.isPlaying()) {
                        BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_START);
                    }
            }
        }


    }
}
