package com.njnu.kai.feisms;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ExcludeContactsActivity extends BaseSelectedContactsActivity {
private static final String PREFIX = "ExcludeContactsActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Exclude Contacts");
		Log.i(PREFIX, "onCreate");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.i(PREFIX, "onNewIntent");
	}
}
