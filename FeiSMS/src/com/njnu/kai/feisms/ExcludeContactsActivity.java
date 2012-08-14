package com.njnu.kai.feisms;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class ExcludeContactsActivity extends ListActivity {

	private FeiSMSDataManager mDataManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDataManager = FeiSMSDataManager.getDefaultInstance(this);
		refreshGroupContacts();
	}

	private void refreshGroupContacts() {
		SMSGroupEntryContacts entryContacts = mDataManager.getSMSGroupEntryContacts(FeiSMSConst.GROUP_ID_EXCLUDE);
		if (entryContacts != null) {
			ArrayAdapter adapter = new ArrayAdapter<SMSGroupEntryContactsItem>(this, R.layout.simple_list_item_checked,
					entryContacts.getListContacts());
			setListAdapter(adapter);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 100:
			chooseContacts();
			break;
			
		case 101:
			break;
			
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(100, 100, 100, "Add Contacts");
		menu.add(100, 101, 101, "Remove Selected Contacts");
		return super.onCreateOptionsMenu(menu);
	}

	private void chooseContacts() {
		Intent intent = new Intent(this, ChooseContactsActivity.class);
		intent.putExtra(FeiSMSConst.KEY_GROUP_ID, FeiSMSConst.GROUP_ID_EXCLUDE);
		startActivityForResult(intent, FeiSMSConst.REQUEST_CODE_CHOOSE_CONTACTS);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FeiSMSConst.REQUEST_CODE_CHOOSE_CONTACTS && resultCode == RESULT_OK && data != null) {
			refreshGroupContacts();
		}
	}
}
