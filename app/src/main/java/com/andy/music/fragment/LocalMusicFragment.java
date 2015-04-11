package com.andy.music.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andy.music.R;
import com.andy.music.adapter.MyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地音乐模块
 * 整体为一个ViewPager，由歌曲，歌手，专辑，文件夹四个页面构成
 * Created by Andy on 2014/12/13.
 */
public class LocalMusicFragment extends Fragment {

    private View mainview;
    private ViewPager musicPager;

    private List<Fragment> fragmentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        Log.d(TagConstants.TAG, "LocalMusicFragment-->onCreate()");
        ActionBar actionBar = getActivity().getActionBar();
        if(actionBar!=null) {
            actionBar.setTitle("本地音乐");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.d(TagConstants.TAG, "LocalMusicFragment-->onCreateView()");
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Log.d(TagConstants.TAG, "LocalMusicFragment-->onViewCreated()");
        musicPager = (ViewPager)getActivity().findViewById(R.id.view_pager_local_music);

        // 要加载的页面
        fragmentList = new ArrayList<>();

        fragmentList.add(new SongListFragment());
        fragmentList.add(new SingerListFragment());
        fragmentList.add(new AlbumListFragment());

        // 页面标题
        final ArrayList<String> titles = new ArrayList<String>();
        titles.add("歌曲");
        titles.add("歌手");
        titles.add("专辑");

        // 设置适配器
        musicPager.setAdapter(new MyFragmentPagerAdapter(getFragmentManager(), fragmentList, titles));

    }


    @Override
    public void onAttach(Activity activity) {
//        Log.d(TagConstants.TAG, "LocalMusicFragment-->onAttach()");
        super.onAttach(activity);
    }





    @Override
    public void onStart() {
//        Log.d(TagConstants.TAG, "LocalMusicFragment-->onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
//        Log.d(TagConstants.TAG, "LocalMusicFragment-->onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
//        Log.d(TagConstants.TAG, "LocalMusicFragment-->onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
//        Log.d(TagConstants.TAG, "LocalMusicFragment-->onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
//        Log.d(TagConstants.TAG, "LocalMusicFragment-->onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
//        Log.d(TagConstants.TAG, "LocalMusicFragment-->onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
