package com.andy.music.function;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.andy.music.R;
import com.andy.music.adapter.MusicListAdapter;
import com.andy.music.entity.MusicList;
import com.andy.music.utility.BroadCastHelper;
import com.andy.music.utility.MusicLocator;

/**
 * Created by Andy on 2014/12/13.
 */
public class MusicListManager implements AdapterView.OnItemClickListener {

    private ListView listView;
    private MusicList musicList;

    public View getListView(Context context) {
        initView(context);
        return listView;
    }

    public void initView(Context context) {
        musicList = MusicList.getInstance(MusicList.MUSIC_LIST_LOCAL);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.fragment_music_list, null);
        listView = (ListView) layout.findViewById(R.id.lv_music_list);
        MusicListAdapter adapter = new MusicListAdapter(context, MusicList.getInstance(MusicList.MUSIC_LIST_LOCAL).getList(), R.layout.music_list_cell);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView name = ((TextView) view.findViewById(R.id.tv_music_name));
        name.setTextColor(Color.parseColor("#ec505e"));

        // 定位当前播放歌曲
        String listName;
        int sourceId;
        if (musicList != null) {
            listName = musicList.getListName();
            sourceId = musicList.getList().get(position).getId();
        } else {
            listName = MusicLocator.getLocatedList().getListName();
            sourceId = MusicLocator.getSourceId();
        }
        Bundle bundle = new Bundle();
        bundle.putString("list_name", listName);
        bundle.putInt("position", position);
        bundle.putInt("source_id", sourceId);
        MusicLocator.setLocation(bundle);

        // 发送播放音乐的广播
        BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY);

        // 更新列表
        refreshMusicList();
    }

    /**
     * 刷新列表。当前歌曲改变的时候刷新
     */
    private void refreshMusicList() {

        for (int i = 0; i < listView.getChildCount(); i++) {

            // 遍历当前的可见部分的 View
            View view = listView.getChildAt(i);

            // 设置不是当前播放的歌曲的样式
            view.setBackgroundColor(Color.parseColor("#00000000"));
            view.findViewById(R.id.v_locator_bar).setBackgroundColor(Color.parseColor("#00000000"));
            ((TextView) view.findViewById(R.id.tv_music_name)).setTextColor(Color.parseColor("#ccffffff"));
            ((TextView) view.findViewById(R.id.tv_music_singer)).setTextColor(Color.parseColor("#78ffffff"));
            ((TextView) view.findViewById(R.id.tv_music_number)).setTextColor(Color.parseColor("#78ffffff"));

            // 当前歌曲的序号
            String musicNum = ((TextView) view.findViewById(R.id.tv_music_number)).getText().toString();

            // 设置当前歌曲的样式
            if (Integer.parseInt(musicNum) == MusicLocator.getPosition() + 1) {
                view.setBackgroundColor(Color.parseColor("#34000000"));
                view.findViewById(R.id.v_locator_bar).setBackgroundColor(Color.parseColor("#ec505e"));
                ((TextView) view.findViewById(R.id.tv_music_name)).setTextColor(Color.parseColor("#ec505e"));
                ((TextView) view.findViewById(R.id.tv_music_singer)).setTextColor(Color.parseColor("#ec505e"));
                ((TextView) view.findViewById(R.id.tv_music_number)).setTextColor(Color.parseColor("#ec505e"));
            }
        }

    }
}
