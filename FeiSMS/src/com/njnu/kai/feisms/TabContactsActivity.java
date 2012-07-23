package com.njnu.kai.feisms;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class TabContactsActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_contact);
	}

	@Override
	protected void onResume() {
		super.onResume();

		View currentFocus = getListView();
		IBinder windowToken = currentFocus != null ? currentFocus.getWindowToken() : null;
//		Log.i("gaga", " focusView=" + currentFocus + " token=" + windowToken);
		if (windowToken != null) {
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}
