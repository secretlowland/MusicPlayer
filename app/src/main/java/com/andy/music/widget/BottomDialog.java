package com.andy.music.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.andy.music.util.ScreenInfo;


public class BottomDialog extends Dialog {

	public BottomDialog(Context context) {
		super(context);
		initDialog();
	}

	public BottomDialog(Context context, int theme) {
		super(context, theme);
		initDialog();
		
	}

	private void initDialog() {
		setCanceledOnTouchOutside(true);
		Window window = getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}
	
	


	@Override
	public void setContentView(int layoutResID) {
		LayoutParams params = new LayoutParams(ScreenInfo.getScreenWidth(), LayoutParams.WRAP_CONTENT);
		View view = LayoutInflater.from(getContext()).inflate(layoutResID, null);
		super.setContentView(view, params);
	}

	@Override
	public void setContentView(View view) {
		LayoutParams params = new LayoutParams(ScreenInfo.getScreenWidth(), LayoutParams.WRAP_CONTENT);
		super.setContentView(view, params);
	}
	
	
	

}
