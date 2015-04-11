package com.andy.music.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.andy.music.R;

/**
 * 专辑列表模块
 * Created by Andy on 2014/12/16.
 */
public class AlbumListFragment extends Fragment {

    private View mainView;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mainView = inflater.inflate(R.layout.fragment_list_album, (ViewGroup)getActivity().findViewById(R.id.view_pager_local_music), false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // 移除已存在的 view
        ViewGroup group = ((ViewGroup) mainView.getParent());
        if (group!=null) {
            group.removeAllViewsInLayout();
//            Log.d(TagConstants.TAG, "已移除已存在的view");
        }
        return mainView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 获取 ListView
        listView =(ListView) view.findViewById(R.id.lv_list_album);

        // 为 ListView 设置适配器
        listView.setAdapter(getAdapter());

        // 为 ListView 设置监听器
        listView.setOnItemClickListener(getOnItemClickListener());
    }

    public BaseAdapter getAdapter() {
        Cursor cursor = com.andy.music.data.CursorAdapter.getMediaLibCursor();
        String[] from = {MediaStore.Audio.Media.ALBUM};
        int[] to = {R.id.tv_list_cell_double_line_first};
        return new SimpleCursorAdapter(getActivity(), R.layout.list_cell_double_line, cursor, from, to, 0);
    }

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取当前 item 的专辑名
                String albumName = ((TextView)view.findViewById(R.id.tv_list_cell_double_line_first)).getText().toString();

                // 将主体部分替换成 songListFragment
                SongListFragment fragment = new SongListFragment();
                Bundle bundle = new Bundle();
                String selection = MediaStore.Audio.Media.ALBUM + "=?";
                String[] selectionArgs = {albumName};
                bundle.putString("selection", selection);    // 将查询语句传递到 fragment
                bundle.putStringArray("selection_args", selectionArgs);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frag_container_main_content, fragment).addToBackStack(null).commit();
            }
        };
    }


}
