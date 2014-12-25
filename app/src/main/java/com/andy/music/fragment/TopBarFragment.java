package com.andy.music.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andy.music.R;
import com.andy.music.view.TitleBar;

/**
 * 标题模块
 * Created by Andy on 2014/11/28.
 */
public class TopBarFragment extends Fragment implements TitleBar.OnButtonClickListener{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_title_bar, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TitleBar titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        titleBar.setOnButtonClickListener(this);
    }

    @Override
    public void onTitleButtonClick() {
        getActivity().onBackPressed();
    }

    @Override
    public void onMenuButtonClick() {

    }
}
