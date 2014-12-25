package com.andy.music.function;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.andy.music.entity.TagConstants;
import com.andy.music.view.PlayActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 音乐播放进度管理类
 * Created by Andy on 2014/12/21.
 */
public class MusicProgressManager {

    private static int currentProgress = 0;
    private static Timer progressTimer;
    private static TimerTask progressTimerTask;

    public static int getProgress() {
        return currentProgress;
    }

    public static void setProgress(int progress) {
        currentProgress = progress;
    }


    public static void start() {
        if (progressTimer!=null) {
            progressTimer.cancel();
            progressTimer = null;
        }

        if (progressTimerTask!=null) {
            progressTimerTask = null;
        }
        progressTimer = new Timer();
        progressTimerTask = new TimerTask() {
            @Override
            public void run() {
                // 播放进度增加 1 s
                currentProgress +=  1000;
                Log.d(TagConstants.TAG, "currentProgress-->"+currentProgress);
            }
        };
        progressTimer.schedule(progressTimerTask, 0, 1000);
    }

    public static void stop() {
        if (progressTimerTask!=null) progressTimerTask = null;
        if (progressTimer!=null) {
            progressTimer.cancel();
            progressTimer.purge();
        }
    }

    public static void init() {
        // 进度重置为 0
        currentProgress = 0;
    }

}
