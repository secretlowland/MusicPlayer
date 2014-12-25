package com.andy.music.function;

import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.andy.music.R;
import com.andy.music.entity.TagConstants;
import com.andy.music.utility.BroadCastHelper;
import com.andy.music.utility.ContextUtil;

/**
 * 监听音乐播放器的播放，暂停，下一首等操作
 * Created by Andy on 2014/11/23.
 */
public class MusicPlayListener implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        AdapterView.OnItemClickListener{

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btn_music_play_pre):
                // 发送播放上一首的广播
                BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS);
                break;
            case (R.id.btn_music_play_next):
                // 发送播放下一首的广播
                BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tb_music_play_toggle:
                if (isChecked) {
                    // 发送开始播放的广播
                    BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_START);
                    // 将播放状态存储到 SharedPreferences
                    SharedPreferences pref = ContextUtil.getInstance().getSharedPreferences("music_play_status", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("playing", true);
                    editor.commit();
                } else {
                    // 发送暂停播放的广播
                    BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PAUSE);
                    // 将播放状态存储到 SharedPreferences
                    SharedPreferences pref = ContextUtil.getInstance().getSharedPreferences("music_play_status", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("playing", false);
                    editor.commit();
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
