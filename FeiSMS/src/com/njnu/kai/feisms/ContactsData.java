package com.njnu.kai.feisms;

import java.util.ArrayList;
import java.util.List;

public class ContactsData {
	
	public static class ContactsInfo {
		private long mId;
		private String mName;
		private List<String> mListPhoneNumber;
		
		public ContactsInfo(long id, String name, int capacity) {
			mId = id;
			mName = name;
			mListPhoneNumber = new ArrayList<String>(capacity);
		}
		
		public long getId() {
			return mId;
		}

		public void setId(long id) {
			mId = id;
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
		
		public int getPhoneNumberCount() {
			return mListPhoneNumber.size();
		}
		
		public String getPhoneNumber(int index) {
			return mListPhoneNumber.get(index);
		}
	}
	
	private List<ContactsInfo> mListContactsInfo;
	private int mPhoneCount;

	public ContactsData(int capacity) {
		mListContactsInfo = new ArrayList<ContactsInfo>(capacity);
	}
	
	public void appendContactsInfo(ContactsInfo info) {
		mListContactsInfo.add(info);
	}
	
	public int getContactsCount() {
		return mListContactsInfo.size();
	}
	
	public ContactsInfo getContactsInfo(int index) {
		ContactsInfo info = null;
		if (mListContactsInfo.size() >= (index + 1)) {
			info = mListContactsInfo.get(index);
		}
		return info;
	}

	public int getPhoneCount() {
		return mPhoneCount;
	}

	public void setPhoneCount(int phoneCount) {
		mPhoneCount = phoneCount;
	}


}