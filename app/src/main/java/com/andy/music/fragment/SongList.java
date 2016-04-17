package com.andy.music.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.andy.music.data.CursorAdapter;
import com.andy.music.data.MusicScanner;
import com.andy.music.entity.Music;

import java.util.List;

/**
 * Created by Andy on 2015/4/30.
 */
public class SongList extends BaseSongList {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);
        showLoadingView (true);
        Handler handler = new Handler ();
        handler.postDelayed (new Runnable () {
            @Override
            public void run() {
                // 获取传递进来的内容（查询语句）
                List<Music> musicList = null;
                Bundle bundle = getArguments();
                String selection = null;
                String selectionArgs[] = null;
                if (bundle != null) {
                    selection = bundle.getString("selection");
                    selectionArgs = bundle.getStringArray("selection_args");
                }
                Cursor searchCursor = CursorAdapter.get(selection, selectionArgs);
                if (searchCursor != null) {
                    musicList = MusicScanner.scan(searchCursor);
                    searchCursor.close();
                    updateList (musicList);
                    showLoadingView (false);
                }
            }
        }, 0);
    }
}
