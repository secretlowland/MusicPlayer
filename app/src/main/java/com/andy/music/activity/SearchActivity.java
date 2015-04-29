package com.andy.music.activity;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andy.music.R;
import com.andy.music.entity.TagConstants;
import com.andy.music.fragment.LocalSongList;
import com.andy.music.fragment.SearchSongList;
import com.andy.music.fragment.SongListFragment;
import com.andy.music.fragment.TopBarFragment;

/**
* 本地音乐查找视图
* Created by Andy on 2014/12/11.
*/
public class SearchActivity extends FragmentActivity {

    private EditText searchContent;
    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        TopBarFragment topBarFragment = new TopBarFragment();
//        topBarFragment.setCustomTitle("搜索");
//        topBarFragment.hideSearchIcon();
        transaction.add(R.id.ll_top_bar_container, new TopBarFragment(), "topBar");
        transaction.add(R.id.frag_container_search_list, new LocalSongList(), "songList");
        transaction.commit();

        // 初始化变量
        searchContent = (EditText) this.findViewById(R.id.et_search_box);
        searchContent.addTextChangedListener(new EditChangedListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        TopBarFragment topBar = (TopBarFragment)getSupportFragmentManager().findFragmentByTag("topBar");
        if (topBar!=null) {
            topBar.setCustomTitle("搜索");
            topBar.hideSearchIcon();
        }
    }

    /**
     * 监听键盘输入状态
     */
    class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SearchSongList fragment = new SearchSongList();
            Bundle bundle = new Bundle();
            String[] args = {s.toString()};
            bundle.putStringArray("selection_args", args);
            fragment.setArguments(bundle);
            transaction.replace(R.id.frag_container_search_list, fragment);
            transaction.commit();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
