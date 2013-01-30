package com.njnu.kai.letterview;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyUserInfoAdapter extends BaseAdapter {

	private Context context;

	private List<UserInfo> userInfos;

	public MyUserInfoAdapter(Context context, List<UserInfo> userInfos) {

		this.context = context;
		this.userInfos = userInfos;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userInfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// View view = convertView;
		View view = convertView;

		ViewHolder holder = null;
		if (view == null) {

			view = LayoutInflater.from(context).inflate(R.layout.list_item,
					null);

			holder = new ViewHolder();
			view.setTag(holder);

			holder.tv_catalog = (TextView) view.findViewById(R.id.tv_catalog);
			holder.tv_nick = (TextView) view.findViewById(R.id.tv_nick);
			holder.tv_mobile = (TextView) view.findViewById(R.id.tv_mobile);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

//		Log.i("coder", "" + userInfos.get(position));
//		if (holder == null) {
//			Log.i("coder", "--holder is null--");
//		}
//		if (holder.tv_nick == null) {
//			Log.i("coder", "--holder tv_nick is null--");
//		}
//		if (userInfos.get(position).getUserName() == null) {
//			Log.i("coder", "--holder getUserName is null--");
//		}
		
		
		
		String catalog = PinyinUtils.converterToFirstSpell(
				userInfos.get(position).getUserName()).substring(0, 1);
		if (position == 0) {
			holder.tv_catalog.setVisibility(View.VISIBLE);
			holder.tv_catalog.setText(catalog);
		} else {
			String lastCatalog = PinyinUtils.converterToFirstSpell(
					userInfos.get(position - 1).getUserName()).substring(0,
					1);
			if (catalog.equals(lastCatalog)) {
				holder.tv_catalog.setVisibility(View.GONE);
			} else {
				holder.tv_catalog.setVisibility(View.VISIBLE);
				holder.tv_catalog.setText(catalog);
			}
		}
		
		holder.tv_nick.setText(userInfos.get(position).getUserName());
		holder.tv_mobile.setText(userInfos.get(position).getPhoneNum());

		return view;
	}

	static class ViewHolder {

		TextView tv_catalog;
		TextView tv_nick;
		TextView tv_mobile;
	}

}
