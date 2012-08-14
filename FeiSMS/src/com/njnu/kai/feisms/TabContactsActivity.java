package com.njnu.kai.feisms;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TabContactsActivity extends ListActivity {
	private static final String PREFIX = "TabContactsActivity";
	private Handler mHandler = new Handler();
	private int mGroupId;
	private FeiSMSDataManager mDataManager;
	private ListView mListView;
	private TextView mTextViewSummary;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_contacts);
		mGroupId = getIntent().getIntExtra(FeiSMSConst.KEY_GROUP_ID, 0);
		mDataManager = FeiSMSDataManager.getDefaultInstance(this);
		mListView = getListView();
		mTextViewSummary = (TextView) findViewById(R.id.summary);
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//		Cursor cursor = getContentResolver().query(Contacts.CONTENT_URI, new String[] { Contacts._ID, Contacts.DISPLAY_NAME }, null, null,
//				Contacts.DISPLAY_NAME + " asc");
//		startManagingCursor(cursor);
//
//		ListAdapter listAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor,
//				new String[] { Contacts.DISPLAY_NAME }, new int[] { android.R.id.text1 });
//		setListAdapter(listAdapter);
		refreshGroupContacts();
	}

	void refreshGroupContacts() {
		SMSGroupEntryContacts entryContacts = mDataManager.getSMSGroupEntryContacts(mGroupId);
		if (entryContacts != null) {
			ArrayAdapter adapter = new ArrayAdapter<SMSGroupEntryContactsItem>(this, R.layout.simple_list_item_checked,
					entryContacts.getListContacts());
			setListAdapter(adapter);
		}
		mTextViewSummary.setText(String.format("Total %1$d contacts.", entryContacts != null ? entryContacts.getCount() : 0));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(100, 100, 100, "Add Contacts");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 100:
			chooseContacts();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		Log.i(PREFIX, "onActivityResult requestCode=" + requestCode + " resultCode=" + resultCode + " data=" + data);
		if (requestCode == FeiSMSConst.REQUEST_CODE_CHOOSE_CONTACTS && resultCode == RESULT_OK && data != null) {
//			Long[] dataIds = (Long[])data.getSerializableExtra(FeiSMSConst.KEY_CONTACTS_ID);
//			List<String> listName = data.getStringArrayListExtra(FeiSMSConst.KEY_CONTACTS_NAME);
//			List<String> listPhone = data.getStringArrayListExtra(FeiSMSConst.KEY_CONTACTS_PHONE);
			refreshGroupContacts();
		}
	}

	private void chooseContacts() {
		Intent intent = new Intent(this, ChooseContactsActivity.class);
		intent.putExtra(FeiSMSConst.KEY_GROUP_ID, mGroupId);
		startActivityForResult(intent, FeiSMSConst.REQUEST_CODE_CHOOSE_CONTACTS);
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
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(windowToken,
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}, 0);
	}

//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		Log.i(PREFIX, "onSaveInstanceState");
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		Log.i(PREFIX, "onPause");
//
//	}
//
//	@Override
//	protected void onStop() {
//		super.onStop();
//		Log.i(PREFIX, "onStop");
//	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(PREFIX, "onDestroy");
		SMSUtils.clearContactsData();
	}
}
