package com.andy.music.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.andy.music.MServiceConnection;
import com.andy.music.function.MusicListFactory;
import com.andy.music.R;
import com.andy.music.entity.MusicList;
import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicPlayService;
import com.andy.music.widget.PlayBarFragment;


public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 添加播放控制条模块
        if(findViewById(R.id.fragment_container)!=null) {
            if(savedInstanceState!=null) {
                return;
            }

            PlayBarFragment playBarFragment = new PlayBarFragment();
            playBarFragment.setArguments(getIntent().getExtras());

            getFragmentManager().beginTransaction().add(R.id.fragment_container, playBarFragment).commit();

        }

        // 设置透明状态栏
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        initView();

    }

    @Override
    protected void onDestroy() {
        Log.d(TagConstants.TAG, "MusicListActivity-->onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {


        // 决定跳转到哪个列表
        Intent intent = new Intent();

        switch (v.getId()) {

            case R.id.btn_to_favorite_music:
                // 跳转到我的最爱音乐列表
                intent.setClass(this, FavoriteMusicActivity.class);
                break;
            case R.id.btn_to_local_music:
                // 跳转到本地音乐列表
                intent.setClass(this, LocalMusicActivity.class);
                break;
            case R.id.btn_to_recent_music:
                // 跳转到最近播放列表
                intent.setClass(this, RecentMusicActivity.class);
                break;
            case R.id.btn_to_play_view:
                // 跳转到播放界面
                intent.setClass(this, PlayActivity.class);
                break;
            default:
                break;
        }
         startActivity(intent);

    }


    private void initView() {

        // 初始化成员变量
        Button toLocalMusic = (Button) findViewById(R.id.btn_to_local_music);
        Button toFavorMusic = (Button) findViewById(R.id.btn_to_favorite_music);
        Button toRecentMusic = (Button) findViewById(R.id.btn_to_recent_music);
        Button toCustomList = (Button) findViewById(R.id.btn_to_custom_list);
        Button toPlayView = (Button) findViewById(R.id.btn_to_play_view);

        // 设置监听事件
        toLocalMusic.setOnClickListener(this);
        toFavorMusic.setOnClickListener(this);
        toRecentMusic.setOnClickListener(this);
        toPlayView.setOnClickListener(this);
//        toCustomList.setOnClickListener(this);

        // 创建音乐列表
        MusicListFactory.create(MusicList.MUSIC_LIST_LOCAL);
        MusicListFactory.create(MusicList.MUSIC_LIST_RECENT);
        MusicListFactory.create(MusicList.MUSIC_LIST_FAVORITE);
        MusicListFactory.create(MusicList.MUSIC_LIST_DOWNLOAD);

    }


}
