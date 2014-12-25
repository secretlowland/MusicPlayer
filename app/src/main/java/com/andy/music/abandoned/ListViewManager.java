package com.andy.music.abandoned;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.andy.music.R;
import com.andy.music.utility.ContextUtil;

/**
 * Created by Andy on 2014/12/14.
 */
public class ListViewManager {

    private ListView listView;

    public View getView() {
        return listView;
    }

    public void initView() {
        LayoutInflater inflater = LayoutInflater.from(ContextUtil.getInstance());

        // 实例化 listView
        listView =(ListView) inflater.inflate(R.layout.list_normal, null).findViewById(R.id.lv_list_normal);
    }
}
