package com.andy.music.fragment;

import com.andy.music.entity.Music;
import com.andy.music.function.MusicListManager;

import java.util.List;

/**
 * Created by Andy on 2015/4/28.
 */
public class LocalSongList extends BaseSongList {
    @Override
    List<Music> getList() {
        MusicListManager manager = MusicListManager.getInstance(MusicListManager.MUSIC_LIST_LOCAL);
        List<Music> list = manager.getList();
        return list;
    }
}
