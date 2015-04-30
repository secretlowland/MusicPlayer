package com.andy.music.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.andy.music.R;
import com.andy.music.data.CursorAdapter;
import com.andy.music.data.MusicScanner;
import com.andy.music.entity.Music;
import com.andy.music.function.MusicListManager;

import java.util.List;

/**
 * Created by Andy on 2015/4/30.
 */
public class SongList extends BaseSongList {

    private ListView listView;
    @Override
    List<Music> getList() {
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
        }
        return musicList;
    }
}
