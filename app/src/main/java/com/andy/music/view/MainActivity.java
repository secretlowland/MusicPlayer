package com.andy.music.view;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.andy.music.R;
import com.andy.music.entity.MusicList;
import com.andy.music.entity.TagConstants;
import com.andy.music.fragment.NavPanelFragment;
import com.andy.music.fragment.PlayBarFragment;
import com.andy.music.function.MusicListFactory;


public class MainActivity extends Activity {

    private SlideMenu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 初始化
        init();

        // 设置透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);   //透明导航栏
        }

        // 加载模块
        if (findViewById(R.id.frag_container_nav_panel) != null) {
            if (savedInstanceState != null) {
                return;
            }

            NavPanelFragment navPanelFragment = new NavPanelFragment();
            PlayBarFragment playBarFragment = new PlayBarFragment();
            navPanelFragment.setArguments(getIntent().getExtras());
            playBarFragment.setArguments(getIntent().getExtras());

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.frag_container_nav_panel, navPanelFragment);
            transaction.add(R.id.frag_container_play_bar, playBarFragment);
            transaction.commit();
        }

    }

    @Override
    protected void onDestroy() {
        Log.d(TagConstants.TAG, "MusicListActivity-->onDestroy()");
        super.onDestroy();
    }

    private void init() {

        // 创建音乐列表
        MusicListFactory.create(MusicList.MUSIC_LIST_LOCAL);
        MusicListFactory.create(MusicList.MUSIC_LIST_RECENT);
        MusicListFactory.create(MusicList.MUSIC_LIST_FAVORITE);
        MusicListFactory.create(MusicList.MUSIC_LIST_DOWNLOAD);

        menu = (SlideMenu) findViewById(R.id.slide_menu);
    }


}
