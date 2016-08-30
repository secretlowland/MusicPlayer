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
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.andy.music.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 主界面导航模块
 * Created by Andy on 2014/11/27.
 */
public class NavPanelFragment extends android.support.v4.app.Fragment {

    GridView navPanel, navCenter, navBottom;

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
        navPanel = (GridView)getActivity().findViewById(R.id.gv_nav_top);
        navCenter = (GridView)getActivity().findViewById(R.id.gv_nav_center);
        navBottom = (GridView)getActivity().findViewById(R.id.gv_nav_bottom);
        navPanel.setAdapter(getTopAdapter());
        navCenter.setAdapter(getCenterAdapter());
        navBottom.setAdapter(getBottomAdapter());

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
        navCenter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                CloudSongList fragment = null;
                String tag = "";
                switch (position) {
                    case 0:
                        fragment = new CloudSongList();
                        fragment.setTitle("排行榜");
                        tag = "rankFragment";
                        break;
                    case 1:
                        fragment = new CloudSongList();
                        fragment.setTitle("猜你喜欢");
                        tag = "recommendFragment";
                        break;
                    case 2:
                        fragment = new CloudSongList();
                        fragment.setTitle("最新单曲");
                        tag = "latestFragment";
                        break;
                    default: break;
                }

                transaction.replace(R.id.frag_container_main_content, fragment, tag);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        navBottom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                CloudSongList fragment = null;
                String tag = "";
                switch (position) {
                    case 0:
                        fragment = new CloudSongList();
                        fragment.setTitle("聆听大自然的声音");
                        tag = "localMusicFragment";
                        break;
                    case 1:
                        fragment = new CloudSongList();
                        fragment.setTitle("韩语抒情，流淌在心底的歌");
                        tag = "FavouriteSongList";
                        break;
                    case 2:
                        fragment = new CloudSongList();
                        fragment.setTitle("等待是相似的形状");
                        tag = "RecentSongList";
                        break;
                    case 3:
                        fragment = new CloudSongList ();
                        fragment.setTitle("以梦为马");
                        tag = "CloudSongList";
                        break;
                    case 4:
                        fragment = new CloudSongList ();
                        fragment.setTitle("最好的年华，最好的你");
                        tag = "CloudSongList";
                        break;
                    case 5:
                        fragment = new CloudSongList ();
                        fragment.setTitle("如果时光倒流");
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
    private BaseAdapter getTopAdapter() {
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

        int resource = R.layout.grid_cell_nav_panel;
        String[] from = {"icon", "title"};
        int[] to = {R.id.iv_nav_panel_icon, R.id.tv_nav_panel_title};
        return new SimpleAdapter(getActivity(), data, resource, from, to);
    }

    private BaseAdapter getCenterAdapter() {
        final ArrayList<HashMap<String, Object>> data = new ArrayList<>();

        HashMap<String , Object> rank = new HashMap<>();
        rank.put("icon", R.drawable.rank_list);
        rank.put("title", "排行榜");
        data.add(rank);

        HashMap<String , Object> recommend = new HashMap<>();
        recommend.put("icon", R.drawable.recommend_list);
        recommend.put("title", "猜你喜欢");
        data.add(recommend);

        HashMap<String , Object> latest = new HashMap<>();
        latest.put("icon", R.drawable.latest_list);
        latest.put("title", "最新单曲");
        data.add(latest);

        int resource = R.layout.grid_cell_nav_net;
        String[] from = {"icon", "title"};
        int[] to = {R.id.iv_nav_panel_icon, R.id.tv_nav_panel_title};
        return new SimpleAdapter(getActivity(), data, resource, from, to);
    }

    private BaseAdapter getBottomAdapter() {
        final ArrayList<HashMap<String, Object>> data = new ArrayList<>();

        HashMap<String , Object>list1 = new HashMap<>();
        list1.put("icon", R.drawable.cover_1);
        list1.put("title", "聆听大自然的声音");
        data.add(list1);

        HashMap<String , Object> list2 = new HashMap<>();
        list2.put("icon", R.drawable.cover_2);
        list2.put("title", "韩语抒情，流淌在心底的歌");
        data.add(list2);

        HashMap<String , Object> list3 = new HashMap<>();
        list3.put("icon", R.drawable.cover_3);
        list3.put("title", "等待是相似的形状");
        data.add(list3);

        HashMap<String , Object> list4 = new HashMap<>();
        list4.put("icon", R.drawable.cover_4);
        list4.put("title", "以梦为马");
        data.add(list4);

        HashMap<String , Object> list5 = new HashMap<>();
        list5.put("icon", R.drawable.cover_5);
        list5.put("title", "最好的年华，最好的你");
        data.add(list5);

        HashMap<String , Object> list6 = new HashMap<>();
        list6.put("icon", R.drawable.cover_6);
        list6.put("title", "如果时光倒流");
        data.add(list6);

        int resource = R.layout.grid_cell_nav_list;
        String[] from = {"icon", "title"};
        int[] to = {R.id.iv_nav_panel_icon, R.id.tv_nav_panel_title};
        return new SimpleAdapter(getActivity(), data, resource, from, to);
    }

}
