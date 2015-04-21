package com.andy.music.fragment;


import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.andy.music.entity.Music;
import com.andy.music.function.MusicListManager;

import java.util.List;

/**
 * Created by Andy on 2015/1/11.
 */
public class RecentSongList extends BaseSongList {
    @Override
    List<Music> getList() {
        MusicListManager manager = MusicListManager.getInstance(MusicListManager.MUSIC_LIST_RECENT);
        List<Music> list = manager.getList();
        return list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActionBar actionBar = getActivity().getActionBar();
        if(actionBar!=null) {
            actionBar.setTitle("最近播放");
            actionBar.setDisplayHomeAsUpEnabled(true);  // 是否显示返回图标
        }
    }


}