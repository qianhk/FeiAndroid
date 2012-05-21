package com.monkeybrowser;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;

public class HomepageBtnAdapter extends BaseAdapter {
	private int[] mHpDefaultIcon;
	private int[] mHpPressIcon;
	
	ArrayList<ShakeButton> items;

	public HomepageBtnAdapter(Context c, ArrayList<ShakeButton> items) {
		mContext = c;
		this.items = items;
	}


	public void setHpDefaultIcon(int[] iconAarray) {
		mHpDefaultIcon = iconAarray;
	}

	public void setHpPressIcon(int[] iconAarray) {
		mHpPressIcon = iconAarray;
	}

	public int getCount() {
		return mHpDefaultIcon.length;
	}

	public Object getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ShakeButton button;
		if (convertView == null) {
			button = items.get(position);
			// button.setPadding(8, 8, 8, 8);
		} else {
			button = (ShakeButton) convertView;
		}
		// button.setShakeButtonBackGround(mHpDefaultIcon[position]);

		button.setButtonBackGroundDrawable(button.setBackground(
				mHpDefaultIcon[position], mHpPressIcon[position],
				mHpPressIcon[position]));

		button.setShakeAnimation(R.anim.shake_button_anim,
				R.anim.shake_cross_anim);
		

		return button;
	}

	private Context mContext;

}