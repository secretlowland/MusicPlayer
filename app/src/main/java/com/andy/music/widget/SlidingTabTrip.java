package com.andy.music.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 导航条
 * Created by lxn on 2015/10/3.
 */
public class SlidingTabTrip extends HorizontalScrollView {

    LinearLayout tabContainer;
    ViewPager viewPager;

    public SlidingTabTrip(Context context) {
        this(context, null);
    }

    public SlidingTabTrip(Context context, AttributeSet attrs) {
        super(context, attrs);
        tabContainer = new LinearLayout(context);
        tabContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.addView(tabContainer);
    }

    public void setViewPager(ViewPager viewPager) {
        if (viewPager!=null) {
            this.viewPager =viewPager;
            this.viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
            initTabTrip();
        }
    }

    private void initTabTrip() {
        PagerAdapter adapter = viewPager.getAdapter();
        tabContainer.setWeightSum(adapter.getCount());
        for (int i=0; i<adapter.getCount(); i++) {
            String title = (String)adapter.getPageTitle(i);
            TextView tv = createTabView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
            tv.setText(title);
            tv.setLayoutParams(params);
            tabContainer.addView(tv);
        }
    }

    private TextView createTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setSingleLine(true);
        return textView;
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }



}
