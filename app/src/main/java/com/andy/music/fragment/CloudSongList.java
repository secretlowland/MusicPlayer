package com.andy.music.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);
        showLoadingView (true);
        BmobQuery<Music> query = new BmobQuery<> ();
        query.setLimit (20);
        query.findObjects (getActivity (), new FindListener<Music> () {
            @Override
            public void onSuccess(List<Music> list) {
                updateList (list);
                showLoadingView (false);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText (getActivity (), s, Toast.LENGTH_SHORT).show ();
                showLoadingView (false);
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TopBarFragment topBar = (TopBarFragment)getActivity().getSupportFragmentManager().findFragmentByTag("topBar");
        if (topBar!=null) {
            topBar.setCustomTitle("网络歌曲");
        }
    }


}
