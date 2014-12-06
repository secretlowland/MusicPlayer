package com.andy.music.utility;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 获取屏幕信息
 * Created by Andy on 2014/11/29.
 */
public final class ScreenInfo {

    private static DisplayMetrics metrics = new DisplayMetrics();


    public static int getScreenInfo() {
        Context context = ContextUtil.getInstance();
        WindowManager wm =  (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return 0;
    }


    public static int getScreenWidth() {
        getScreenInfo();
        return metrics.widthPixels;
    }

    public static int getScreenHeight() {
        getScreenInfo();
        return metrics.heightPixels;
    }

}
