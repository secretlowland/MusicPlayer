package com.andy.music.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.music.R;
import com.andy.music.entity.Music;
import com.andy.music.entity.MusicList;
import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicListAdapter;
import com.andy.music.function.MusicPlayService;
import com.andy.music.utility.BroadCastHelper;
import com.andy.music.utility.MusicLocator;

import org.w3c.dom.Text;

/**
 * 音乐列表模块
 * Created by Andy on 2014/11/28.
 */
public class MusicListFragment extends Fragment implements AdapterView.OnItemClickListener{

    /**
     * 视图中显示的音乐列表
     */
    private MusicList musicList;

    /**
     * Activity 中用于显示音乐列表的 ListView
     */
    private ListView musicListView;

    private MusicListAdapter adapter;
    private TextView title;

    private Receiver receiver;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshMusicList();   // 刷新列表
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // 获取从 Activity 传递过来的信息
        Bundle bundle = getArguments();
        String listName = bundle.getString("list_name");

        // 初始化变量
        musicListView = (ListView) getActivity().findViewById(R.id.lv_music_list);
        musicList = MusicList.getInstance(listName);
//        title = (TextView) getActivity().findViewById(R.id.tv_title);

//        title.setText("本地列表");

        // 加载音乐列表
        if (musicList != null && !musicList.isEmpty()) {
            adapter = new MusicListAdapter(getActivity(), musicList.getList(), R.layout.music_list_cell);
            musicListView.setAdapter(adapter);
        } else {          // 列表为空
            Toast.makeText(getActivity(), "一首歌曲都没有呢~~", Toast.LENGTH_SHORT).show();
        }

        // 设置监听事件
        musicListView.setOnItemClickListener(this);

        // 注册广播接收
        registerReceiver();

    }

    /**
     * 刷新列表。当前歌曲改变的时候刷新
     */
    private void refreshMusicList() {

        for (int i=0; i<musicListView.getChildCount(); i++) {

            // 遍历当前的可见部分的 View
            View view = musicListView.getChildAt(i);

            // 设置不是当前播放的歌曲的样式
            view.setBackgroundColor(Color.parseColor("#00000000"));
            view.findViewById(R.id.v_locator_bar).setBackgroundColor(Color.parseColor("#00000000"));
            ((TextView) view.findViewById(R.id.tv_music_name)).setTextColor(Color.parseColor("#ccffffff"));
            ((TextView) view.findViewById(R.id.tv_music_singer)).setTextColor(Color.parseColor("#78ffffff"));
            ((TextView) view.findViewById(R.id.tv_music_number)).setTextColor(Color.parseColor("#78ffffff"));

            // 当前歌曲的序号
            String  musicNum = ((TextView) view.findViewById(R.id.tv_music_number)).getText().toString();

            // 设置当前歌曲的样式
            if (Integer.parseInt(musicNum)==MusicLocator.getPosition()+1) {
                view.setBackgroundColor(Color.parseColor("#34000000"));
                view.findViewById(R.id.v_locator_bar).setBackgroundColor(Color.parseColor("#ec505e"));
                ((TextView) view.findViewById(R.id.tv_music_name)).setTextColor(Color.parseColor("#ec505e"));
                ((TextView) view.findViewById(R.id.tv_music_singer)).setTextColor(Color.parseColor("#ec505e"));
                ((TextView) view.findViewById(R.id.tv_music_number)).setTextColor(Color.parseColor("#ec505e"));
            }
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView name = ((TextView) view.findViewById(R.id.tv_music_name));
        name.setTextColor(Color.parseColor("#ec505e"));

        // 定位当前播放歌曲
        Bundle bundle = new Bundle();
        bundle.putString("list_name", musicList.getListName());
        bundle.putInt("position", position);
        bundle.putInt("source_id", musicList.getList().get(position).getId());
        MusicLocator.setLocation(bundle);

        // 发送播放音乐的广播
        BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY);

        // 更新列表
        refreshMusicList();
    }

    /**
     * 动态注册广播接收
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.setPriority(-1000);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM);
        receiver = new Receiver();
        getActivity().registerReceiver(receiver, filter);
    }

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT)) {
                refreshMusicList();
            } else if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM)) {
                refreshMusicList();
            }
        }
    }

}
