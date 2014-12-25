package com.andy.music.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andy.music.R;
import com.andy.music.adapter.MyPagerAdapter;
import com.andy.music.function.ListViewFactory;
import com.andy.music.function.MusicListManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 2014/12/13.
 */
public class ViewPagerFragment extends android.support.v4.app.Fragment {

    private ViewPager musicPager;
    private PagerTabStrip pagerTabStrip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        musicPager = (ViewPager)getActivity().findViewById(R.id.view_pager_local_music);
        pagerTabStrip = (PagerTabStrip)getActivity().findViewById(R.id.pager_title_local_music);

        // 要加载的数据
        final List<android.support.v4.app.Fragment> fragmentList = new ArrayList<android.support.v4.app.Fragment>();
        fragmentList.add(new MusicListFragment());
        fragmentList.add(new NavPanelFragment());
        fragmentList.add(new TestFragment());

        final ArrayList<String> titles = new ArrayList<String>();
        titles.add("歌曲");
        titles.add("歌手");
        titles.add("专辑");

        // 设置适配器
        FragmentStatePagerAdapter mAdapter = new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int i) {
                return fragmentList.get(i);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        };
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int i) {
                return fragmentList.get(i);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }

        };
        musicPager.setAdapter(mAdapter);

    }
}
