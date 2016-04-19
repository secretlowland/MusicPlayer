package com.andy.music.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.andy.music.R;
import com.andy.music.data.CursorAdapter;
import com.andy.music.util.CharacterParser;
import com.andy.music.util.StringComparator;
import com.nolanlawson.supersaiyan.SectionedListAdapter;
import com.nolanlawson.supersaiyan.Sectionizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 歌曲列表模块
 * Created by Andy on 2014/12/16.
 */
public class LocalSingerList extends ListFragment {

    private List<HashMap<String, Object>> data = new ArrayList<>();
    private SectionedListAdapter secAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareData();
    }

    public BaseAdapter getAdapter() {
        int resource = R.layout.list_cell_double_line;
        String[] from = {"name", "num"};
        int[] to = {R.id.tv_list_cell_double_line_first, R.id.tv_list_cell_double_line_second};

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, resource, from, to);
        secAdapter = SectionedListAdapter.Builder.create(getActivity(), adapter)
                .setSectionizer(new Sectionizer<HashMap<String, Object>>() {
                    @Override
                    public CharSequence toSection(HashMap<String, Object> input) {
                        if (input != null) {
                            String name = (String) input.get("name");
                            if (name != null && name.length() > 0) {
                                String spelling = CharacterParser.getInstance().getSelling(name);
                                char firstChar = Character.toUpperCase(spelling.charAt(0));
                                if (firstChar >='A' && firstChar <='Z') {
                                    return Character.toString(firstChar);
                                }
                            }
                        }
                        return "#";
                    }
                })
                .sortKeys(new StringComparator())
                .build();
        return secAdapter;
    }


    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取当前 item 的歌手名
                String singerName = ((TextView) view.findViewById(R.id.tv_list_cell_double_line_first)).getText().toString();

                // 将主体部分替换成 songListFragment
                SongList fragment = new SongList();
                Bundle bundle = new Bundle();
                String selection = MediaStore.Audio.Media.ARTIST + "=?";
                String[] selectionArgs = {singerName};
                bundle.putString("selection", selection);    // 将查询语句传递到 fragment
                bundle.putStringArray("selection_args", selectionArgs);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag("localMusicFragment");
                transaction.hide(frag);
                transaction.add(R.id.frag_container_main_content, fragment).addToBackStack(null).commit();

            }
        };
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

    private void prepareData() {

        showLoadingView(true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = CursorAdapter.getMediaLibCursor();
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
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
                showLoadingView(false);
                secAdapter.notifyDataSetChanged();
            }
        }, 0);
    }

    private int getInt(String str) {
        int index = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c <= 57) index++;
        }
        return Integer.parseInt(str.substring(0, index));
    }

}
