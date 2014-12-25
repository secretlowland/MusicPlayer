package com.andy.music.fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.music.R;
import com.andy.music.entity.TagConstants;
import com.andy.music.utility.MusicLocator;

/**
 * 歌曲列表模块
 * Created by Andy on 2014/12/16.
 */
public class SingerListFragment extends Fragment {

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_common, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 获取 ListView
        listView =(ListView) view.findViewById(R.id.lv_list_common);

        // 为 ListView 设置适配器
        listView.setAdapter(getAdapter());

        // 为 ListView 设置监听器
        listView.setOnItemClickListener(getOnItemClickListener());
    }

    public BaseAdapter getAdapter() {
        Cursor cursor = com.andy.music.data.CursorAdapter.getMediaLibCursor();
        String[] from = {MediaStore.Audio.Media.ARTIST};
        int[] to = {R.id.tv_list_cell_double_line_first};
        SimpleCursorAdapter adapter =new SimpleCursorAdapter(getActivity(), R.layout.list_cell_double_line, cursor, from, to, 0);
        return adapter;
    }


    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取当前 item 的歌手名
                String singerName = ((TextView)view.findViewById(R.id.tv_list_cell_double_line_first)).getText().toString();

                // 将主体部分替换成 songListFragment
                SongListFragment fragment = new SongListFragment();
                Bundle bundle = new Bundle();
                String whereClause = MediaStore.Audio.Media.ARTIST + "='" + singerName + "'";
                bundle.putString("where_clause", whereClause);    // 将查询语句传递到 fragment
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frag_container_main_content, fragment).addToBackStack(null).commit();

            }
        };
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Log.d(TagConstants.TAG, "fragment可见");
        } else {
            Log.d(TagConstants.TAG, "fragment不可见");
        }
    }
}
