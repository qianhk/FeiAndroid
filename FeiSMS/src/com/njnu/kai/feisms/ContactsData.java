package com.njnu.kai.feisms;

import java.util.ArrayList;
import java.util.List;

public class ContactsData {
	
	public static class ContactsInfo {
		private String mName;
		private List<String> mListPhoneNumber;
		
		public ContactsInfo(String name, int capacity) {
			mName = name;
			mListPhoneNumber = new ArrayList<String>(capacity);
		}
		
		public String getName() {
			return mName;
		}
		public void setName(String name) {
			mName = name;
		}
		
		public void appendPhoneNumber(String number){
			mListPhoneNumber.add(number);
		}
	}
	
	private List<ContactsInfo> mListContactsInfo;

	public ContactsData(int capacity) {
		mListContactsInfo = new ArrayList<ContactsInfo>(capacity);
	}
	
	public void appendContactsInfo(ContactsInfo info) {
		mListContactsInfo.add(info);
	}
	
	public int getCount() {
		return mListContactsInfo.size();
	}
	
	public ContactsInfo getContactsInfo(int index) {
		ContactsInfo info = null;
		if (mListContactsInfo.size() >= (index + 1)) {
			info = mListContactsInfo.get(index);
		}
		return info;
	}
}