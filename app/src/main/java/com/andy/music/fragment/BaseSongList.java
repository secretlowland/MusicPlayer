package com.andy.music.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.andy.music.R;
import com.andy.music.adapter.MusicListAdapter;
import com.andy.music.entity.Music;
import com.andy.music.utility.BroadCastHelper;
import com.andy.music.utility.MusicLocator;

import java.util.List;

/**
 * Created by Andy on 2015/1/11.
 */
public abstract class BaseSongList extends ListFragment {

    private ListView listView;
    private Cursor searchCursor;
    private List<Music> musicList;

    abstract List<Music> getList();

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 获取 ListView 对象
        listView = (ListView) getActivity().findViewById(R.id.lv_song);

        // 为 ListView 对象设置适配器
        listView.setAdapter(getAdapter());

        // 为 ListView 设置监听器
        listView.setOnItemClickListener(getOnItemClickListener());

        // 动态注册广播接收器，用于接收播放下一首，上一首等广播
        registerReceiver();
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshMusicList(listView);
    }
    @Override
    public BaseAdapter getAdapter() {
        return new MusicListAdapter(getActivity().getApplicationContext(), getList(), R.layout.song_list_cell);
    }

    @Override
    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 定位当前歌曲列表和位置
                MusicLocator.setCurrentMusicList(getList());
                MusicLocator.setCurrentPosition(position);

                // 发送播放歌曲的广播
                BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY);

            }
        };
    }

    /**
     * 刷新列表。当前歌曲改变的时候刷新
     */
    private void refreshMusicList(ListView listView) {

        // 遍历当前的可见部分的 View
        for (int i = 0; i < listView.getChildCount(); i++) {

            // 获取 listView 中的第 i 项
            View view = listView.getChildAt(i);

            // 设置不是当前播放的歌曲的样式
            view.setBackgroundColor(Color.parseColor("#00000000"));
            view.findViewById(R.id.v_locator_bar).setBackgroundColor(Color.parseColor("#00000000"));
            ((TextView) view.findViewById(R.id.tv_music_name)).setTextColor(Color.parseColor("#cc000000"));
            ((TextView) view.findViewById(R.id.tv_music_singer)).setTextColor(Color.parseColor("#78000000"));
            ((TextView) view.findViewById(R.id.tv_music_number)).setTextColor(Color.parseColor("#78000000"));

            // 当前歌曲的名字和歌手
            String musicName = ((TextView) view.findViewById(R.id.tv_music_name)).getText().toString();
            String musicSinger = ((TextView) view.findViewById(R.id.tv_music_singer)).getText().toString();

            // 设置当前歌曲的样式
            Music curMusic = MusicLocator.getCurrentMusic();
            if (curMusic!=null && curMusic.getName().equals(musicName) &&
                    curMusic.getSinger().equals(musicSinger)) {
                view.setBackgroundColor(Color.parseColor("#c4d9c6"));
                view.findViewById(R.id.v_locator_bar).setBackgroundColor(Color.parseColor("#729939"));
                ((TextView) view.findViewById(R.id.tv_music_name)).setTextColor(Color.parseColor("#729939"));
                ((TextView) view.findViewById(R.id.tv_music_singer)).setTextColor(Color.parseColor("#729939"));
                ((TextView) view.findViewById(R.id.tv_music_number)).setTextColor(Color.parseColor("#729939"));
            }
        }

    }

    /**
     * 动态注册广播接收
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.setPriority(-1000);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM);
        Receiver receiver = new Receiver();
        getActivity().registerReceiver(receiver, filter);
    }

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY) ||
                    action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT) ||
                    action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS) ||
                    action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM)) {
                refreshMusicList(listView);    // 刷新列表
            }

        }
    }
}
