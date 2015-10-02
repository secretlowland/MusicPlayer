package com.andy.music.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.andy.music.R;

/**
 * 底部菜单栏按钮
 * Created by zy on 2015/9/17.
 */
public class MenuButton extends Button {

    private final int DEFAULT_TEXT_COLOR = 0xff666666;
    private final int DEFALUT_TEXT_SIZE = 0x8;

    public MenuButton(Context context) {
        super(context);
        initView();
    }

    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setPadding(0, 30, 0, 30);
        setTextColor(DEFAULT_TEXT_COLOR);
        setBackgroundResource(R.drawable.btn_common);
    }
}
