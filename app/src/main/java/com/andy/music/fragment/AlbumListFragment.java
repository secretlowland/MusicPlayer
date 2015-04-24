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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.andy.music.R;
import com.andy.music.data.CursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        mainView = inflater.inflate(R.layout.fragment_list_album, (ViewGroup) getActivity().findViewById(R.id.view_pager_local_music), false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 移除已存在的 view
        ViewGroup group = ((ViewGroup) mainView.getParent());
        if (group != null) {
            group.removeAllViewsInLayout();
        }
        return mainView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 获取 ListView
        listView = (ListView) view.findViewById(R.id.lv_list_album);

        // 为 ListView 设置适配器
        listView.setAdapter(getAdapter());

        // 为 ListView 设置监听器
        listView.setOnItemClickListener(getOnItemClickListener());
    }

    public BaseAdapter getAdapter() {
        Cursor cursor = CursorAdapter.getMediaLibCursor();
        int resource = R.layout.list_cell_double_line;
        String[] from = {"name", "num"};
        int[] to = {R.id.tv_list_cell_double_line_first, R.id.tv_list_cell_double_line_second};

        List<HashMap<String, Object>> data = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            HashMap<String, Object> map = null;
            HashMap<String, Object> pre = null;
            if (!data.isEmpty()) {  // 列表不为空,则遍历 data 中已存在的数据
                int num = 0;  // 该歌手出现的次数
                for (int i = 0; i < data.size(); i++) {
                    pre = data.get(i);
                    if (name.equals(pre.get("name"))) {
                        String str = (String) pre.get("num");
                        num = getInt(str);
                        data.remove(pre);
                    }
                }
                map = new HashMap<>();
                map.put("name", name);
                map.put("num", ++num + "首歌曲");
                data.add(map);
            } else {  // 列表为空
                map = new HashMap<>();
                map.put("name", name);
                map.put("num", 1 + "首歌曲");
                data.add(map);
            }

        }
        cursor.close();
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, resource, from, to);
        return adapter;
    }

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取当前 item 的专辑名
                String albumName = ((TextView) view.findViewById(R.id.tv_list_cell_double_line_first)).getText().toString();

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

    /**
     * 从字符串中取出数字
     * 注：因为此方法只在本类中特定环境下使用，所以并不是很严谨
     * @param str 带有数字的字符串，如 “ 5首歌曲 ”
     * @return 字符串中的数字
     */
    private int getInt(String str) {
        int index = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c <= 57) index++;
        }
        return Integer.parseInt(str.substring(0, index));
    }

}
