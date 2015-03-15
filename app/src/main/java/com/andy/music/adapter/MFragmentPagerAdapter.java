package com.andy.music.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by Andy on 2015/3/5.
 */
public class MFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "FragmentPagerAdapter";
    private static final boolean DEBUG = false;
    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;

    private List<Fragment> list;
    private List<String> titles;

    public MFragmentPagerAdapter(FragmentManager fm, List<Fragment> list, List<String> titles) {
        super(fm);
        this.mFragmentManager = fm;
        this.list = list;
        this.titles = titles;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }
}
