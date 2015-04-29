package com.andy.music.fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andy.music.R;
import com.andy.music.adapter.MyFragmentPagerAdapter;
import com.andy.music.entity.TagConstants;
import com.andy.music.widget.IndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地音乐模块
 * 整体为一个ViewPager，由歌曲，歌手，专辑，文件夹四个页面构成
 * Created by Andy on 2014/12/13.
 */
public class LocalMusicFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private View mainview;
    private ViewPager musicPager;

    private List<Fragment> fragmentList;
    private List<IndicatorView> indicatorList;

    private int currentPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.d(TagConstants.TAG, "LocalMusicFragment-->onCreateView()");
        return inflater.inflate(R.layout.fragment_local_music, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Log.d(TagConstants.TAG, "LocalMusicFragment-->onViewCreated()");

        TopBarFragment topBar = (TopBarFragment)getActivity().getSupportFragmentManager().findFragmentByTag("topBar");
        if (topBar!=null) {
            topBar.setCustomTitle("本地音乐");
        }
        musicPager = (ViewPager) getActivity().findViewById(R.id.view_pager_local_music);

        // 要加载的页面
        fragmentList = new ArrayList<>();
        fragmentList.add(new LocalSongList());
        fragmentList.add(new SingerListFragment());
        fragmentList.add(new AlbumListFragment());

        // 页面标题
        final ArrayList<String> titles = new ArrayList<String>();
        titles.add("歌曲");
        titles.add("歌手");
        titles.add("专辑");

        // 设置适配器
        musicPager.setAdapter(new MyFragmentPagerAdapter(getFragmentManager(), fragmentList, titles));
        musicPager.setOnPageChangeListener(this);

        // 测试Indicatior
        IndicatorView indicator0 = (IndicatorView) getActivity().findViewById(R.id.indicator_0);
        IndicatorView indicator1 = (IndicatorView) getActivity().findViewById(R.id.indicator_1);
        IndicatorView indicator2 = (IndicatorView) getActivity().findViewById(R.id.indicator_2);
        indicatorList = new ArrayList<>();
        indicatorList.add(indicator0);
        indicatorList.add(indicator1);
        indicatorList.add(indicator2);
        indicator0.setOnClickListener(this);
        indicator1.setOnClickListener(this);
        indicator2.setOnClickListener(this);
        setCurrentIndicator(currentPage, 1.0f);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.currentPage = musicPager.getCurrentItem();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.indicator_0:
                musicPager.setCurrentItem(0, true);
                setCurrentIndicator(0, 1.0f);
                break;
            case R.id.indicator_1:
                musicPager.setCurrentItem(1, true);
                setCurrentIndicator(1, 1.0f);
                break;
            case R.id.indicator_2:
                musicPager.setCurrentItem(2, true);
                setCurrentIndicator(2, 1.0f);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            IndicatorView left = indicatorList.get(position);
            IndicatorView right = indicatorList.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setCurrentIndicator(int index, float alpha) {
        resetIndicator();
        indicatorList.get(index).setIconAlpha(alpha);
    }

    private void resetIndicator() {
        for (IndicatorView indicatorView : indicatorList) {
            indicatorView.setIconAlpha(0);
        }
    }

}

