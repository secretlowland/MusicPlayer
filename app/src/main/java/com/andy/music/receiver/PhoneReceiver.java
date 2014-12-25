package com.andy.music.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.andy.music.entity.TagConstants;
import com.andy.music.utility.BroadCastHelper;

/**
 * 接收来电和去电的广播
 * Created by Andy on 2014/12/10.
 */
public class PhoneReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {  // 广播为拨打电话
            Log.d(TagConstants.TAG, "广播-->拨打电话");
            // 发送暂停播放的广播
            BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PAUSE);
        } else {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TagConstants.TAG, "电话响了。");
                    BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PAUSE);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TagConstants.TAG, "拨打电话");
                    BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PAUSE);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TagConstants.TAG, "恢复正常");
                    BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_START);
            }
        }


    }
}
