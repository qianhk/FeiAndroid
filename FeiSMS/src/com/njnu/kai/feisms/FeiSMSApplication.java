package com.njnu.kai.feisms;

import android.app.Application;

public class FeiSMSApplication extends Application {
	private static FeiSMSApplication mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}

	public static FeiSMSApplication getInstance() {
		return mInstance;
	}

}
