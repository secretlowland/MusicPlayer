//package com.andy.music.abandoned;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.andy.music.R;
//import com.andy.music.entity.Music;
//import com.andy.music.fragment.BaseSongList;
//import com.andy.music.function.MusicListManager;
//
//import java.util.List;
//
///**
// * Created by Andy on 2015/4/28.
// */
//public class LocalSongList extends BaseSongList {
//
//    private View mainView;
//
//    @Override
//    List<Music> getList() {
//        MusicListManager manager = MusicListManager.getInstance(MusicListManager.MUSIC_LIST_LOCAL);
//        List<Music> list = manager.getList();
//        return list;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        mainView = inflater.inflate(R.layout.fragment_list_song, (ViewGroup)getActivity().findViewById(R.id.view_pager_local_music), false);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
//        // 移除已存在的 view
//        ViewGroup group = ((ViewGroup) mainView.getParent());
//        if (group!=null) {
//            group.removeAllViewsInLayout();
//        }
//        return mainView;
//    }
//}
