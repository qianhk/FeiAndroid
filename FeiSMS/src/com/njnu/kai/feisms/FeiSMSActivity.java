package com.njnu.kai.feisms;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FeiSMSActivity extends ListActivity {
	private FeiSMSDataManager mDataManager;
	private SMSGroupInfoAdapter mGroupInfoAdapter;
	private static final String PREFIX = "[FeiSMSActivity]:";

	ListView.OnCreateContextMenuListener mMenuLis = new ListView.OnCreateContextMenuListener() {
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
			AdapterView.AdapterContextMenuInfo _menuInfo = (AdapterView.AdapterContextMenuInfo)menuInfo;
			String groupName = mGroupInfoAdapter.getItem(_menuInfo.position).getGroupName();
			menu.setHeaderTitle(groupName);
			menu.add(0, 1, 1, "position");
			menu.add(0, 2, 2, "Delete Group");
			menu.add(0, 3, 3, "Send Group");
//			menu.addSubMenu(1, 100, 100, "sub1");
//			menu.addSubMenu(1, 101, 101, "sub2");
			Log.i(PREFIX, "onCreateContextMenu1 menuInfo=" + menuInfo);
		}
	};

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
				intent.putExtra(FeiSMSConst.GROUP_ID, id);
				startActivity(intent);
			}
		});
		getListView().setOnCreateContextMenuListener(mMenuLis);
//		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 1, 1, "Edit Group");
		menu.add(0, 2, 2, "Delete Group");
		menu.add(0, 3, 3, "Send Group");
		Log.i(PREFIX, "onCreateContextMenu2");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		String groupName = mGroupInfoAdapter.getItem(menuInfo.position).getGroupName();
		Log.i(PREFIX, "onContextItemSelected " + item.getItemId() + " groupName=" + groupName);
		switch (item.getItemId()) {
		case 1:

			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "Add Group");
		menu.add(0, 2, 2, "Delete Selected Group");
		menu.add(0, 3, 3, "Send Selected Group");
		menu.add(0, 4, 4, "Send All Group");
		boolean sss = super.onCreateOptionsMenu(menu);
//		Log.i(PREFIX, "onCreateOptionsMenu " + menu.size());
		return sss;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean consumed = true;
		switch (item.getItemId()) {
		case 0: // Add group
			break;

		default:
			consumed = false;
			break;
		}
		boolean sss = super.onOptionsItemSelected(item);
//		Log.i(PREFIX, "onOptionsItemSelected " + sss);
		return sss;
//		return consumed ? true : super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		List<SMSGroupInfo> listGroupInfo = mDataManager.getAllSMSGroupInfo();
		mGroupInfoAdapter.refreshGroupInfo(listGroupInfo);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(PREFIX, "onDestroy");
	}
}
