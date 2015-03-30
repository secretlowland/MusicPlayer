package com.andy.music.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andy.music.R;
import com.andy.music.adapter.MyFragmentPagerAdapter;
import com.andy.music.entity.TagConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地音乐模块
 * 整体为一个ViewPager，由歌曲，歌手，专辑，文件夹四个页面构成
 * Created by Andy on 2014/12/13.
 */
public class LocalMusicFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private View mainview;
    private ViewPager musicPager;

    private List<Fragment> fragmentList;
    private List<TextView> indicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TagConstants.TAG, "LocalMusicFragment-->onCreateView()");
        return inflater.inflate(R.layout.fragment_local_music, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TagConstants.TAG, "LocalMusicFragment-->onViewCreated()");
        musicPager = (ViewPager)getActivity().findViewById(R.id.view_pager_local_music);

        // 要加载的页面
        fragmentList = new ArrayList<>();
        fragmentList.add(new SongListFragment());
        fragmentList.add(new SingerListFragment());
        fragmentList.add(new AlbumListFragment());

        // 页面标题
        indicator = new ArrayList<>();
        indicator.add((TextView)getActivity().findViewById(R.id.indicator_song));
        indicator.add((TextView)getActivity().findViewById(R.id.indicator_singer));
        indicator.add((TextView)getActivity().findViewById(R.id.indicator_album));
        final ArrayList<String> titles = new ArrayList<String>();
        titles.add("歌曲");
        titles.add("歌手");
        titles.add("专辑");

        // 设置适配器
        musicPager.setAdapter(new MyFragmentPagerAdapter(getFragmentManager(), fragmentList, titles));
        musicPager.setOnPageChangeListener(this);
    }

    @Override
    public void onDestroyView() {
        Log.d(TagConstants.TAG, "LocalMusicFragment-->onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TagConstants.TAG, "LocalMusicFragment-->onDestroy()");
        super.onDestroy();
//        for (int i=0; i<fragmentList.size(); i++) {
//            Fragment fragment = fragmentList.get(i);
//            if (fragment.isInLayout()) {
//                Log.d(TagConstants.TAG, "frag "+ (i+1)+ " is destroyed.");
//                fragment.onDestroyView();
//                fragment.onDestroy();
//                fragment.onDetach();
//                fragmentList.clear();
//            }
//        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.ll_indicator);
        TextView tv =(TextView) getActivity().findViewById(R.id.flag);
        View slider = (View)getActivity().findViewById(R.id.v_slider);
        tv.setText("pos:" + position + "   posOffset:" + positionOffset + "   posOffsetPix:" + positionOffsetPixels);
        tv.setTextColor(Color.parseColor("#ff00ff"));

        int x = (int)(100*positionOffset);
        slider.scrollTo(x, 0);
        int columWidth = layout.getWidth()/6;
        for (int i=0; i<layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);


        }

    }

    private void indicate(View view, int lastPos, int offset) {
        int temp = lastPos;

    }


    @Override
    public void onPageSelected(int position) {
        initIndicator();
        TextView textView = indicator.get(position);
        textView.setTextColor(Color.parseColor("#70ae95"));
        textView.setBackgroundColor(Color.parseColor("#cbd6ae"));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void initIndicator() {
        for (TextView tv: indicator) {
            tv.setTextColor(Color.parseColor("#2d3526"));
            tv.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

}
