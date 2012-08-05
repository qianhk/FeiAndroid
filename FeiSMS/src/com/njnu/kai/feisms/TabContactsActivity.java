package com.njnu.kai.feisms;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class TabContactsActivity extends ListActivity {
	private static final String PREFIX = "TabContactsActivity";
	private Handler mHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_contact);
		int groupId = getIntent().getIntExtra(FeiSMSConst.KEY_GROUP_ID, 0);
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPostResume() {
		super.onPostResume();

		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				View currentFocus = getListView();
				IBinder windowToken = currentFocus != null ? currentFocus.getWindowToken() : null;
				Log.i(PREFIX, "after onPostResume focusView=" + currentFocus + " token=" + windowToken);
				if (windowToken != null) {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}, 0);
	}

}
