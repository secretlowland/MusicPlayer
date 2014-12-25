package com.andy.music.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.andy.music.R;
import com.andy.music.fragment.MusicListFragment;

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

        // 初始化变量
        searchContent = (EditText) this.findViewById(R.id.et_search_box);
        searchBtn = (Button) this.findViewById(R.id.btn_search);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 添加列表模块
                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                MusicListFragment fragment = new MusicListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("search_content", searchContent.getText().toString());
                fragment.setArguments(bundle);
                transaction.add(R.id.frag_container_search_list, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
