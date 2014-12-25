package com.andy.music.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.andy.music.entity.TagConstants;

import java.util.List;

/**
 * Created by Andy on 2014/12/15.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public MyPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        Log.d(TagConstants.TAG, "MyPagerAdapter");
        this.list = list;
    }

    @Override
    public Fragment getItem(int i) {
        Log.d(TagConstants.TAG, "getItem");
        return list.get(i);
    }

    @Override
    public int getCount() {
        Log.d(TagConstants.TAG, "getCount");
        return list.size();
    }
}
