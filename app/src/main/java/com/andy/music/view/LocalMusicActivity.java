package com.andy.music.view;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.andy.music.function.MusicListAdapter;
import com.andy.music.data.MusicScanner;
import com.andy.music.R;
import com.andy.music.data.CursorAdapter;
import com.andy.music.entity.MusicList;
import com.andy.music.entity.TagConstants;

/**
 * 本地音乐列表视图
 * Created by Andy on 2014/11/21.
 */
public class LocalMusicActivity extends MusicListActivity implements Runnable {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TagConstants.TAG, "LocalMusicActivity--onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        Log.d(TagConstants.TAG, "LocalMusicActivity-->onStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(TagConstants.TAG, "LocalMusicActivity-->onRestart()");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.d(TagConstants.TAG, "LocalMusicActivity-->onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TagConstants.TAG, "LocalMusicActivity-->onDestroy()");
        super.onDestroy();
    }

    @Override
    public MusicList getMusicList() {
        return MusicList.getInstance(MusicList.MUSIC_LIST_LOCAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 创建扫描音乐的菜单
        menu.add(Menu.NONE, Menu.FIRST + 1, 0, "扫描音乐"); // 添加扫描音乐菜单
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST + 1:

                // 扫描本地音乐并保存到数据库，此操作比较耗时，单独在一个线程中执行
                new Thread(this, "scanMusic").start();
                // 更新视图
                Cursor searchCursor = CursorAdapter.get(null);
                super.musicListView.setAdapter(new MusicListAdapter(this, MusicScanner.scan(searchCursor), R.layout.music_list_cell));
                Toast.makeText(this, "扫描完成！", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {

        // 由于在数据库中建立列表的的时间比较长，故单独放在一个线程中
        // 获得查询游标
        Cursor searchCursor = CursorAdapter.get(null);

        // 将游标中的数据存到数据库
        musicList.setList(searchCursor);

        // 关闭 Cursor，释放资源
        searchCursor.close();
    }

}
