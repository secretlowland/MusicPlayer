package com.andy.music.listener;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

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

        switch (v.getId()) {
            case (R.id.btn_music_play_next):
                if (!MusicLocator.toNext())
                    Toast.makeText(ContextUtil.getInstance(), "已经到最后一首了", Toast.LENGTH_SHORT).show();
                else
                    BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
                break;
            case (R.id.btn_music_play_pre):
                if (!MusicLocator.toPrevious())
                    Toast.makeText(ContextUtil.getInstance(), "已经到第一首了", Toast.LENGTH_SHORT).show();
                else
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
                    if (!MusicPlayService.isPlaying()) {
                        BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_START);
                    }

                } else {
                    // 发送暂停播放的广播
                    if (MusicPlayService.isPlaying()) {
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
