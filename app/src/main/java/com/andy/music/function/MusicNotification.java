package com.andy.music.function;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private Notification notification;
    private Receiver receiver;

    private RemoteViews views;

    private MusicNotification(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.builder = new Notification.Builder(context);
        this.notification = builder.build();
        this.receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
        context.registerReceiver(receiver, filter);
    }

    public static MusicNotification getInstance(Context context) {
        if(musicNotification==null) {
            Log.d(TagConstants.TAG, "musicNotification is null");
            musicNotification = new MusicNotification(context);
        } else {
            Log.d(TagConstants.TAG, "musicNotification is not null");
        }
        return musicNotification;
    }

    public void refreshNotification() {
        builder.setTicker("哈哈。换了一首歌哦~~~~~~~~");
        views.setTextViewText(R.id.tv_music_name, MusicLocator.getCurrentMusic().getName());
        views.setTextViewText(R.id.tv_music_singer, MusicLocator.getCurrentMusic().getSinger());
    }

    public  void sendNotification () {

        // 点击通知栏的意图
        Intent intent = new Intent(context, PlayActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // 在通知栏显示歌曲名称和歌手名
        views = new RemoteViews(context.getPackageName(), R.layout.noti_bar);
        Music music = MusicLocator.getCurrentMusic();
        if(music!=null) {
            views.setTextViewText(R.id.tv_music_name, music.getName());
            views.setTextViewText(R.id.tv_music_singer, music.getSinger());
        }

        // 设置通知栏属性
        builder.setContent(views);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker("Hello, I'm Andy!");
        builder.setWhen(System.currentTimeMillis());
        builder.setOngoing(true);

        // 发送通知
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


    class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TagConstants.TAG, "我收到了通知，更新通知栏");
            String action = intent.getAction();
            if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT) ||
                    action.equals(BroadCastHelper.ACTION_MUSIC_PLAY) ||
                    action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM) ||
                    action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS)) {
                // TODO 更新通知栏
                musicNotification.refreshNotification();
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }
        }
    }
}
