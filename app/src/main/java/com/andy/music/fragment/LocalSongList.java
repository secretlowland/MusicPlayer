package com.andy.music.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.andy.music.R;
import com.andy.music.adapter.MusicListAdapter;
import com.andy.music.data.CursorAdapter;
import com.andy.music.data.MusicScanner;
import com.andy.music.entity.Music;
import com.andy.music.util.BroadCastHelper;
import com.andy.music.util.MusicLocator;

import java.util.List;

/**
 * 歌曲列表模块
 * Created by Andy on 2014/12/17.
 */
public class LocalSongList extends BaseSongList {

    @Override
    List<Music> getList() {
        // 获取传递进来的内容（查询语句）
        Bundle bundle = getArguments();
        String selection = null;
        String selectionArgs[] = null;
        if (bundle != null) {
            selection = bundle.getString("selection");
            selectionArgs = bundle.getStringArray("selection_args");
        }
        Cursor searchCursor = CursorAdapter.get(selection, selectionArgs);
        List<Music> musicList = null;
        if (searchCursor != null) {
            musicList = MusicScanner.scan(searchCursor);
            searchCursor.close();
        }
        return musicList;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView)view.findViewById(R.id.lv_list_common);
        DisplayMetrics res = getResources().getDisplayMetrics();
        int paddingTopDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, res);
        int paddingBottomDp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, res);
        listView.setPadding(0, paddingTopDp, 0, paddingBottomDp);
    }

}
