package com.andy.music.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.util.Log;

import com.andy.music.data.MusicScanner;
import com.andy.music.data.CursorAdapter;
import com.andy.music.data.MusicDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 音乐列表
 * 注 : 音乐列表的实例只能通过 MusicListFactory.create() 静态方法 或 MusicList.getInstance(Context context, String name)
 * 静态方法获得。当数据库中没有与音乐列表相应的表时通过 MusicListFactory.create() 方法获得，如果数据库中
 * 存在相应的表则可以通过 MusicList.getInstance(Context context, String name) 方法获得。
 * Created by Andy on 2014/11/15.
 */
public class MusicList {

    /**
     * 本地音乐
     */
    public static final String MUSIC_LIST_LOCAL = "list_local";

    /**
     * 最近播放
     */
    public static final String MUSIC_LIST_RECENT = "list_recent";

    /**
     * 我的最爱
     */
    public static final String MUSIC_LIST_FAVORITE = "list_favorite";

    /**
     * 我的下载
     */
    public static final String MUSIC_LIST_DOWNLOAD = "list_down_load";

    private List<Music> list = null;
    private MusicDBHelper musicDBHelper = null;
    private SQLiteDatabase dbWriter = null;
    private SQLiteDatabase dbReader = null;

    private String tabName = null;

    /**
     * 私有构造方法，用于创建自定义列表
     *
     * @param name 音乐列表的名字
     */
    private MusicList(String name) {

        if (name.equals(MUSIC_LIST_LOCAL) ||
                name.equals(MUSIC_LIST_RECENT) ||
                name.equals(MUSIC_LIST_FAVORITE) ||
                name.equals(MUSIC_LIST_DOWNLOAD)) {
            this.tabName = name;
        } else {
            this.tabName = "list_" + name.hashCode();
        }

        this.musicDBHelper = MusicDBHelper.getInstance();
        this.list = new ArrayList<Music>();
        this.list = getList();
    }


    /**
     * 根据音乐列表的名字返回一个MusicList 实例，如果不存在就返回 null
     *
     * @param name 要返回的音乐列表的名字
     * @return 返回一个音乐列表音乐列表的实例
     */
    public static MusicList getInstance(String name) {
        return exist(name) ? new MusicList(name) : null;
    }

    /**
     * 判断列表在数据库中是否存在
     *
     * @param name    列表的名字
     * @return 是否存在
     */
    public static boolean exist(String name) {
        return MusicListName.exsist(name);
    }

    /**
     * 获取音乐列表
     *
     * @return 返回音乐列表 List<Music>
     */
    public List<Music> getList() {
        dbReader = musicDBHelper.getReadableDatabase();
        Cursor cursor = dbReader.query(tabName, null, null, null, null, null, null);

        // 通过CursorHelper.translate() 方法将音乐列表数据库中的 Cursor  转换成查询系统媒体库的 Cursor
        list = MusicScanner.scan(CursorAdapter.translate(cursor));
        dbReader.close();
        return list;
    }

    /**
     * 设置列表，向数据库表中批量添加数据
     *
     * @param cursor 数据来源的游标
     */
    public void setList(Cursor cursor) {

        // 如果表不为空则清空
        if (!isEmpty()) {
            clear();
        }

        dbWriter = musicDBHelper.getWritableDatabase();
        while (cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put("source_id", cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
            dbWriter.insert(tabName, null, values);
        }
        dbWriter.close();

    }

    /**
     * 设置音乐列表，将其存入数据库
     *
     * @param list 本地列表的文件来源
     */
    public void setList(List<Music> list) {

        // 如果表不为空则清空
        if (!isEmpty()) {
            Log.d(TagConstants.TAG, "表为空");
            clear();
        } else {
            Log.d(TagConstants.TAG, "表不为空");
        }

        for (Music item : list) {
            ContentValues values = new ContentValues();
            values.put("source_id", item.getId());
            dbWriter.insert(tabName, null, values);
        }
        dbWriter.close();

    }

    /**
     * 获取音乐列表的列表名
     *
     * @return 返回列表名
     */
    public String getListName() {
        return tabName;
    }

    /**
     * 设置音乐列表的名称
     *
     * @param name 列表的名称
     */
    public void setListName(String name) {
        this.tabName = name;
    }

    /**
     * 向列表中添加音乐文件
     *
     * @param music 要添加的音乐文件
     */
    public void add(Music music) {

        dbWriter = musicDBHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("source_id", music.getId());
            dbWriter.insert(tabName, null, values);
        } catch (Exception e) {
            Log.d(TagConstants.TAG, "添加失败！");
            e.printStackTrace();
        } finally {
            dbWriter.close();
        }

    }

    /**
     * 移除列表中的音乐文件
     *
     * @param id 要移除的文件序号
     */
    public void remove(int id) {
        String whereClause = "_id = " + String.valueOf(id);
        dbWriter = musicDBHelper.getWritableDatabase();
        try {
            dbWriter.delete(tabName, whereClause, null);
        } catch (Exception e) {
            Log.d(TagConstants.TAG, "移除失败！");
            e.printStackTrace();
        } finally {
            dbWriter.close();
        }
    }

    /**
     * 获取歌曲所在的位置
     *
     * @param music 歌曲
     * @return 歌曲在列表中的位置
     */
    public int indexOf(Music music) {
        if (list.contains(music)) {
            Log.d(TagConstants.TAG, "当前歌曲存在");
            return list.indexOf(music);
        }
        Log.d(TagConstants.TAG, "当前歌曲不存在");
        return list.indexOf(music);
    }

    /**
     * 获取指定位置的前一首歌曲
     *
     * @param lastPos 指定位置
     * @return 上一首歌曲
     */
    public Music getPrevious(int lastPos) {
        if (lastPos - 1 > 0) {
            return list.get(lastPos);
        }
        return null;
    }

    /**
     * 获取指定位置的下一首歌曲
     *
     * @param lastPos 指定位置
     * @return 下一首歌曲
     */
    public Music getNext(int lastPos) {
        Log.d(TagConstants.TAG, "列表的大小-->" + list.size());
        if (lastPos + 1 <= list.size()) {
            Log.d(TagConstants.TAG, "lastPos-->" + lastPos);
            return list.get(lastPos + 1);
        }
        return null;
    }

    public Music getRandom() {
        // TODO 随机获取一首歌曲
        return null;
    }

    /**
     * 清空表中的数据
     */
    public void clear() {
        dbWriter = musicDBHelper.getWritableDatabase();
        try {
            dbWriter.delete(tabName, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbWriter.close();
        }
    }

    /**
     * 判断表是否为空
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        dbReader = musicDBHelper.getReadableDatabase();
        Cursor cursor = dbReader.rawQuery("SELECT * FROM " + tabName + " LIMIT 1", null);
        return !cursor.moveToNext();
    }


//    public List<Music> refresh(String name) {
//        List<Music> list = null;
//        dbReader = musicDBHelper.getReadableDatabase();
////        Cursor c = dbReader.query();
//        String selection = context.toString();
//        return null;
//    }

}
