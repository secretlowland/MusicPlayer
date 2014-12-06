package com.andy.music.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andy.music.R;

/**
 * 自定义标题栏
 * Created by Andy on 2014/12/4.
 */
public class TitleBar extends RelativeLayout {

    private ImageButton menuBtn;
    private ImageButton searchBtn;
    private TextView title;

    private OnButtonClickListener listener;

    public interface OnButtonClickListener {
        public void onTitleButtonClick();
        public void onMenuButtonClick();
    }


    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        title = new TextView(context);
        searchBtn = new ImageButton(context);
        menuBtn = new ImageButton(context);

        // 设置属性
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, 0);
        setBackgroundColor(ta.getColor(R.styleable.TitleBar_background, Color.parseColor("#34f49930")));
        title.setText(ta.getString(R.styleable.TitleBar_title));
        title.setTextColor(ta.getColor(R.styleable.TitleBar_titleTextColor, Color.parseColor("#95b55b")));
        title.setTextSize(ta.getDimensionPixelSize(R.styleable.TitleBar_titleTextSize, 20));
        searchBtn.setImageResource(ta.getResourceId(R.styleable.TitleBar_searchIcon, 0));
        menuBtn.setImageResource(ta.getResourceId(R.styleable.TitleBar_menuIcon, 0));
        menuBtn.setBackgroundColor(Color.parseColor("#00000000"));

        // 设置布局
        LayoutParams titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutParams searchBtnParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutParams menuBtnParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        titleParams.addRule(RelativeLayout.CENTER_VERTICAL);
        menuBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        menuBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        menuBtnParams.rightMargin = 50;
        menuBtnParams.height = 50;
        menuBtnParams.width = 50;
        searchBtnParams.addRule(RelativeLayout.ALIGN_RIGHT);

        // 添加子控件
        addView(title, titleParams);
        addView(menuBtn, menuBtnParams);

        title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTitleButtonClick();
            }
        });

        // 释放资源
        ta.recycle();
    }


    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

}
