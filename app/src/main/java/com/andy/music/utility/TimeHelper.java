package com.andy.music.utility;

/**
 * Created by Andy on 2014/12/7.
 */
public class TimeHelper {

    /**
     * 将毫秒转换成 MM:SS 格式
     * @param ms  毫秒
     * @return MM:SS 格式的 时间
     */
    public static String  timeFormate(int ms) {
        String time;
        int seconds = ms/1000;
        int min = seconds/60;
        int sec = seconds%60;
        if (min<10) {
            time = "0"+min+":"+sec;
        } else {
            time = min+":"+sec;
        }
        return time;
    }
}
