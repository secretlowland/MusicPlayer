package com.andy.music.function;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.andy.music.R;

/**
 * 监听音乐播放器的播放，暂停，下一首等操作
 * Created by Andy on 2014/11/23.
 */
public class MusicPlayListener implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        AdapterView.OnItemClickListener{

    /**
     * 获取从客户端传来的 MusicPlayService，只有获得了 MusicPlayService 才能调用它的方法
     */
    private MusicPlayService musicPlayService;

    public MusicPlayListener(MusicPlayService service) {
        // 获取从客户端传来的 MusicPlayService
        this.musicPlayService = service;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btn_music_play_pre):
                // 播放上一首
                musicPlayService.playPrevious();
                break;
            case (R.id.btn_music_play_next):
                // 播放下一首
                musicPlayService.playNext();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tb_music_play_toggle:
                if (isChecked) {
                    musicPlayService.start();
                } else {
                    musicPlayService.pause();
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
