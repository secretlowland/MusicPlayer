package com.andy.music.activity;

import android.support.v4.app.FragmentActivity;

/**
 * Created by zy on 2015/9/17.
 */
public class ActivityManager {
    private static FragmentActivity currentActivity;

    public static FragmentActivity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(FragmentActivity activity) {
        currentActivity = activity;
    }
}
