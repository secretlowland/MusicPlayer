package com.andy.music.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andy.music.R;
import com.andy.music.entity.TagConstants;

/**
 * Created by Andy on 2014/12/15.
 */
public class TestFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TagConstants.TAG, "TestFragment-->onCreateView()");
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TagConstants.TAG, "TestFragment-->onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TagConstants.TAG, "TestFragment-->onStart()");
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        Log.d(TagConstants.TAG, "TestFragment-->onDestroyView()");
        super.onDestroyView();
    }
}
