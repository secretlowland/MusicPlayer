//package com.andy.music.listener;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.TextView;
//
//import com.andy.music.R;
//import com.andy.music.function.MusicListManager;
//import com.andy.music.utility.BroadCastHelper;
//import com.andy.music.utility.MusicLocator;
//
///**
// * 音乐列表监听类
// * Created by Andy on 2014/12/14.
// */
//public class MusicListListener implements AdapterView.OnItemClickListener {
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//        MusicListManager musicListManager = MusicListManager.getInstance(MusicListManager.MUSIC_LIST_LOCAL);
//
//        TextView name = ((TextView) view.findViewById(R.id.tv_music_name));
//        name.setTextColor(Color.parseColor("#ec505e"));
//
//        // 发送播放音乐的广播
//        BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY);
//
//        // 更新列表
//    }
//}
