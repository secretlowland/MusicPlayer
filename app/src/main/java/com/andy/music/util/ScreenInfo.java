package com.andy.music.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.andy.music.activity.ActivityManager;

import java.util.Objects;

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

    public static int getStatusHeight() {
        int statusHeight = 0;
        Rect rect = new Rect();
        Activity act = ActivityManager.getCurrentActivity();
        act.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        statusHeight = rect.top;
        if (statusHeight ==0) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object loadObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(loadObject).toString());
                statusHeight = act.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

}
