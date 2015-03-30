package com.andy.music.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.andy.music.R;

/**
 * ListFragment 基类
 * Created by Andy on 2014/12/16.
 */
public abstract class ListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_song, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView;

        // 获取 ListView 对象
        listView = (ListView) getActivity().findViewById(R.id.lv_song);

        // 为 ListView 对象设置适配器
        listView.setAdapter(getAdapter());

        // 为 ListView 设置监听器
        listView.setOnItemClickListener(getOnItemClickListener());
    }

    /**
     * 从子类获取适配器
     */
    public abstract BaseAdapter getAdapter();

    /**
     * 从子类获取监听器
     */
    public abstract AdapterView.OnItemClickListener getOnItemClickListener();

}
