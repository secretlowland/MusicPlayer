package com.andy.music.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.music.R;
import com.andy.music.activity.SearchActivity;
import com.andy.music.util.ScreenInfo;

/**
 * 标题栏模块
 * Created by Andy on 2014/12/15.
 */
public class TopBarFragment extends Fragment {

    ImageButton searchBtn, backBtn;
    TextView title;
    LinearLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout)inflater.inflate(R.layout.fragment_top_bar, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusHeight = ScreenInfo.getStatusHeight();
            View v = new View(getActivity());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
            v.setLayoutParams(layoutParams);
            layout.addView(v, 0);
        }

        searchBtn = (ImageButton)getActivity().findViewById(R.id.ib_top_bar_search_btn);
        backBtn = (ImageButton)getActivity().findViewById(R.id.ib_top_bar_back_btn);
        title = (TextView)getActivity().findViewById(R.id.tv_top_bar_title);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    /**
     * 设置标题
     * @param title 标题
     */
    public void setCustomTitle(String title) {
        this.title.setText(title);
    }

    /**
     * 隐藏搜索按钮
     */
    public void hideSearchIcon() {
        this.searchBtn.setVisibility(View.INVISIBLE);
    }

    public void hideBackIcon() {
        this.backBtn.setVisibility(View.INVISIBLE);
    }


}
