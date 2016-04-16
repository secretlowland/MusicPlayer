package com.andy.music.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.andy.music.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 主界面导航模块
 * Created by Andy on 2014/11/27.
 */
public class NavPanelFragment extends android.support.v4.app.Fragment {

    GridView navPanel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_panel, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化成员变量
        navPanel = (GridView)getActivity().findViewById(R.id.gv_nav_panel);
        navPanel.setAdapter(getInitAdapter());

        // 设置监听事件
        navPanel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = null;
                String tag = "";
                switch (position) {
                    case 0:
                        fragment = new LocalMusicFragment();
                        tag = "localMusicFragment";
                        break;
                    case 1:
                        fragment = new FavouriteSongList();
                        tag = "FavouriteSongList";
                        break;
                    case 2:
                        fragment = new RecentSongList();
                        tag = "RecentSongList";
                        break;
                    case 3:
                        fragment = new CloudSongList ();
                        tag = "CloudSongList";
                        break;
                    default: break;
                }

                transaction.replace(R.id.frag_container_main_content, fragment, tag);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }

    @Override
    public void onResume() {
        TopBarFragment topBar = (TopBarFragment)getActivity().getSupportFragmentManager().findFragmentByTag("topBar");
        if (topBar!=null) {
            topBar.setCustomTitle("音乐");
        }
        super.onResume();
    }


    /**
     * 初始化 用于 GridView 的 Adapter
     * @return adapter
     */
    private BaseAdapter getInitAdapter() {
        final ArrayList<HashMap<String, Object>> data = new ArrayList<>();

        HashMap<String , Object> local = new HashMap<>();
        local.put("icon", R.drawable.class_icon_local);
        local.put("title", "本地音乐");
        data.add(local);

        HashMap<String , Object> favourite = new HashMap<>();
        favourite.put("icon", R.drawable.class_icon_favourite);
        favourite.put("title", "我的最爱");
        data.add(favourite);

        HashMap<String , Object> recent = new HashMap<>();
        recent.put("icon", R.drawable.class_icon_recent);
        recent.put("title", "最近播放");
        data.add(recent);

        HashMap<String, Object> cloud = new HashMap<> ();
        cloud.put ("icon", R.drawable.class_icon_favourite);
        cloud.put ("title", "网络歌曲");
        data.add (cloud);

        int resource = R.layout.grid_cell_nav_panel;
        String[] from = {"icon", "title"};
        int[] to = {R.id.iv_nav_panel_icon, R.id.tv_nav_panel_title};
        return new SimpleAdapter(getActivity(), data, resource, from, to);
    }
}
