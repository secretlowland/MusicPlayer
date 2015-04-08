package com.andy.music.fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.andy.music.R;
import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicListManager;

/**
 * 主界面导航模块
 * Created by Andy on 2014/11/27.
 */
public class NavPanelFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

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
    public void onResume() {
        ActionBar actionBar = getActivity().getActionBar();
        if(actionBar!=null) {
            actionBar.setTitle("音乐");
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        Log.d(TagConstants.TAG, "NavPanelFragment-->onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {

        // 模块替换
        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        SongListFragment fragment= new SongListFragment();
        String listName = MusicListManager.MUSIC_LIST_LOCAL;
        boolean flag = true;

        switch (v.getId()) {
            case R.id.btn_to_local_music:
                flag = false;
                LocalMusicFragment frag = new LocalMusicFragment();
//                transaction.setCustomAnimations(R.anim.frag_in, R.anim.frag_out, 0, 0);  // 必须在 replace() 等方法之前调用
                transaction.replace(R.id.frag_container_main_content, frag);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.btn_to_recent_music:
                flag = false;
                transaction.commit();
                break;
            case R.id.btn_to_favorite_music:
                flag = false;

                break;
            default:
                break;
        }

        if (flag) {
            Bundle bundle = new Bundle();
            bundle.putString("list_name", listName);
            fragment.setArguments(bundle);
            transaction.replace(R.id.frag_container_main_content, fragment);
            transaction.addToBackStack(null);  // 添加到返回栈
            transaction.commit();  // 提交事务
        }

    }
}
