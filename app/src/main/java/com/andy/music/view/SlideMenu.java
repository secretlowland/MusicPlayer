package com.andy.music.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.andy.music.R;
import com.andy.music.entity.TagConstants;
import com.andy.music.utility.ScreenInfo;
import com.nineoldandroids.view.ViewHelper;

/**
 * 侧滑菜单
 * Created by Andy on 2014/11/29.
 */
public class SlideMenu extends HorizontalScrollView {


    private ViewGroup wrapper, content;
    private LinearLayout menu;

    private int screenWidth;
    private int screenHeight;
    private int menuWidth;
    private int rightPadding;
    private boolean firstTime = true;
    private boolean isOpen;


    /**
     * 此构造方法有程序中代码调用
     *
     * @param context 上下文
     */
    public SlideMenu(Context context) {
        this(context, null);
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 此构造方法在使用自定义 xml 属性文件时被调用
     *
     * @param context      上下文
     * @param attrs
     * @param defStyleAttr
     */
    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 获取自定义的属性
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlideMenu, defStyleAttr, 0);
        for (int i = 0; i < ta.getIndexCount(); i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.SlideMenu_paddingRight:
                    rightPadding = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()));
                    break;
                default:
                    break;
            }
        }

        ta.recycle();  // 释放资源

        // 获取屏幕宽度和高度
        screenWidth = ScreenInfo.getScreenWidth();
        screenHeight = ScreenInfo.getScreenHeight();

    }

    /**
     * 设置子 View 的宽和高，以及自己的宽和高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (firstTime) {    // 只在第一次调用
            wrapper = (ViewGroup) getChildAt(0);
            menu = (LinearLayout) wrapper.getChildAt(0);
            content = (ViewGroup) wrapper.getChildAt(1);

            menuWidth = menu.getLayoutParams().width = screenWidth - rightPadding;   // 设置 menu 的宽度
            menu.getLayoutParams().height = screenHeight;
            content.getLayoutParams().width = screenWidth;    // 设置 content 的宽度
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        // 通过设置偏移量，将 menu 隐藏
        if (changed) {
            this.scrollTo(menuWidth, 0);
            isOpen = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:   // 滑动屏幕后抬起
                // 水平方向移动的距离，也就是距离最左边的距离
                int scrollX = getScrollX();
                if (scrollX >= (menuWidth / 2)) {
                    this.smoothScrollTo(menuWidth, 0);
                    isOpen = false;
                } else {
                    this.smoothScrollTo(0, 0);
                    isOpen = true;
                }
                return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        // 设置属性动画
        float scale = l*1.0f/rightPadding;   // 变化梯度 1~0
        ViewHelper.setTranslationX(menu, l*0.8f);
        ViewHelper.setAlpha(menu, 1-scale*0.6f);
        ViewHelper.setScaleX(menu, 1-scale*0.3f);
        ViewHelper.setScaleY(menu, 1-scale*0.3f);
        ViewHelper.setScaleX(content, 1-(1-scale)*0.3f);
        ViewHelper.setScaleY(content, 1-(1-scale)*0.3f);
    }


    public void openMenu() {
        if (isOpen) return;
        this.smoothScrollTo(0, 0);
        isOpen = true;
    }

    public void closeMenu() {
        if (!isOpen) return;
        this.smoothScrollTo(menuWidth, 0);
        isOpen = false;
    }

    public void toggleMenu() {
        if (isOpen) {
            Log.d(TagConstants.TAG, "关闭");
            closeMenu();
        } else {
            Log.d(TagConstants.TAG, "打开");
            openMenu();
        }
    }

}
