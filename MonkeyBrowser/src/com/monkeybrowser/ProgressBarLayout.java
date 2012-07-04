package com.monkeybrowser;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class ProgressBarLayout extends LinearLayout {
	MonkeyBrowserActivity mBrowserActivity;

	public ProgressBarLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ProgressBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ProgressBarLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		initView(context);
	}

	public void initView(Context context) {
		LayoutInflater factory = LayoutInflater.from(context);
		factory.inflate(R.layout.progress_bar, this);
	}

}