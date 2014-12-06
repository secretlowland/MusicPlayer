package com.andy.music.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.andy.music.entity.Music;
import com.andy.music.entity.MusicList;
import com.andy.music.entity.TagConstants;

/**
 * 定位当前播放的歌曲以及记录上次播放的位置
 * Created by Andy on 2014/11/19.
 */
public class MusicLocator {

    private static SharedPreferences pref = ContextUtil.getInstance().getSharedPreferences("music_location", Context.MODE_PRIVATE);
    private static SharedPreferences.Editor editor = pref.edit();


    /**
     * 获取上次播放的歌曲的位置信息
     * @return 歌曲的位置信息
     */
    public static Bundle getLocation() {
//        SharedPreferences pref = ContextUtil.getInstance().getSharedPreferences("music_location", Context.MODE_PRIVATE);
        Bundle bundle = new Bundle();
        bundle.putString("list_name", pref.getString("list_name", MusicList.MUSIC_LIST_LOCAL));
        bundle.putInt("position", pref.getInt("position", 0));
        bundle.putInt("source_id", pref.getInt("source_id", 0));
        return bundle;
    }

    public static int getPosition() {
        return getLocation().getInt("position");
    }

    public static int getSourceId() {
        return getLocation().getInt("source_id");
    }

    public static MusicList getLocatedList() {
        String listName = MusicLocator.getLocation().getString("list_name", MusicList.MUSIC_LIST_LOCAL);
        return MusicList.getInstance(listName);
    }

    public static Music getLocatedMusic() {
        int position = MusicLocator.getLocation().getInt("position", 0);
        MusicList list = getLocatedList();
        if (list.getList().size()<=0) return null;
        return getLocatedList().getList().get(position);
    }

    /**
     * 定位当前播放的歌曲
     * @param bundle  当前歌曲的位置信息，包括列表名，在列表中的位置及歌曲的 ID
     */
    public static void setLocation(Bundle bundle) {

        // 将歌曲位置信息保存到 SharedPreferences
//        SharedPreferences pref = ContextUtil.getInstance().getSharedPreferences("music_location", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String listName = bundle.getString("list_name");
        int position = bundle.getInt("position");
        int srcId = bundle.getInt("source_id");

        if (listName!=null) {
            editor.putString("list_name", listName);
        }
        editor.putInt("position", position);
        editor.putInt("source_id", srcId);
        editor.commit();

    }

    /**
     * 定位当前歌曲下一首
     * @return 是否成功
     */
    public static boolean toNext() {
        // 将歌曲位置信息保存到 SharedPreferences
        Log.d(TagConstants.TAG, "toNext()");
        SharedPreferences pref = ContextUtil.getInstance().getSharedPreferences("music_location", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int position = pref.getInt("position", 0) +1;
        MusicList list = MusicList.getInstance(pref.getString("list_name", MusicList.MUSIC_LIST_LOCAL));

        if (position>list.getList().size()-1) {
            return false;
        }
        editor.putInt("position", position);
        editor.commit();
        return true;
    }

    /**
     * 定位歌曲到当前歌曲的前一首
     * @return  是否成功
     */
    public static boolean toPrevious() {
        // 将歌曲位置信息保存到 SharedPreferences
        SharedPreferences pref = ContextUtil.getInstance().getSharedPreferences("music_location", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int position = pref.getInt("position", 0) -1;

        if (position<0) {
            return false;
        }

        editor.putInt("position", position);
        editor.commit();

        Bundle bundle = new Bundle();
        bundle.putBoolean("music_changed", true);
        Message msg = new Message();
        msg.setData(bundle);

        return true;
    }

    /**
     * 定位歌曲到一个随机位置
     */
    public static boolean toRandom() {
        // 将歌曲位置信息保存到 SharedPreferences
        MusicList list = getLocatedList();
        int listSize = list.getList().size();
        int randomPos = (int)(Math.random()*listSize);   // 产生歌曲 的一个随机位置

        if (randomPos>listSize-1 || randomPos<0) return false;
        editor.putInt("position", randomPos);
        editor.commit();
        return true;
    }

}
