package com.andy.music.function;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Andy on 2014/12/14.
 */
public class AlbumListManager extends ListViewManager {

    private ListView listView;

    @Override
    public View loadView(View view) {
        listView = (ListView)view;
        setAdapter();
        return null;
    }

    @Override
    public void setAdapter() {

    }

    @Override
    public void setListener() {

    }

    public void initView() {

    }

}
