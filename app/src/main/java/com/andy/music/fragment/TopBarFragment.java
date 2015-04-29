package com.andy.music.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.andy.music.R;
import com.andy.music.activity.MainActivity;
import com.andy.music.activity.SearchActivity;

import org.w3c.dom.Text;

/**
 * 标题栏模块
 * Created by Andy on 2014/12/15.
 */
public class TopBarFragment extends Fragment {

    ImageButton searchBtn, backBtn;
    TextView title;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_bar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        this.searchBtn.setVisibility(View.GONE);
    }

    public void hideBackIcon() {
        this.backBtn.setVisibility(View.GONE);
    }


}
