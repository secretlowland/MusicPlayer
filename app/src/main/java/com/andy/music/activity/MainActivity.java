package com.andy.music.activity;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.andy.music.R;
import com.andy.music.fragment.NavPanelFragment;
import com.andy.music.fragment.PlayBarFragment;
import com.andy.music.fragment.SearchSongList;
import com.andy.music.fragment.TopBarFragment;
import com.andy.music.function.MusicListFactory;
import com.andy.music.function.MusicListManager;
import com.andy.music.util.MusicLocator;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.bmob.v3.Bmob;


public class MainActivity extends FragmentActivity {

    private final String APP_KEY_BMOB = "c4b279fe7822a48105fff4759e19c337";
    private RelativeLayout layout;
    private SearchView searchView;
    private SearchManager searchManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);

        ActivityManager.setCurrentActivity(this);

        // 初始化
        init();

        // 更新媒体库
//        updateMediaStore();

        // 初始化 Bmob
        Bmob.initialize (this, APP_KEY_BMOB);

         // 设置沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);   //透明导航栏
            //  改变状态栏颜色
            //  创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            //  给状态栏设置颜色
            tintManager.setStatusBarTintColor(Color.parseColor("#729939"));
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
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
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
        layout.setLongClickable(true);

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
