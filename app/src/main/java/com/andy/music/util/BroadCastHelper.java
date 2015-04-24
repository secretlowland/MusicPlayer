package com.andy.music.util;

import android.content.Intent;

/**
 * 广播发送中心
 * Created by Andy on 2014/11/28.
 */
public class BroadCastHelper {

    /**
     * 广播内容。本应用所有的广播都从这里发送
     */

    // 与音乐播放有关的广播
    public static final String ACTION_MUSIC_START = "ACTION_MUSIC_START";
    public static final String ACTION_MUSIC_PAUSE = "ACTION_MUSIC_PAUSE";
    public static final String ACTION_MUSIC_PLAY = "ACTION_MUSIC_PLAY";
    public static final String ACTION_MUSIC_PLAY_PREVIOUS = "ACTION_MUSIC_PLAY_PREVIOUS";
    public static final String ACTION_MUSIC_PLAY_NEXT = "ACTION_MUSIC_PLAY_NEXT";
    public static final String ACTION_MUSIC_PLAY_RANDOM = "ACTION_MUSIC_PLAY_RANDOM";

    /**
     * 发送一条广播
     * @param action  广播内容
     */
    public static void send(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        ContextUtil.getInstance().sendBroadcast(intent);
    }
}
