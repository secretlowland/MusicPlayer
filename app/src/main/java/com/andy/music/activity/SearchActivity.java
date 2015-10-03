package com.andy.music.activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.andy.music.R;
import com.andy.music.fragment.LocalSongList;
import com.andy.music.fragment.SearchSongList;
import com.andy.music.fragment.TopBarFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;

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

        // 设置沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);   //透明导航栏
            //  改变状态栏颜色
            //  创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            //  给状态栏设置颜色
            tintManager.setStatusBarTintColor(0xee729939);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        TopBarFragment topBarFragment = new TopBarFragment();
        transaction.add(R.id.ll_top_bar_container, new TopBarFragment(), "topBar");
        transaction.add(R.id.frag_container_search_list, new SearchSongList(), "songList");
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
