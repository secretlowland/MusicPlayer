package com.andy.music.function;

import com.andy.music.data.MusicDBHelper;
import com.andy.music.entity.*;

/**
 * 音乐列表制造工厂
 * 例如产生一个本地音乐列表，我的最爱，最近播放，我的下载以及自定义列表
 * Created by Andy on 2014/11/15.
 */
public class MusicListFactory {

    /**
     * 在数据库中建立一个音乐表     *
     * @param name 要建立的表的名称
     */
    public static MusicListManager create(String name) {

        String tabName;
        if (name.equals(MusicListManager.MUSIC_LIST_LOCAL) ||
                name.equals(MusicListManager.MUSIC_LIST_CURRENT) ||
                name.equals(MusicListManager.MUSIC_LIST_RECENT) ||
                name.equals(MusicListManager.MUSIC_LIST_FAVORITE) ||
                name.equals(MusicListManager.MUSIC_LIST_DOWNLOAD)) {
            tabName = name;
        } else {
            tabName = "list_custom_" + name.hashCode();
        }

        if (!MusicListManager.exist(tabName)) {  // 表不存在
            String sql = "CREATE TABLE IF NOT EXISTS " + tabName + " (" +
                    "_id INTEGER PRIMARY KEY," +
                    "source_id INTEGER)";
            MusicDBHelper musicDBHelper = MusicDBHelper.getInstance();
            MusicDBHelper.createTable(musicDBHelper.getWritableDatabase(), sql);
            MusicListName.add(name, tabName);
        }

        return MusicListManager.getInstance(name);

    }


}
