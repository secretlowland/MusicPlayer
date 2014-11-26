package com.andy.music.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.andy.music.data.MusicDBHelper;

/**
 * 存储音乐列表的名称
 * Created by Andy on 2014/11/16.
 */
public class MusicListName {

    /**
     *  在数据库中的 _list表中添加一个要新增的音乐表的名字
     * @param name  音乐列表的名字
     * @param tabName  音乐列表在数据库中的表名
     */
    public static void add(String name, String tabName) {
        SQLiteDatabase dbWriter = MusicDBHelper.getInstance().getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("tab_name", tabName);
        cv.put("hash_code", name.hashCode());
        dbWriter.insert("_list", null, cv);
    }



    /**
     * 判断表在数据库中是否存在
     *
     * @param name 表的名字
     * @return 是否存在
     */
    public static boolean exsist(String name) {
        SQLiteDatabase dbReader = MusicDBHelper.getInstance().getReadableDatabase();
        Cursor cursor = dbReader.query("_list", null, null, null, null, null, null);
        boolean flag = false;
        while (cursor.moveToNext()) {
            if (name.hashCode() == cursor.getInt(cursor.getColumnIndexOrThrow("hash_code"))) {
                flag = true;
            }
        }
        return flag;
    }

}
