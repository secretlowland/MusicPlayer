package com.andy.music.activity;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.andy.music.R;
import com.andy.music.entity.TagConstants;
import com.andy.music.fragment.NavPanelFragment;
import com.andy.music.fragment.PlayBarFragment;
import com.andy.music.fragment.SearchSongList;
import com.andy.music.fragment.TopBarFragment;
import com.andy.music.function.MusicListFactory;
import com.andy.music.function.MusicListManager;
import com.andy.music.util.MusicLocator;


public class MainActivity extends FragmentActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {

    private RelativeLayout layout;
    private SearchView searchView;
    private SearchManager searchManager;
    private GestureDetector detector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);

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

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frag_container_top_bar, topBarFragment, "topBar");
            transaction.add(R.id.frag_container_main_content, navPanelFragment);
            transaction.add(R.id.frag_container_play_bar, playBarFragment);
            transaction.commit();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        Log.d(TagConstants.TAG, "MainActivity-->onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
//        Log.d(TagConstants.TAG, "MainActivity-->onDestroy()");
        MusicLocator.saveMusicLocation();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("歌曲");
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
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
                searchView.onActionViewCollapsed();  //  收起搜索框
                break;
            default:
                break;

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

        // 首次启动时扫描音乐
        firstScanMusic();

        // 初始化变量
        layout = (RelativeLayout) this.findViewById(R.id.frag_container_main_content);
        detector = new GestureDetector(this, this);

        layout.setOnTouchListener(this);
        layout.setLongClickable(true);
        detector.setIsLongpressEnabled(true);

        // 设置 ActionBar
        ActionBar actionbar = getActionBar();
        if (actionbar != null) {
            actionbar.setTitle("音乐");
            actionbar.setDisplayShowHomeEnabled(false);  // 是否显示图标 默认true
            actionbar.setDisplayShowTitleEnabled(true);  // 是否显示标题 默认true
            actionbar.setDisplayHomeAsUpEnabled(false);  // 是否显示返回图标
            actionbar.setHomeButtonEnabled(false);  // 标题是否可点击
        }

    }


    // 更新媒体库
    private void updateMediaStore() {
        // 通知系统扫描媒体库
        MediaScannerConnection.scanFile(this, new String[]{Environment
                .getExternalStorageDirectory().getAbsolutePath()}, null, null);
    }

    // 判断是否是首次启动，如果是则扫描音乐
    private void firstScanMusic() {
        SharedPreferences pref = this.getSharedPreferences("launch_info", Context.MODE_PRIVATE);
        boolean firstLaunch = pref.getBoolean("first_launch", true);
        if (firstLaunch) {
//            MusicListManager.scanMusic();
//            Toast.makeText(this, "扫描音乐完成！", Toast.LENGTH_SHORT).show();
        }
        pref.edit().putBoolean("first_launch", false).apply();
    }


    // SearchView 的事件监听
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
            transaction.addToBackStack(null);
            transaction.commit();
            searchView.clearFocus();
            searchManager.stopSearch();
            searchView.onActionViewCollapsed();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return true;
        }
    };


}
