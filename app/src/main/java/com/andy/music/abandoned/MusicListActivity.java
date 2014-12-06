package com.andy.music.abandoned;

import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.music.function.MusicListAdapter;
import com.andy.music.R;
import com.andy.music.entity.Music;
import com.andy.music.entity.MusicList;
import com.andy.music.entity.TagConstants;
import com.andy.music.utility.MusicLocator;

/**
 * 音乐列表视图
 * Created by Andy on 2014/11/16.
 */
public abstract class MusicListActivity extends ListActivity implements AdapterView.OnItemClickListener  {


    /**
     * 视图中显示的音乐列表
     */
    public MusicList musicList;

    /**
     * Activity 中用于显示音乐列表的 ListView
     */
    public ListView musicListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TagConstants.TAG, "MusicListActivity-->onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        initView();
    }

    @Override
    protected void onStart() {
        Log.d(TagConstants.TAG, "MusicListActivity-->onStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(TagConstants.TAG, "MusicListActivity-->onRestart()");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.d(TagConstants.TAG, "MusicListActivity-->onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TagConstants.TAG, "MusicListActivity-->onDestroy()");
        super.onDestroy();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MusicListAdapter adapter = new MusicListAdapter(this, musicList.getList(), R.layout.music_list_cell);
        adapter.notifyDataSetChanged();

        // 改变上次选中的歌曲显示样式
        ((TextView) view.findViewById(R.id.tv_music_name)).setTextColor(Color.GRAY);

        // 获得当前歌曲
        Music music = musicList.getList().get(position);



        // 定位当前播放歌曲
        Bundle bundle = new Bundle();
        bundle.putString("list_name", musicList.getListName());
        bundle.putInt("position", position);
        bundle.putInt("source_id", music.getId());
        MusicLocator.setLocation(bundle);

        // 改变当前播放歌曲的显示样式
        ((TextView) view.findViewById(R.id.tv_music_name)).setTextColor(Color.parseColor("#ec505e"));


        // 添加到最近播放
        MusicList recentList = MusicList.getInstance(MusicList.MUSIC_LIST_RECENT);
        recentList.add(new Music(musicList.getList().get(position).getId()));

    }

    public void initView() {

        // 初始化全局变量
        musicListView = (ListView) this.findViewById(android.R.id.list);
        musicList = getMusicList();  // 获取从子类传入的 MusicList

        musicListView.setOnItemClickListener(this);

        // 加载音乐列表
        if (musicList != null && !musicList.isEmpty()) {
            MusicListAdapter adapter = new MusicListAdapter(this, musicList.getList(), R.layout.music_list_cell);
            musicListView.setAdapter(adapter);
        } else {          // 列表为空
            Toast.makeText(this, "一首歌曲都没有呢~~", Toast.LENGTH_SHORT).show();
        }

    }

    public abstract MusicList getMusicList();

}
