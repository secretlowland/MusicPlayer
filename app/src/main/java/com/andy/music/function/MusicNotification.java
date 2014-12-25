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
    private NotificationManager notificationManager;
    private Notification.Builder builder;
    private Notification notification;
    private Receiver receiver;

    private MusicNotification(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new Notification.Builder(context);
        notification = builder.build();
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
        context.registerReceiver(receiver, filter);
    }

    public static MusicNotification getInstance(Context context) {
        return new MusicNotification(context);
    }

    public void sendNotification(Context context) {

        // 通知栏要加载的视图
        RemoteViews views = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.noti_bar);

        // 点击通知栏的意图
        Intent contentIntent = new Intent(context, PlayActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, contentIntent, 0);

        // 点击暂停/播放按钮和下一首按钮
        Intent toggleBtnIntent = new Intent(BroadCastHelper.ACTION_MUSIC_START);
        Intent nextBtnIntent = new Intent(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);

        // 在通知栏显示歌曲名称和歌手名
        views.setTextViewText(R.id.tv_music_name, MusicLocator.getLocatedMusic().getName());
        views.setTextViewText(R.id.tv_music_singer, MusicLocator.getLocatedMusic().getSinger());


//        Intent fillInIntent = new Intent(BroadCastHelper.ACTION_MUSIC_PLAY);
//        PendingIntent pendButtonIntent = PendingIntent.getBroadcast(context, 0, fillInIntent, 0);
//        views.setOnClickPendingIntent(R.id.btn_music_toggle, pendButtonIntent);
        builder.setContent(views);
        builder.setContentIntent(pendingIntent);
        builder.setTicker("Hello, I'm Andy!");
        builder.setOngoing(true);
        notificationManager.notify(NOTIFICATION_ID, builder.build());   // 发送通知
    }

    public void refreshNotification() {
        builder.setTicker("哈哈。换了一首歌哦~~~~~~~~");
        builder.setContentTitle(MusicLocator.getLocatedMusic().getName());
        builder.setContentText(MusicLocator.getLocatedMusic().getSinger());
    }

    class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TagConstants.TAG, "我收到了通知，更新通知栏");
            String action = intent.getAction();
            if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT)) {
                // TODO 更新通知栏
                notificationManager.notify(NOTIFICATION_ID, notification);
            }
        }
    }
}
