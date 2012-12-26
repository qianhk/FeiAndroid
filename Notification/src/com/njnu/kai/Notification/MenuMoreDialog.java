/**
 * @(#)MenuMoreDialog.java		2012-12-26
 * Copyright (c) 2007-2012 Shanghai ShuiDuShi Co.Ltd. All right reserved.
 *
 */
package com.njnu.kai.Notification;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


public class MenuMoreDialog extends Dialog {
	private static final String LOG_TAG = "MenuMoreDialog";

	public MenuMoreDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.lyric_pics_panel, null);
		setContentView(view);
	}

	public MenuMoreDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	public MenuMoreDialog(Context context) {
		super(context);
		init();

	}

}
