package com.andy.music.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.andy.music.utility.ContextUtil;

/**
 * 数据库辅助类
 * Created by Andy on 2014/11/16.
 */
public class MusicDBHelper extends SQLiteOpenHelper {

    private static final String CREATE_LIST = "CREATE TABLE IF NOT EXISTS _list (" +
            "_id INTEGER PRIMARY KEY, " +
            "hash_code INTEGER," +
            "name TEXT DEFAULT list_custom," +
            "tab_name)";

    private static MusicDBHelper musicDBHelper;
    private MusicDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 获取一个数据库辅助类的实例
     * @return 返回一个数据库辅助类的实例
     */
    public static MusicDBHelper getInstance() {
        if (musicDBHelper!=null) return musicDBHelper;
        return new MusicDBHelper(ContextUtil.getInstance(), "music", null, 1);
    }


    /**
     * 在数据库中建立一个表
     * @param db  建立的表所在的数据库
     * @param sql 建立表的sql语句
     */
    public static void createTable(SQLiteDatabase db, String sql) {
        db.execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建立一张用于存放音乐列表名称的表
        createTable(db, CREATE_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
