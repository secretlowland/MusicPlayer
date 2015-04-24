//package com.andy.music.abandoned;
//
//import android.app.ActionBar;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.andy.music.R;
//import com.andy.music.fragment.SongListFragment;
//
///**
// * 本地音乐查找视图
// * Created by Andy on 2014/12/11.
// */
//public class SearchActivity extends FragmentActivity {
//
//    private EditText searchContent;
//    private Button searchBtn;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//
//        ActionBar actionbar = getActionBar();
//        // 去除Actionbar 图标
//        if (actionbar!=null) {
//            actionbar.setTitle("搜索");
//            actionbar.setDisplayShowHomeEnabled(true);  // 是否显示图标 默认true
//            actionbar.setDisplayShowTitleEnabled(true);  // 是否显示标题 默认true
//            actionbar.setHomeButtonEnabled(true); // 应用图标是否可点击（返回）默认false
//            actionbar.setDisplayHomeAsUpEnabled(true);
//        }
//
//        // 初始化变量
//        searchContent = (EditText) this.findViewById(R.id.et_search_box);
//        searchBtn = (Button) this.findViewById(R.id.btn_search);
//
//        searchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 添加列表模块
//                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                SongListFragment fragment = new SongListFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("search_content", searchContent.getText().toString());
//                fragment.setArguments(bundle);
//                transaction.add(R.id.frag_container_search_list, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });
//    }
//}
