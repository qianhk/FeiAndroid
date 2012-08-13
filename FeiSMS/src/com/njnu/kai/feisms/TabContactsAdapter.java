package com.njnu.kai.feisms;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TabContactsAdapter extends BaseAdapter {
	private SMSGroupEntryContacts mEntryContacts;

	public void refreshContacts(SMSGroupEntryContacts entryContacts) {
		mEntryContacts = entryContacts;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
