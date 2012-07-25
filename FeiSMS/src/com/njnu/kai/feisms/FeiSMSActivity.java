package com.njnu.kai.feisms;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class FeiSMSActivity extends ListActivity {
	private FeiSMSDataManager mDataManager;
	private SMSGroupInfoAdapter mGroupInfoAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_contacts_preview);
		mDataManager = FeiSMSDataManager.getDefaultInstance(this);
		mGroupInfoAdapter = new SMSGroupInfoAdapter(this);
		
//		mDataManager.AppendSMSGroup("groupName1", "groupSmS1");
//		mDataManager.AppendSMSGroup("groupName2", "groupSmS2");
//		mDataManager.AppendSMSGroup("groupName3", "groupSmS3");
//		mDataManager.AppendSMSGroup("groupName4", "groupSmS4");
//		mDataManager.AppendSMSGroup("groupName5", "groupSmS5");
//		mDataManager.AppendSMSGroup("groupName6", "groupSmS6");
//		mDataManager.AppendSMSGroup("groupName14", "groupSmS14");
//		mDataManager.AppendSMSGroup("groupName15", "groupSmS15");
//		mDataManager.AppendSMSGroup("groupName16", "groupSmS16");
//		mDataManager.AppendSMSGroup("groupName24", "groupSmS24");
//		mDataManager.AppendSMSGroup("groupName25", "groupSmS25");
//		mDataManager.AppendSMSGroup("groupName26", "groupSmS26");
//		mDataManager.AppendContactsToGroup(1, 1, "contactsName1", "contactsPhoneNumber1");
//		mDataManager.AppendContactsToGroup(1, 2, "contactsName2", "contactsPhoneNumber2");
//		mDataManager.AppendContactsToGroup(1, 3, "contactsName3", "contactsPhoneNumber3");
//		mDataManager.AppendContactsToGroup(2, 4, "contactsName4", "contactsPhoneNumber4");
//		mDataManager.AppendContactsToGroup(2, 5, "contactsName5", "contactsPhoneNumber5");
//		mDataManager.AppendContactsToGroup(2, 6, "contactsName6", "contactsPhoneNumber6");
//		mDataManager.AppendContactsToGroup(2, 7, "contactsName7", "contactsPhoneNumber7");

		setListAdapter(mGroupInfoAdapter);
		
		this.getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(FeiSMSActivity.this, ContentContactsDetailActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "Add Group");
		menu.add(0, 2, 2, "Delete Selected Group");
		menu.add(0, 3, 3, "Send Selected Group");
		menu.add(0, 4, 4, "Send All Group");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0: //Add group
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		List<SMSGroupInfo> listGroupInfo = mDataManager.getAllSMSGroupInfo();
		mGroupInfoAdapter.refreshGroupInfo(listGroupInfo);
	}
}
