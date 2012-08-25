package com.njnu.kai.feisms;

import android.app.Application;

public class FeiSMSApplication extends Application {
	private static FeiSMSApplication mInstance;
	private ContactsData mContactsData; // for keep variable only
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}

	public static FeiSMSApplication getInstance() {
		return mInstance;
	}

	public void setContactsData(ContactsData contactsData) {
		mContactsData = contactsData;
	}

}
