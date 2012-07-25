package com.njnu.kai.feisms;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SMSGroupInfoAdapter extends BaseAdapter {
	private static final String PREFIX = "[SMSGroupInfoAdapter]:";
	private Context mContext;
	private List<SMSGroupInfo> mListGroupInfo;

	public SMSGroupInfoAdapter(Context context) {
		mContext = context;
	}

	public void refreshGroupInfo(List<SMSGroupInfo> listGroupInfo) {
		mListGroupInfo = listGroupInfo;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return (mListGroupInfo == null) ? 0 : mListGroupInfo.size();
	}

	@Override
	public SMSGroupInfo getItem(int position) {
		SMSGroupInfo groupInfo = null;
		if (position >= 0 && position < getCount()) {
			groupInfo = mListGroupInfo.get(position);
		}
		return groupInfo;
	}

	@Override
	public long getItemId(int position) {
		SMSGroupInfo groupInfo = getItem(position);
		return (groupInfo == null) ? -1 : groupInfo.getGroupId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.content_contacts_preview_item, null);
		}
		SMSGroupInfo groupInfo = mListGroupInfo.get(position);
		TextView textviewTitle = (TextView) convertView.findViewById(R.id.textview_title);
		textviewTitle.setText(groupInfo.getGroupName());
		TextView textviewContent = (TextView) convertView.findViewById(R.id.textview_content);
		int personAmount = groupInfo.getPersonAmount();
		if (personAmount > 0) {
			textviewContent.setText("本组共 " + groupInfo.getPersonAmount() + " 位联系人");
		} else {
			textviewContent.setText("本组暂无联系人");
		}
		return convertView;
	}

}
