package com.andy.music.data;

import android.database.Cursor;
import android.provider.MediaStore;

import com.andy.music.data.CursorAdapter;
import com.andy.music.entity.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * 扫描本地音乐
 * Created by Andy on 2014/11/15.
 */
public class MusicScanner {

    /**
     * query()方法参数说明 :
     * public final Cursor query (Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
     * <p/>
     * uri(Uri)--> The URI, using the content:// scheme, for the content to retrieve.
     * <p/>
     * projection(String[])--> A list of which columns to return. Passing null will return all columns, which is inefficient.
     * <p/>
     * selection(String)--> A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself)
     * Passing null will return all rows for the given URI.
     * <p/>
     * selectionArgs(String[])--> You may include ?s in selection, which will be replaced by the values from selectionArgs,
     * in the order that they appear in the selection. The values will be bound as Strings.
     * <p/>
     * sortOrder(String)--> How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself).
     * Passing null will use the default sort order, which may be unordered.
     */

    public static Music scan(int id) {
        Cursor cursor = CursorAdapter.get(id);
        List<Music> list = scan(cursor);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public static List<Music> scan(Cursor cursor) {

        List<Music> list = new ArrayList<Music>();

        while (cursor != null && cursor.moveToNext()) {
            Music music = new Music();

            // 设置歌曲ID
            music.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));

            // 设置歌曲总时间
            music.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));

            // 设置歌曲名称
            music.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));

            // 设置歌手
            music.setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));

            // 设置歌曲路径
            music.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
            list.add(music);
        }
        return list;
    }

}
