package com.andy.music.function;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.andy.music.R;
import com.andy.music.entity.Music;
import com.andy.music.entity.TagConstants;
import com.andy.music.utility.BroadCastHelper;
import com.andy.music.utility.MusicLocator;
import com.andy.music.view.PlayActivity;

/**
 * 在通知栏显示播放的音乐，并进行相关操作
 * Created by Andy on 2014/12/12.
 */
public class MusicNotification {

    private static final int NOTIFICATION_ID = 1;
    private static MusicNotification musicNotification;
    private Context context;
    private NotificationManager notificationManager;
    private Notification.Builder builder;
    private Receiver receiver;

    private RemoteViews views;
    private int num =0;

    private MusicNotification(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.builder = new Notification.Builder(context);
        this.receiver = new Receiver();
        this.views = new RemoteViews(context.getPackageName(), R.layout.noti_bar);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PAUSE);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_START);
        context.registerReceiver(receiver, filter);
    }

    public static MusicNotification getInstance(Context context) {
        if(musicNotification==null) {
            musicNotification = new MusicNotification(context);
        }
        return musicNotification;
    }

    /**
     * 更新通知栏
     */
    public void refreshNotification() {
        builder.setTicker("更新歌曲");
        Music music = MusicLocator.getCurrentMusic();
        if (music!=null) {
            views.setTextViewText(R.id.tv_music_name, music.getName());
            views.setTextViewText(R.id.tv_music_singer, music.getSinger());
        }

        if (MusicPlayService.isPlaying()) {
            views.setImageViewResource(R.id.btn_music_toggle, R.drawable.btn_play_bar_toggle_on);
        } else {
            views.setImageViewResource(R.id.btn_music_toggle, R.drawable.btn_play_bar_toggle_off);
        }

        // 设置按钮点击操作   要加 PendingIntent.FLAG_UPDATE_CURRENT ，才会正确传值
        // http://blog.csdn.net/faithmy509/article/details/8457575
        Intent nextIntent  = new Intent(context, MusicPlayService.class);
        nextIntent.putExtra("play_action", "next");
        PendingIntent nextPendingIntent = PendingIntent.getService(context, num++, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.btn_music_play_next, nextPendingIntent);

        Intent toggleIntent = new Intent(context, MusicPlayService.class);
        String action = null;
        if (MusicPlayService.isPlaying()) {
            action = "pause";
        } else {
            action = "start";
        }
        toggleIntent.putExtra("play_action", action);
        PendingIntent togglePendingIntent = PendingIntent.getService(context, num++, toggleIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.btn_music_toggle, togglePendingIntent);


    }

    /**
     * 发送通知
     */
    public  void sendNotification () {

        // 点击通知栏的意图
        Intent intent = new Intent(context, PlayActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // 在通知栏显示歌曲名称和歌手名
        Music music = MusicLocator.getCurrentMusic();
        if(music!=null) {
            views.setTextViewText(R.id.tv_music_name, music.getName());
            views.setTextViewText(R.id.tv_music_singer, music.getSinger());
        }
        if (!MusicPlayService.isPlaying()) {
            views.setImageViewResource(R.id.btn_music_toggle, R.drawable.btn_play_bar_toggle_off);
        } else {
            views.setImageViewResource(R.id.btn_music_toggle, R.drawable.btn_play_bar_toggle_on);
        }

        // 设置通知栏属性
        builder.setContent(views);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker("Hello, I'm Andy!");
        builder.setWhen(System.currentTimeMillis());
//        builder.setOngoing(true);

        // 发送通知
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * 取消通知栏
     */
    public void destroyNotification(Context context) {
        notificationManager.cancel(NOTIFICATION_ID);
        context.unregisterReceiver(receiver);
    }


    class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TagConstants.TAG, "Notification-->action-->"+action);
            if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT) ||
                    action.equals(BroadCastHelper.ACTION_MUSIC_PLAY) ||
                    action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM) ||
                    action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS)) {
                musicNotification.refreshNotification();
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }
            if (action.equals(BroadCastHelper.ACTION_MUSIC_PAUSE)) {
//                builder.setOngoing(false);
                musicNotification.refreshNotification();
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }
            if (action.equals(BroadCastHelper.ACTION_MUSIC_START)) {
//                builder.setOngoing(true);
                musicNotification.refreshNotification();
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }
        }
    }
}
