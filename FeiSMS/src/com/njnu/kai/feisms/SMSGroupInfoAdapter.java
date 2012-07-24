package com.njnu.kai.feisms;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

public class SMSGroupInfoAdapter extends SimpleCursorAdapter {
	private static final String PREFIX = "[SMSGroupInfoAdapter]:";

	/**
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return 0;
	}

	/**
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		return null;
	}

	/**
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
