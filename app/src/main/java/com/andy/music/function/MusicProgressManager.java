package com.andy.music.function;

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

    /**
     * 获得个 歌曲的当前播放进度
     * @return 歌曲当前播放进度
     */
    public static int getProgress() {
        return currentProgress;
    }

    /**
     * 设置歌曲的播放进度
     * @param progress 播放进度
     */
    public static void setProgress(int progress) {
        currentProgress = progress;
    }


    /**
     * 播放开始后开始计时
     */
    public static void start() {
        if (progressTimer != null) {
            progressTimer.cancel();
            progressTimer = null;
        }

        if (progressTimerTask != null) {
            progressTimerTask = null;
        }
        progressTimer = new Timer();
        progressTimerTask = new TimerTask() {
            @Override
            public void run() {
                // 播放进度增加 1 s
                currentProgress += 1000;
            }
        };
        progressTimer.schedule(progressTimerTask, 0, 1000);
    }

    /**
     * 播放暂停，停止计时
     */
    public static void stop() {
        if (progressTimerTask != null) progressTimerTask = null;
        if (progressTimer != null) {
            progressTimer.cancel();
            progressTimer.purge();
        }
    }

    /**
     * 初始化音乐播放进度，即进度重置为 0
     */
    public static void init() {
        // 进度重置为 0
        currentProgress = 0;
    }

}
