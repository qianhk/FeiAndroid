package com.njnu.kai.feisms;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class ExcludeContactsActivity extends BaseSelectedContactsActivity {
private static final String PREFIX = "ExcludeContactsActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		setTitle("Exclude Contacts");
		Log.i(PREFIX, "onCreate taskid=" + getTaskId() + " this=" + this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.i(PREFIX, "onNewIntent");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(100, 100, 100, "Add Contacts");
//		menu.add(100, 101, 101, "Remove Selected Contacts");
		super.onCreateOptionsMenu(menu);
		
//		MenuItem itemAdd = menu.getItem(0);
//		itemAdd.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//		itemAdd.setIcon(android.R.drawable.ic_menu_add);
		return true;
	}
}
