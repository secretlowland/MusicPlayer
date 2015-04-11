package com.andy.music.fragment;


import android.util.Log;

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

}