package com.andy.music.fragment;

import android.widget.Toast;

import com.andy.music.entity.Music;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by andy on 2016/4/16.
 */
public class CloudSongList extends BaseSongList {


    @Override
    List<Music> getList() {
        final List<Music> data;
        BmobQuery<Music> query = new BmobQuery<> ();
        query.setLimit (20);
        query.findObjects (getActivity (), new FindListener<Music> () {
            @Override
            public void onSuccess(List<Music> list) {
                Toast.makeText (getActivity (), "获取网络歌曲成功", Toast.LENGTH_SHORT).show ();
                updateData (list);

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText (getActivity (), s, Toast.LENGTH_SHORT).show ();
            }
        });
        return null;
    }
}
