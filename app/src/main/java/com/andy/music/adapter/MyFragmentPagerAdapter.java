package com.andy.music.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.andy.music.entity.TagConstants;

import java.util.List;

/**
 * Created by Andy on 2014/12/15.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "FragmentPagerAdapter";
    private static final boolean DEBUG = false;
    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;

    private List<Fragment> list;
    private List<String> titles;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list, List<String> titles) {
        super(fm);
        this.mFragmentManager = fm;
        this.list = list;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TagConstants.TAG, "MFPA-->getItem()-->"+position);
        return list.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(TagConstants.TAG, "MFPA-->instantiateItem()-->"+position);

        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        final long itemId = getItemId(position);

        // Do we already have this fragment?
        String name = makeFragmentName(container.getId(), itemId);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            Log.d(TagConstants.TAG, "fragment已经存在啦！"+itemId);
            if (DEBUG) Log.v(TAG, "Attaching item #" + itemId + ": f=" + fragment);
            mCurTransaction.attach(fragment);
        } else {
            fragment = getItem(position);
            if (DEBUG) Log.v(TAG, "Adding item #" + itemId + ": f=" + fragment);
            mCurTransaction.add(container.getId(), fragment,
                    makeFragmentName(container.getId(), itemId));
        }
        if (fragment != mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d(TagConstants.TAG, "MFPA-->destroyItem()-->"+position);
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (DEBUG) Log.v(TAG, "Detaching item #" + getItemId(position) + ": f=" + object
                + " v=" + ((Fragment)object).getView());
        mCurTransaction.detach((Fragment)object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Log.d(TagConstants.TAG, "MFPA-->setPrimaryItem()-->"+position);
        Fragment fragment = (Fragment)object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(true);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        Log.d(TagConstants.TAG, "MFPA-->finishUpdate()");
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }


    @Override
    public long getItemId(int position) {
        Log.d(TagConstants.TAG, "MFPA-->getItemId()-->"+position);
        return position;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
