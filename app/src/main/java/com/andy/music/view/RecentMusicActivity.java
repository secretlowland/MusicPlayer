package com.andy.music.view;

import android.os.Bundle;

import com.andy.music.entity.MusicList;

/**
 * 最近播放列表
 * Created by Andy on 2014/11/21.
 */
public class RecentMusicActivity extends MusicListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public MusicList getMusicList() {
        return MusicList.getInstance(MusicList.MUSIC_LIST_RECENT);
    }
}
