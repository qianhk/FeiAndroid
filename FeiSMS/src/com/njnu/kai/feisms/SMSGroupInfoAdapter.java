package com.njnu.kai.feisms;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class SMSGroupInfoAdapter extends BaseAdapter {
	private static final String PREFIX = "[SMSGroupInfoAdapter]:";
	private Context mContext;
	private List<SMSGroupInfo> mListGroupInfo;
	private SparseBooleanArray mBooleanArray;

	private class ViewHolder {
		CheckBox mCheckBox;
		TextView mTextViewTitle;
		TextView mTextViewContent;
		View mParentView;
		int mDataId;
	}

	public int[] getCheckedGroupIds() {
		int size = mBooleanArray.size();
		int[] arrChecked = new int[size];
		for (int idx = 0; idx < size; ++idx) {
			arrChecked[idx] = mBooleanArray.keyAt(idx);
		}
		return arrChecked;
	}
	
	public void setCheckedState(int[] groupIds) {
		mBooleanArray.clear();
		for (int groupId : groupIds) {
			mBooleanArray.put(groupId, true);
		}
	}

	public SMSGroupInfoAdapter(Context context) {
		mContext = context;
		mBooleanArray = new SparseBooleanArray();
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

	private void setRowState(ViewHolder holder) {
		if (holder.mParentView instanceof CheckableLinearLayout) {
			CheckableLinearLayout cll = (CheckableLinearLayout) holder.mParentView;
			Boolean checked = mBooleanArray.get(holder.mDataId);
			cll.setChecked(checked);
			holder.mCheckBox.setChecked(checked);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.content_contacts_preview_item, null);
			holder = new ViewHolder();
			holder.mTextViewTitle = (TextView) convertView.findViewById(R.id.textview_title);
			holder.mTextViewContent = (TextView) convertView.findViewById(R.id.textview_content);
			holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox_select);
			holder.mParentView = convertView;
			convertView.setTag(holder);
			holder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						mBooleanArray.put(holder.mDataId, isChecked);
					} else {
						mBooleanArray.delete(holder.mDataId);
					}
					setRowState(holder);
				}
			});
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SMSGroupInfo groupInfo = mListGroupInfo.get(position);
		holder.mTextViewTitle.setText(groupInfo.getGroupName());
		int personAmount = groupInfo.getPersonAmount();
		if (personAmount > 0) {
			holder.mTextViewContent.setText("本组共 " + groupInfo.getPersonAmount() + " 位联系人");
		} else {
			holder.mTextViewContent.setText("本组暂无联系人");
		}
		holder.mDataId = groupInfo.getGroupId();
		setRowState(holder);
		return convertView;
	}

}
