package com.njnu.kai.feisms;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
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
import android.widget.Toast;

public class FeiSMSActivity extends ListActivity {
	private FeiSMSDataManager mDataManager;
	private SMSGroupInfoAdapter mAdapterGroupInfo;
	private static final String PREFIX = "[FeiSMSActivity]:";
	private final static String KEY_SELECTED_GROUP_ID = "key_selected_group_id";

	ListView.OnCreateContextMenuListener mMenuLis = new ListView.OnCreateContextMenuListener() {
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
			AdapterView.AdapterContextMenuInfo _menuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
			String groupName = mAdapterGroupInfo.getItem(_menuInfo.position).getGroupName();
			menu.setHeaderTitle(groupName);
			menu.add(0, 1, 1, "Edit Group");
			menu.add(0, 2, 2, "Delete Group");
			menu.add(0, 3, 3, "Send Group");
//			menu.addSubMenu(1, 100, 100, "sub1");
//			menu.addSubMenu(1, 101, 101, "sub2");
//			Log.i(PREFIX, "onCreateContextMenu1 menuInfo=" + menuInfo);
		}
	};

	private void promptRestoreSelectState(String from) {
		Toast.makeText(this, "Restore state from " + from, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_contacts_preview);

		Log.i(PREFIX, "onCreate: " + this);

		mDataManager = FeiSMSDataManager.getDefaultInstance(this);
		mAdapterGroupInfo = new SMSGroupInfoAdapter(this);

//		mDataManager.appendSMSGroup("groupName1", "groupSmS1");
//		mDataManager.appendSMSGroup("groupName2", "groupSmS2");
//		mDataManager.appendSMSGroup("groupName3", "groupSmS3");
//		mDataManager.appendSMSGroup("groupName4", "groupSmS4");
//		mDataManager.appendSMSGroup("groupName5", "groupSmS5");
//		mDataManager.appendSMSGroup("groupName6", "groupSmS6");
//		mDataManager.appendSMSGroup("groupName14", "groupSmS14");
//		mDataManager.appendSMSGroup("groupName15", "groupSmS15");
//		mDataManager.appendSMSGroup("groupName16", "groupSmS16");
//		mDataManager.appendSMSGroup("groupName24", "groupSmS24");
//		mDataManager.appendSMSGroup("groupName25", "groupSmS25");
//		mDataManager.appendSMSGroup("groupName26", "groupSmS26");
//		mDataManager.appendContactsToGroup(1, 1, "contactsName1", "contactsPhoneNumber1");
//		mDataManager.appendContactsToGroup(1, 2, "contactsName2", "contactsPhoneNumber2");
//		mDataManager.appendContactsToGroup(1, 3, "contactsName3", "contactsPhoneNumber3");
//		mDataManager.appendContactsToGroup(2, 4, "contactsName4", "contactsPhoneNumber4");
//		mDataManager.appendContactsToGroup(2, 5, "contactsName5", "contactsPhoneNumber5");
//		mDataManager.appendContactsToGroup(2, 6, "contactsName6", "contactsPhoneNumber6");
//		mDataManager.appendContactsToGroup(2, 7, "contactsName7", "contactsPhoneNumber7");

		setListAdapter(mAdapterGroupInfo);

		this.getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				openContentContactsDetailActivity((int) id);
			}

		});
		getListView().setOnCreateContextMenuListener(mMenuLis);
//		registerForContextMenu(getListView());

//		if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SELECTED_GROUP_ID)) {
//			int[] selectedRow = savedInstanceState.getIntArray(KEY_SELECTED_GROUP_ID);
//			Log.e(PREFIX, "onCreate ori length=" + selectedRow.length + " firstid=" + selectedRow[0]);
//			promptRestoreSelectState("onCreate length=" + selectedRow.length);
//			mAdapterGroupInfo.setCheckedState(selectedRow);
//		}
	}

	private void openContentContactsDetailActivity(int groupId) {
		Intent intent = new Intent(FeiSMSActivity.this, ContentContactsDetailActivity.class);
		intent.putExtra(FeiSMSConst.KEY_GROUP_ID, groupId);
		startActivity(intent);
	}

