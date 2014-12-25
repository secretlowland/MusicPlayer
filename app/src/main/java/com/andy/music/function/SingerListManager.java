package com.andy.music.function;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import com.andy.music.R;

/**
 * Created by Andy on 2014/12/14.
 */
public class SingerListManager {

    private ListView listView;

    public View getView(Context context) {
        initView(context);
        return listView;
    }

    public void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        listView = (ListView)inflater.inflate(R.layout.singer_list, null).findViewById(R.id.lv_singer_list);
        Cursor cursor = com.andy.music.data.CursorAdapter.getMediaLibCursor();
        String[] from = { MediaStore.Audio.Media.ARTIST } ;
        int[] to = { R.id.tv_singer_list_singer };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context, R.layout.singer_list_cell, cursor, from, to, 0);
        listView.setAdapter(adapter);
    }
}
