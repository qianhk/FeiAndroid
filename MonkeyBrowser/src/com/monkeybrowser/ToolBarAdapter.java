package com.monkeybrowser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ToolBarAdapter extends BaseAdapter {

	Context mContext;

	LayoutInflater mLayoutInflater;

	ToolBarItemData mMenuItemData;

	public ToolBarAdapter(Context context, ToolBarItemData menuItemData) {

		mContext = context;

		mLayoutInflater = LayoutInflater.from(context);

		mMenuItemData = menuItemData;
	}

	public void refreshData(ToolBarItemData menuItemData) {
		mMenuItemData = menuItemData;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mMenuItemData.getCount();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.toolbar_item, null);
		}

		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.imageview);
		imageView.setBackgroundDrawable(mMenuItemData.getDrawable(position));

		return convertView;
	}

}
