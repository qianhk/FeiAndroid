package com.njnu.kai.feisms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.njnu.kai.feisms.ContactsData.ContactsInfo;

public class ChooseContactsAdapter extends BaseAdapter {

	private static final String PREFIX = "ChooseContactsAdapter";
	private ContactsData mContactsData;
	private boolean mDisplayDifference;
	private Context mContext;
	private FeiSMSDataManager mDataManager;
	private HanziToPinyin mHanziToPinyin = HanziToPinyin.getInstance();

	private List<ChooseContactsForDisplay> mListContactsForDisplay;
	private List<ChooseContactsForDisplay> mListContactsForDisplayBak;
	private List<ChooseContactsForDisplay> mListContactsForDisplayFilter;

	private long[] mUsedContactsId;

	public ChooseContactsAdapter(Context context) {
		mContext = context;
		mDataManager = FeiSMSDataManager.getDefaultInstance(context);
	}

	public void setContactsData(ContactsData contactsData) {
		mContactsData = contactsData;
		mListContactsForDisplay = new ArrayList<ChooseContactsForDisplay>(mContactsData.getPhoneCount());
		mListContactsForDisplayBak = mListContactsForDisplay;
	}

	public void refreshContactsData(boolean isDisplayDifference) {
		if (mContactsData == null) return;

		mDisplayDifference = isDisplayDifference;
		mListContactsForDisplay.clear();
		mUsedContactsId = null;

		mUsedContactsId = mDataManager.getUsedContactsId();
		int idx = -1;
		int totalContactsCount = mContactsData.getContactsCount();
		for (int i = 0; i < totalContactsCount; ++i) {
			ContactsInfo info = mContactsData.getContactsInfo(i);
			if (Arrays.binarySearch(mUsedContactsId, info.getId()) < 0) {
				int infoPhoneCount = info.getPhoneNumberCount();
				for (int j = 0; j < infoPhoneCount; ++j) {
					addDisplayContacts(info.getId(), info.getName(), info.getPhoneNumber(j));
				}
			} else if (info.getPhoneNumberCount() > 1 && isDisplayDifference) {
				List<String> noUsedPhone = mDataManager.getContactsNoUsedPhoneNumber(info);
				int infoPhoneCount = noUsedPhone == null ? 0 : noUsedPhone.size();
				for (int j = 0; j < infoPhoneCount; ++j) {
					addDisplayContacts(info.getId(), info.getName(), noUsedPhone.get(j));
				}
			}
		}
		notifyDataSetChanged();
	}

	private void addDisplayContacts(long id, String name, String phone) {
		ChooseContactsForDisplay cfd = new ChooseContactsForDisplay();
		cfd.mContactsId = id;
		cfd.mName = name;
		cfd.mPhone = phone;
		cfd.mPinyin = mHanziToPinyin.getPinyin(name);
		mListContactsForDisplay.add(cfd);
	}

	@Override
	public int getCount() {
		return mListContactsForDisplay != null ? mListContactsForDisplay.size() : 0;
	}

	@Override
	public ChooseContactsForDisplay getItem(int position) {
		return mListContactsForDisplay.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).mContactsId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.simple_list_item_checked, null);
		}
		CheckedTextView tv = (CheckedTextView) convertView;
		tv.setText(getItem(position).toString());
//		Log.i(PREFIX, "getView=" + convertView);
		return convertView;
	}

	public void filter(String newText) {
		if (TextUtils.isEmpty(newText)) {
			mListContactsForDisplay = mListContactsForDisplayBak;
			mListContactsForDisplayFilter = null;
			notifyDataSetChanged();
			return;
		}
		if (mListContactsForDisplayFilter == null) {
			mListContactsForDisplayFilter = new ArrayList<ChooseContactsForDisplay>(mListContactsForDisplayBak.size());
		}
		mListContactsForDisplayFilter.clear();
		mListContactsForDisplay = mListContactsForDisplayFilter;
//		mListContactsForDisplayFilter.addAll(mListContactsForDisplayBak);
		for (ChooseContactsForDisplay contacts : mListContactsForDisplayBak) {
			if (contacts.isAccordWith(newText)) {
				mListContactsForDisplay.add(contacts);
			}
		}
		notifyDataSetChanged();
	}

}
