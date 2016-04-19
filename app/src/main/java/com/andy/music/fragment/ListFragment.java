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
import android.widget.RelativeLayout;

import com.andy.music.R;


/**
 * ListFragment 基类
 * Created by Andy on 2014/12/16.
 */
public abstract class ListFragment extends Fragment {

    private ListView listView;
    private RelativeLayout loadingView;

    public ListView getListView() {
        return listView;
    }

    public RelativeLayout getLoadingView() {
        return loadingView;
    }

    /**
     * 设置是否显示正在加载的视图
     * @param show
     */
    public void showLoadingView(boolean show) {
        if (loadingView !=null) {
            if (show) {
                loadingView.setVisibility(View.VISIBLE);
            } else {
                loadingView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_common, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 获取 ListView 对象
        listView = (ListView) view.findViewById(R.id.lv_list_common);

        loadingView = (RelativeLayout) view.findViewById(R.id.loading_view);

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