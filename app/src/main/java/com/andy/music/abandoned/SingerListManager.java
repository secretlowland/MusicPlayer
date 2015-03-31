package com.andy.music.abandoned;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.andy.music.R;

import java.io.Serializable;

/**
 * Created by Andy on 2014/12/14.
 */
public class SingerListManager implements Serializable {

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
        int[] to = { R.id.tv_list_cell_double_line_first };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context, R.layout.fragment_list_common, cursor, from, to, 0);
        listView.setAdapter(adapter);
    }
}
