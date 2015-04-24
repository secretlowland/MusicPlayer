package com.andy.music.util;

/**
 * 时间助手类
 * Created by Andy on 2014/12/7.
 */
public class TimeHelper {

    /**
     * 将毫秒转换成 MM:SS 格式
     * @param ms  毫秒
     * @return MM:SS 格式的 时间
     */
    public static String timeFormat(int ms) {
        int seconds = ms/1000;
        int min = seconds/60;
        int sec = seconds%60;
        return (min<10?"0"+min:min)+":"+(sec<10?"0"+sec:sec);
    }
}
