package com.andy.music.listener;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.andy.music.R;
import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicPlayService;
import com.andy.music.utility.BroadCastHelper;
import com.andy.music.utility.ContextUtil;
import com.andy.music.utility.MusicLocator;

/**
 * 监听音乐播放器的播放，暂停，下一首等操作
 * Created by Andy on 2014/11/23.
 */
public class MusicPlayListener implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        AdapterView.OnItemClickListener{

    @Override
    public void onClick(View v) {

        // 读取播放模式
        SharedPreferences pref = ContextUtil.getInstance().getSharedPreferences("play_setting", Context.MODE_PRIVATE);
        int playSchema = pref.getInt("play_schema", MusicPlayService.MUSIC_PLAY_SCHEMA_ORDER);

        switch (v.getId()) {
            case (R.id.btn_music_play_next):
                // 定位到下一首
                if (playSchema!=MusicPlayService.MUSIC_PLAY_SCHEMA_RANDOM) {
                    MusicLocator.toNext();
                } else {
                    MusicLocator.toRandom();
                }
                // 发送播放下一首的广播
                BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
                break;
            case (R.id.btn_music_play_pre):
                // 定位到上一首
                if (playSchema!=MusicPlayService.MUSIC_PLAY_SCHEMA_RANDOM) {
                    MusicLocator.toPrevious();
                } else {
                    MusicLocator.toRandom();
                }
                // 发送播放上一首的广播
                BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tb_music_play_toggle:
                if (isChecked) {
                    // 发送开始播放的广播
                    if (!MusicPlayService.isPlaying) {
                        BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_START);
                    }

                } else {
                    // 发送暂停播放的广播
                    if (MusicPlayService.isPlaying) {
                        BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PAUSE);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
