package com.andy.music.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.andy.music.R;
import com.andy.music.function.MusicListManager;
import com.andy.music.entity.TagConstants;
import com.andy.music.fragment.NavPanelFragment;
import com.andy.music.fragment.PlayBarFragment;
import com.andy.music.fragment.TopBarFragment;
import com.andy.music.function.MusicListFactory;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends FragmentActivity {

    private static final int NOTIFICATON_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);


        // 初始化
        init();

        // 设置透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);   //透明导航栏
        }

        // 加载模块
        if (findViewById(R.id.frag_container_main_content) != null) {
            if (savedInstanceState != null) {
                return;
            }

            TopBarFragment topBarFragment = new TopBarFragment();
            NavPanelFragment navPanelFragment = new NavPanelFragment();
            PlayBarFragment playBarFragment = new PlayBarFragment();
            navPanelFragment.setArguments(getIntent().getExtras());
            playBarFragment.setArguments(getIntent().getExtras());

            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frag_container_top_bar, topBarFragment);
            transaction.add(R.id.frag_container_main_content, navPanelFragment);
            transaction.add(R.id.frag_container_play_bar, playBarFragment);
            transaction.commit();
        }

        // 加载通知栏
//        MusicNotification notify = MusicNotification.getInstance(this);
//        notify.sendNotification(this);

//        Notification.Builder builder = new Notification.Builder(this);
//        Intent intent = new Intent(this, PlayActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        builder.setContentIntent(pendingIntent);
//        builder.setSmallIcon(R.drawable.ic_launcher);
//        builder.setTicker("Hello, I'm Andy!");
//        builder.setWhen(System.currentTimeMillis());
////        builder.setStyle(new Notification.InboxStyle());
//        builder.setContentTitle(MusicLocator.getLocatedMusic().getName());
//        builder.setContentText(MusicLocator.getLocatedMusic().getSinger());
//        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.noti_bar);
//        builder.setContent(views);
//
//        builder.setOngoing(true);
//        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(NOTIFICATON_ID, builder.build());   // 发送通知

    }

    @Override
    protected void onDestroy() {
        Log.d(TagConstants.TAG, "MusicListActivity-->onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                //TODO  查找歌曲
                Toast.makeText(this, "咋找歌曲", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.action_settings:
                Toast.makeText(this, "咋找歌曲", Toast.LENGTH_SHORT).show();
                break;
            default: break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {

        // 创建音乐列表
        MusicListFactory.create(MusicListManager.MUSIC_LIST_CURRENT);
        MusicListFactory.create(MusicListManager.MUSIC_LIST_LOCAL);
        MusicListFactory.create(MusicListManager.MUSIC_LIST_RECENT);
        MusicListFactory.create(MusicListManager.MUSIC_LIST_FAVORITE);
        MusicListFactory.create(MusicListManager.MUSIC_LIST_DOWNLOAD);

    }


}
