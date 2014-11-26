package com.andy.music.function;

import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Andy on 2014/11/25.
 */
public class MusicChangedListener {

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            handler.sendMessage(message);
        }
    };
    private OnMusicChangedListener onMusicChangedListener;
    public android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (onMusicChangedListener != null) {
                onMusicChangedListener.onMusicChanged();
            }
            super.handleMessage(msg);
        }
    };
    private Timer timer = new Timer();

    public MusicChangedListener() {
        timer.schedule(task, 1000, 1000);
    }

    public void setOnMusicChangedListener(OnMusicChangedListener e) {
        onMusicChangedListener = e;
    }


    public interface OnMusicChangedListener {
        public void onMusicChanged();
    }


}
