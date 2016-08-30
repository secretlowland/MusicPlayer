package com.andy.music.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.andy.music.entity.Music;
import com.andy.music.function.MusicListManager;

import java.util.List;

/**
 * 音乐收藏列表
 * Created by Andy on 2015/1/11.
 */
public class FavouriteSongList extends BaseSongList {

    private List<Music> musicList;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);
        showLoadingView (true);
        Handler handler = new Handler ();
        handler.postDelayed (new Runnable () {
            @Override
            public void run() {
                MusicListManager manager = MusicListManager.getInstance (MusicListManager.MUSIC_LIST_FAVORITE);
                musicList = (manager != null ? manager.getList () : null);
                updateList (musicList);
                showLoadingView (false);
            }
        }, 0);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 设置标题
        TopBarFragment topBar = (TopBarFragment) getActivity().getSupportFragmentManager().findFragmentByTag("topBar");
        if (topBar != null) {
            topBar.setCustomTitle("我的最爱");
        }

        // 设置ListView的长按事件（删除喜爱音乐等）
        ListView listView = getListView();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (getSecAdapter() == null || musicList == null || musicList.isEmpty()) {
                    return false;
                }
                final int subpos = getSecAdapter().getSubPosition(position);
                final Music music = musicList.get(subpos);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("删除");
                builder.setMessage("确定删除 " + music.getName() + "?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MusicListManager manager = MusicListManager.getInstance(MusicListManager.MUSIC_LIST_FAVORITE);
                        if (manager != null && manager.remove(music)) {
                            Toast.makeText(FavouriteSongList.this.getActivity(), "移除成功", Toast.LENGTH_SHORT).show();
                            musicList.remove(subpos);
                            updateList(musicList);
                        } else {
                            Toast.makeText(FavouriteSongList.this.getActivity(), "移除成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                return true;
            }
        });

    }


}