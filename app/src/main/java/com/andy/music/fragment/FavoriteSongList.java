package com.andy.music.fragment;

import android.util.Log;

import com.andy.music.entity.Music;
import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicListManager;

import java.util.List;

/**
 * Created by Andy on 2015/1/11.
 */
public class FavoriteSongList extends BaseSongList {
    @Override
    List<Music> getList() {
        MusicListManager manager = MusicListManager.getInstance(MusicListManager.MUSIC_LIST_FAVORITE);
        List<Music> list = manager.getList();
        Log.d(TagConstants.TAG, "list大小-------------->"+list.size());
        return list;
    }
}
