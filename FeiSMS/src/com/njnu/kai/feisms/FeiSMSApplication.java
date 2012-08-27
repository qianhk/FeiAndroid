package com.njnu.kai.feisms;

import java.util.ArrayList;

import android.app.Application;
import android.util.Log;

public class FeiSMSApplication extends Application {
	private static FeiSMSApplication mInstance;
	private ContactsData mContactsData; // for keep variable only
	private static String PREFIX = "FeiSMSApplication";
	
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		
		ArrayList<HanziToPinyin.Token> arrToken = HanziToPinyin.getInstance().get("中");
		Log.i(PREFIX, "HanziToPinyin source=" + arrToken.get(0).source + " target=" + arrToken.get(0).target);
	}

	public static FeiSMSApplication getInstance() {
		return mInstance;
	}

	public void setContactsData(ContactsData contactsData) {
		mContactsData = contactsData;
	}

}
