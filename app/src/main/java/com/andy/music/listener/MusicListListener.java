package com.andy.music.listener;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.andy.music.R;
import com.andy.music.entity.MusicList;
import com.andy.music.utility.BroadCastHelper;
import com.andy.music.utility.MusicLocator;

/**
 * 音乐列表监听类
 * Created by Andy on 2014/12/14.
 */
public class MusicListListener implements AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MusicList musicList = MusicList.getInstance(MusicList.MUSIC_LIST_LOCAL);

        TextView name = ((TextView) view.findViewById(R.id.tv_music_name));
        name.setTextColor(Color.parseColor("#ec505e"));

        // 定位当前播放歌曲
        String listName;
        int sourceId;
        if (musicList!=null) {
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
    }
}
