package com.andy.music.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SearchView;
import android.widget.Toast;

import com.andy.music.R;
import com.andy.music.fragment.LocalMusicFragment;
import com.andy.music.fragment.SearchSongList;
import com.andy.music.fragment.SongListFragment;
import com.andy.music.function.MusicListManager;
import com.andy.music.entity.TagConstants;
import com.andy.music.fragment.NavPanelFragment;
import com.andy.music.fragment.PlayBarFragment;
import com.andy.music.fragment.TopBarFragment;
import com.andy.music.function.MusicListFactory;
import com.andy.music.function.MusicNotification;
import com.andy.music.utility.ContextUtil;
import com.andy.music.utility.MusicLocator;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends FragmentActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {

    private static final int NOTIFICATON_ID = 1;
    private RelativeLayout layout;
    private SearchView searchView;
    private SearchManager searchManager;
    private GestureDetector detector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);

        // 设置 ActionBar
        ActionBar actionbar = getActionBar();
        if (actionbar!=null) {
            actionbar.setTitle("音乐");
            actionbar.setDisplayShowHomeEnabled(false);  // 是否显示图标 默认true
            actionbar.setDisplayShowTitleEnabled(true);  // 是否显示标题 默认true
            actionbar.setDisplayHomeAsUpEnabled(false);  // 是否显示返回图标
            actionbar.setHomeButtonEnabled(false);  // 标题是否可点击
        }

        // 初始化
        init();

        // 更新媒体库
        updateMediaStore();

        // 设置透明状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);   //透明导航栏
//        }

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
//            transaction.add(R.id.frag_container_top_bar, topBarFragment);
            transaction.add(R.id.frag_container_main_content, navPanelFragment);
            transaction.add(R.id.frag_container_play_bar, playBarFragment);
            transaction.commit();
        }

        // 加载通知栏
//        initNotification();
//        MusicNotification.showNofi(this);
        MusicNotification notification = MusicNotification.getInstance(this);
        notification.sendNotification();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onDestroy() {
//        Log.d(TagConstants.TAG, "MusicListActivity-->onDestroy()");
        MusicLocator.saveMusicLocation();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("歌曲");
        searchView.setOnQueryTextListener(searchViewChangedListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:  // ActionBar返回键
                onBackPressed();  // 调用系统返回方法
                break;
            case R.id.action_search:
                //TODO  查找歌曲
                startActivity(new Intent(this, SearchActivity.class));
                break;
            default: break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        Log.d(TagConstants.TAG, "onFling()");
//        if (velocityX<0) {
//            // 左滑，切换到下一页
//            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            LocalMusicFragment fragment = new LocalMusicFragment();
//            transaction.setCustomAnimations(R.anim.frag_in, R.anim.frag_out);
//            transaction.replace(R.id.frag_container_main_content, fragment);
//            transaction.addToBackStack(null);
//            transaction.commit();
//        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        Log.d(TagConstants.TAG, "onTouch()");
        return detector.onTouchEvent(event);
    }

    private void init() {

        // 创建音乐列表
        MusicListFactory.create(MusicListManager.MUSIC_LIST_CURRENT);
        MusicListFactory.create(MusicListManager.MUSIC_LIST_LOCAL);
        MusicListFactory.create(MusicListManager.MUSIC_LIST_RECENT);
        MusicListFactory.create(MusicListManager.MUSIC_LIST_FAVORITE);
        MusicListFactory.create(MusicListManager.MUSIC_LIST_DOWNLOAD);

        // 初始化变量
        layout = (RelativeLayout) this.findViewById(R.id.frag_container_main_content);
        detector = new GestureDetector(this,this);

        layout.setOnTouchListener(this);
        layout.setLongClickable(true);
        detector.setIsLongpressEnabled(true);

        // 获取上次的音乐位置
        MusicLocator.getMusicLocation();

        // 设置标题
        this.setTitle("音乐");

    }

    private void initNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        Intent intent = new Intent(this, PlayActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker("Hello, I'm Andy!");
        builder.setWhen(System.currentTimeMillis());
//        builder.setStyle(new Notification.InboxStyle());
        builder.setContentTitle("Title");
        builder.setContentText("text");
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.noti_bar);

        // 在通知栏显示歌曲名称和歌手名
        views.setTextViewText(R.id.tv_music_name, MusicLocator.getCurrentMusic().getName());
        views.setTextViewText(R.id.tv_music_singer, MusicLocator.getCurrentMusic().getSinger());
        builder.setContent(views);

        builder.setOngoing(true);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATON_ID, builder.build());   // 发送通知
    }


    // 更新媒体库
    private void  updateMediaStore() {
        // 通知系统扫描媒体库
        MediaScannerConnection.scanFile(this, new String[]{Environment
                .getExternalStorageDirectory().getAbsolutePath()}, null, null);
    }

    // SearchView 的监听
    private SearchView.OnQueryTextListener searchViewChangedListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SearchSongList fragment = new SearchSongList();
            Bundle bundle = new Bundle();
            String[] args = {query};
            bundle.putStringArray("selection_args", args);
            fragment.setArguments(bundle);
            transaction.replace(R.id.frag_container_main_content, fragment);
            transaction.commit();
            searchView.clearFocus();
            searchManager.stopSearch();
            // 隐藏系统软键盘
//            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return true;
        }
    };


}
