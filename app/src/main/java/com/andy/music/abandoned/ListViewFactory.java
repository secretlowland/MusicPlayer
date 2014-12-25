//package com.andy.music.function;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.provider.MediaStore;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.SimpleCursorAdapter;
//
//import com.andy.music.R;
//import com.andy.music.adapter.MusicListAdapter;
//import com.andy.music.utility.ContextUtil;
//
///**
// * 列表视图制造工厂
// * 对列表视图进行一些列操作，如设置适配器，设置监听器等
// * Created by Andy on 2014/12/14.
// */
//public class ListViewFactory {
//
//    public static final int LIST_VIEW_MUSIC = 0;
//    public static final int LIST_VIEW_SINGER = 1;
//    public static final int LIST_VIEW_ALBUM = 2;
//
//
//    private static ListView listView;
//    private static BaseAdapter adapter;
//    private static Context context = ContextUtil.getInstance();
//
//    public static View createView(int viewType) {
//
//        // 实例化 listView
//        LayoutInflater inflater = LayoutInflater.from(ContextUtil.getInstance());
//        listView =(ListView) inflater.inflate(R.layout.fragment_list_common, null).findViewById(R.id.lv_list_common);
//
//        // 设置适配器参数
//        Cursor cursor = com.andy.music.data.CursorAdapter.getMediaLibCursor();    // 获取查询游标
//        String[] from = { MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM} ;
//        int[] to = { R.id.tv_list_cell_double_line_first, R.id.tv_list_cell_double_line_second };
//
//        switch (viewType) {
//            case LIST_VIEW_MUSIC:
//                listView.setOnItemClickListener(new MusicListListener());
//                adapter = new MusicListAdapter(context, MusicListManager.getInstance(MusicListManager.MUSIC_LIST_LOCAL).getList(), R.layout.music_list_cell);
//                break;
//            case LIST_VIEW_SINGER:
//                adapter = new SimpleCursorAdapter(context, R.layout.list_cell_double_line, cursor, from, to, 0);
//                break;
//            case LIST_VIEW_ALBUM:
//                break;
//            default: break;
//        }
//        listView.setAdapter(adapter);
//
//        return listView;
//    }
//
//}
