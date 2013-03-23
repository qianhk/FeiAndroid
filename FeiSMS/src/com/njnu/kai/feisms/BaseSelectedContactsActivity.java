package com.njnu.kai.feisms;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BaseSelectedContactsActivity extends ListActivity {
	private static final String PREFIX = "BaseSelectedContactsActivity";

	private Handler mHandler = new Handler();
	protected int mGroupId;
	protected FeiSMSDataManager mDataManager;
	private ListView mListView;
	private TextView mTextViewSummary;

//	private GroupIDUpdateReceiver mReceiver;
//
//	private class GroupIDUpdateReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			mGroupId = intent.getIntExtra(FeiSMSConst.KEY_GROUP_ID, 0);
//			Log.i(PREFIX, "onReceiver " + mGroupId);
//			refreshGroupContacts();
//			unregisterReceiver(mReceiver);
//			mReceiver = null;
//		}
//
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(PREFIX, "onCreate");
		setContentView(R.layout.base_selected_contacts);
		mGroupId = getIntent().getIntExtra(FeiSMSConst.KEY_GROUP_ID, FeiSMSConst.GROUP_ID_EXCLUDE);
		mDataManager = FeiSMSDataManager.getDefaultInstance(this);
		mListView = getListView();
		mTextViewSummary = (TextView)findViewById(R.id.summary);
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		refreshGroupContacts();

//		if (mGroupId == FeiSMSConst.GROUP_ID_CREATE) {
//			mReceiver = new GroupIDUpdateReceiver();
//			registerReceiver(mReceiver, new IntentFilter(FeiSMSConst.ACTION_GROUP_ID_UPDATED));
//		}
	}

	protected void refreshGroupContacts() {
		mListView.clearChoices();
		SMSGroupEntryContacts entryContacts = mDataManager.getSMSGroupEntryContacts(mGroupId);
		if (entryContacts != null) {
			ArrayAdapter adapter = new ArrayAdapter<SMSGroupEntryContactsItem>(this, R.layout.simple_list_item_checked,
					entryContacts.getListContacts());
			setListAdapter(adapter);
		} else {
			setListAdapter(null);
		}
		mTextViewSummary.setText(String.format("Total %1$d contacts.", entryContacts != null ? entryContacts.getCount() : 0));
	}

	@SuppressLint("NewApi")
	private void removeCheckedContacts() {
		ArrayAdapter<SMSGroupEntryContactsItem> listAdapter = (ArrayAdapter<SMSGroupEntryContactsItem>)getListAdapter();
		if (listAdapter != null) {
			if (Build.VERSION.SDK_INT > 10) {
				SparseBooleanArray checkedItemPositions = mListView.getCheckedItemPositions();
				long[] checkedItemIds = new long[mListView.getCheckedItemCount()];
				int size = checkedItemPositions.size();
				for (int idx = 0, j = -1; idx < size; ++idx) {
					if (checkedItemPositions.valueAt(idx)) {
						int pos = checkedItemPositions.keyAt(idx);
						checkedItemIds[++j] = listAdapter.getItem(pos).getDbId();
					}
				}
				mDataManager.deleteSMSContacts(checkedItemIds);
			} else {
				long[] checkItemIds1 = mListView.getCheckItemIds();
				long[] checkedItemIds = new long[checkItemIds1.length];
				for (int idx = checkItemIds1.length - 1; idx >= 0; --idx) {
					checkedItemIds[idx] = listAdapter.getItem((int)checkItemIds1[idx]).getDbId();
				}
				mDataManager.deleteSMSContacts(checkedItemIds);
			}
			refreshGroupContacts();
		}
	}
	thunder://QUFodHRwOi8vMTkyLjE2OC45LjI2OjgwMDAvU2t5ZmFsbCUyMDIwMTIlMjA3MjBwJTIwQmx1UmF5JTIweDI2NCUyMERUUyUyRFdpS2klMjAwMDclRUYlQkMlOUElRTUlQTQlQTclRTclQTAlQjQlRTUlQTQlQTklRTUlQjklOTUlRTYlOUQlODAlRTYlOUMlQkElRTMlODAlOEMlRTklQkIlOTElRTYlOUElOTclRTclQkIlODUlRTUlQTMlQUIlRTUlQjQlOUIlRTglQjUlQjclMjAlRTUlOEYlQjIlRTQlQjglOEElRTYlOUMlODAlRTUlQkMlQkElRTklODIlQTYlRTUlQTUlQjMlRTklODMlOEUlRTMlODAlOEQlMkVta3Y/a2V5PTZmMDk2YmVmNGQ0MWQzZDBjNTZhZjc1ZTEwNGZmMmM1JmZpbGVfdXJsPSUyRmdkcml2ZSUyRnJlc291cmNlJTJGQkYlMkYzMiUyRkJGNUEzQUNEM0M1NTRFMzQ0RDAxQUJGMzJFOTBDOTEwOTRDMzE0MzImZmlsZV90eXBlPTAmYXV0aGtleT05NTc0RjBGMTMzNUE5NzQ2QTBFQTA4MTU5QUZDQzIxNUU3NDdFQjgwNDk5MTRDMDNBQjAyNTAxRkZGRDBCM0I4JmV4cF90aW1lPTEzNjI4NjI5NzkmZnJvbV91aWQ9MjIyNzYxODIzJnRhc2tfaWQ9NTg0MDY5OTQwMTM0MzQ4MTA5MCZnZXRfdWlkPTEwMDEzMjg5NzImbXU9ZmI4ZGU1NmImZD1kbDMudDgmcmVkdWNlX2Nkbj0xJmZpZD0xbEM1RjJ6a08wVG80M29CbC9NeFdnSEd0QkFSRnVIOEFRQUFBTDlhT3MwOFZVNDBUUUdyOHk2UXlSQ1V3eFF5Jm1pZD02NjYmdGhyZXNob2xkPTE1MCZ0aWQ9MjI2NzAwNDc2NTIwNTcwQjcyRTMyOTFEOTA2MEE0MUImc3JjaWQ9NyZ2ZXJubz0xWlo=
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 100:
			chooseContacts();
			break;

		case 101:
			removeCheckedContacts();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		if (mReceiver != null) {
//			unregisterReceiver(mReceiver);
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(100, 100, 100, "Add Contacts");
		menu.add(100, 101, 101, "Remove Selected Contacts");

		return super.onCreateOptionsMenu(menu);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mGroupId >= -1) {
			menu.getItem(0).setVisible(true);
			boolean haveCheckItems = false;
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
				haveCheckItems = mListView.getCheckItemIds().length > 0;
			} else {
				haveCheckItems = mListView.getCheckedItemCount() > 0;
			}
			menu.getItem(1).setVisible(haveCheckItems);
		} else {
			menu.getItem(0).setVisible(false);
			menu.getItem(1).setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	private void chooseContacts() {
		Intent intent = new Intent(this, ChooseContactsActivity.class);
		intent.putExtra(FeiSMSConst.KEY_GROUP_ID, mGroupId);
		startActivityForResult(intent, FeiSMSConst.REQUEST_CODE_CHOOSE_CONTACTS);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FeiSMSConst.REQUEST_CODE_CHOOSE_CONTACTS && resultCode == RESULT_OK && data != null) {
			refreshGroupContacts();
		}
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();

		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				View currentFocus = getListView();
				IBinder windowToken = currentFocus != null ? currentFocus.getWindowToken() : null;
//				Log.i(PREFIX, "after onPostResume focusView=" + currentFocus + " token=" + windowToken);
				if (windowToken != null) {
					((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(windowToken,
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}, 0);
	}
}
