package com.andy.music.function;

import android.view.View;

/**
 * 列表视图管理者抽象类
 * 为列表视图设置适配器和监听器等
 * Created by Andy on 2014/12/15.
 */
public abstract class ListViewManager {

    public abstract View loadView(View view);
    public abstract void setAdapter();
    public abstract void setListener();
}
