package com.monkeybrowser;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class AddressBar extends LinearLayout {
	MonkeyBrowserActivity mBrowserActivity;

	public AddressBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public AddressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public AddressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		initView(context);
	}

	public void initView(Context context) {
		LayoutInflater factory = LayoutInflater.from(context);
		factory.inflate(R.layout.address_bar, this);
	}

}