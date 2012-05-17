package com.monkeybrowser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter{
	
	Context mContext;
	
	LayoutInflater mLayoutInflater;
	
	MenuItemData 	mMenuItemData;
	
	public MenuAdapter(Context context, MenuItemData menuItemData)
	{

		mContext = context;
		
		mLayoutInflater = LayoutInflater.from(context);
		
		mMenuItemData = menuItemData;
	}
	
	public void refreshData(MenuItemData menuItemData)
	{
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

		if (convertView == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.menu_item, null);
		}
		
		ImageView imageView = (ImageView) convertView.findViewById(R.id.imageview);	
		imageView.setBackgroundDrawable(mMenuItemData.getDrawable(position));

		
		TextView  textView = (TextView) convertView.findViewById(R.id.textview);
		textView.setText(mMenuItemData.getTitle(position));
		
		
		return convertView;
		
	}

}