//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//		super.onCreateContextMenu(menu, v, menuInfo);
//		menu.add(0, 1, 1, "Edit Group");
//		menu.add(0, 2, 2, "Delete Group");
//		menu.add(0, 3, 3, "Send Group");
//
//		Log.i(PREFIX, "onCreateContextMenu2");
//	}

	private void showConfirmDialog(String prompt, DialogInterface.OnClickListener listener) {
		AlertDialog dialog = new AlertDialog.Builder(this).setIconAttribute(android.R.attr.alertDialogIcon).setMessage(prompt)
				.setPositiveButton("Okay", listener).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				}).create();
		dialog.show();
	}

	private void deleteSMSGroup(final int[] groupIds) {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				mDataManager.deleteSMSGroup(groupIds);
				mAdapterGroupInfo.deleteSMSGroup(groupIds);
			}
		};
		showConfirmDialog("Make sure delete " + groupIds.length + " items?", listener);
	}

	private void sendSMSGroup(final int[] groupIds) {
		// TODO add code to do send sms.
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Log.i(PREFIX, "will sendSMSGroup " + groupIds.length + " firstGid=" + groupIds[0]);
			}
		};
		showConfirmDialog("Make sure send " + groupIds.length + " groups?", listener);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		SMSGroupInfo smsGroupInfo = mAdapterGroupInfo.getItem(menuInfo.position);
		String groupName = smsGroupInfo.getGroupName();
		Log.i(PREFIX, "onContextItemSelected " + item.getItemId() + " groupName=" + groupName);
		switch (item.getItemId()) {
		case 1: // Edit Group
			openContentContactsDetailActivity(smsGroupInfo.getGroupId());
			break;

		case 2: // Delete Group
			deleteSMSGroup(new int[] { smsGroupInfo.getGroupId() });
			break;

		case 3: // Send Group
			sendSMSGroup(new int[] { smsGroupInfo.getGroupId() });
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		int[] selectedId = mAdapterGroupInfo.getCheckedGroupIds();
		if (selectedId.length > 0) {
			Log.e(PREFIX, "onSaveInstanceState() length=" + selectedId.length + " firstid=" + selectedId[0]);
			outState.putIntArray(KEY_SELECTED_GROUP_ID, selectedId);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.e(PREFIX, "onRestoreInstanceState()");
		if (savedInstanceState.containsKey(KEY_SELECTED_GROUP_ID)) {
			int[] selectedRow = savedInstanceState.getIntArray(KEY_SELECTED_GROUP_ID);
			Log.e(PREFIX, "onRestoreInstanceState ori length=" + selectedRow.length + " firstid=" + selectedRow[0]);
			promptRestoreSelectState("onRestore length=" + selectedRow.length);
			mAdapterGroupInfo.setCheckedState(selectedRow);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "Add Group");
		menu.add(0, 2, 2, "Delete Selected Group");
		menu.add(0, 3, 3, "Send Selected Group");
		menu.add(0, 4, 4, "Send All Group");

		menu.add(100, 100, 100, "Test");

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean haveCheckItems = mAdapterGroupInfo.getCheckedGroupIds().length > 0;
		menu.getItem(1).setVisible(haveCheckItems);
		menu.getItem(2).setVisible(haveCheckItems);
		menu.getItem(3).setVisible(mAdapterGroupInfo.getCount() > 0);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean consumed = true;
		Log.i(PREFIX, "onOptionsItemSelected " + item.getItemId());
		switch (item.getItemId()) {
		case 1: // Add group
			openContentContactsDetailActivity(-1);
			break;

		case 2: // Delete Selected Group
			deleteSMSGroup(mAdapterGroupInfo.getCheckedGroupIds());
			break;

		case 3: // Send Selected Group
			sendSMSGroup(mAdapterGroupInfo.getCheckedGroupIds());
			break;

		case 4: // Send All Group
			sendSMSGroup(mAdapterGroupInfo.getAllGroupids());
			break;

		case 100:
			promptRestoreSelectState("test lenght=100");
			break;

		default:
			consumed = super.onOptionsItemSelected(item);
			break;
		}
		return consumed;
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(PREFIX, "onStart");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i(PREFIX, "onRestart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(PREFIX, "onResume");
		List<SMSGroupInfo> listGroupInfo = mDataManager.getAllSMSGroupInfo();
		mAdapterGroupInfo.refreshGroupInfo(listGroupInfo);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(PREFIX, "onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(PREFIX, "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(PREFIX, "onDestroy");
	}
}
