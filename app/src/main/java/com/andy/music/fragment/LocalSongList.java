package com.andy.music.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;

import com.andy.music.R;
import com.andy.music.data.CursorAdapter;
import com.andy.music.data.MusicScanner;
import com.andy.music.entity.Music;

import java.util.List;

/**
 * 歌曲列表模块
 * Created by Andy on 2014/12/17.
 */
public class LocalSongList extends BaseSongList {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);

        showLoadingView (true);
        // 异步查询
        Handler handler = new Handler ();
        handler.postDelayed (new Runnable () {
            @Override
            public void run() {
                // 获取传递进来的内容（查询语句）
                Bundle bundle = getArguments();
                String selection = null;
                String selectionArgs[] = null;
                if (bundle != null) {
                    selection = bundle.getString("selection");
                    selectionArgs = bundle.getStringArray("selection_args");
                }
                Cursor searchCursor = CursorAdapter.get(selection, selectionArgs);
                if (searchCursor != null) {
                    List<Music> musicList = MusicScanner.scan(searchCursor);
                    updateList (musicList);
                    searchCursor.close();
                    showLoadingView (false);
                }
            }
        }, 0);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView)view.findViewById(R.id.lv_list_common);
        DisplayMetrics res = getResources().getDisplayMetrics();
        int paddingTopDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, res);
        int paddingBottomDp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, res);
        listView.setPadding(0, paddingTopDp, 0, paddingBottomDp);
    }

}
