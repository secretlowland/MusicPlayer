package com.andy.music.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.andy.music.R;
import com.andy.music.entity.MusicList;
import com.andy.music.entity.TagConstants;

/**
 * 主界面导航模块
 * Created by Andy on 2014/11/27.
 */
public class NavPanelFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_panel, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化成员变量
        Button toLocalMusic = (Button) view.findViewById(R.id.btn_to_local_music);
        Button toFavorMusic = (Button) view.findViewById(R.id.btn_to_favorite_music);
        Button toRecentMusic = (Button) view.findViewById(R.id.btn_to_recent_music);

        // 设置监听事件
        toLocalMusic.setOnClickListener(this);
        toFavorMusic.setOnClickListener(this);
        toRecentMusic.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        // 模块替换
        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
        MusicListFragment fragment = new MusicListFragment();
        Log.d(TagConstants.TAG, "替换fragment");
        String listName = MusicList.MUSIC_LIST_LOCAL;

        switch (v.getId()) {
            case R.id.btn_to_local_music:
                listName = MusicList.MUSIC_LIST_LOCAL;
                break;
            case R.id.btn_to_recent_music:
                listName = MusicList.MUSIC_LIST_RECENT;
                break;
            case R.id.btn_to_favorite_music:
                listName = MusicList.MUSIC_LIST_FAVORITE;
                break;
            default:
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putString("list_name", listName);
        fragment.setArguments(bundle);
        transaction.replace(R.id.frag_container_nav_panel, fragment);
        transaction.addToBackStack(null);  // 添加到返回栈
        transaction.commit();  // 提交事务

    }
}
