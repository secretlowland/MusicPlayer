package com.andy.music.utility;

import android.app.Application;

/**
 * Context 辅助类，用于获取应用程序上下文环境
 * Created by Andy on 2014/11/22.
 */
public class ContextUtil extends Application {

    private static ContextUtil instance;

    public static ContextUtil getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
