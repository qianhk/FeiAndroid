package com.njnu.kai.feisms;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Checkable;
import android.widget.ListView;

public class FeiSMSActivity extends ListActivity {
	private FeiSMSDataManager mDataManager;
	private SMSGroupInfoAdapter mGroupInfoAdapter;
	private static final String PREFIX = "[FeiSMSActivity]:";
	
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
//				Intent intent = new Intent(FeiSMSActivity.this, ContentContactsDetailActivity.class);
//				startActivity(intent);
//				SparseBooleanArray bA = getListView().getCheckedItemPositions();
//				int bASize = bA.size();
//				Log.i(PREFIX, "baSize=" + bASize + " positon=" + position);
//				for (int idx = 0; idx < bASize; ++idx) {
//					int key = bA.keyAt(idx);
//					boolean v1 = bA.get(key);
//					boolean v2 = bA.valueAt(idx);
//					Log.i(PREFIX, "idx=" + idx + " key=" + key + " v1=" + v1 + " v2=" + v2);
//				}
//				boolean checked = getListView().getCheckedItemPositions().get(position);
//				Log.i(PREFIX, "parent=" + parent + " view=" + view + " position=" + position + " id=" + id + " getListView=" + getListView());
//				getListView().setItemChecked(position, !checked);
//				if (view instanceof Checkable) {
//					Log.i(PREFIX, "view is instanceof Checkable, checked=" + checked);
//					Checkable cc = (Checkable)view;
//					cc.setChecked(checked);
//				}
			}
		});
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//		getListView().seti
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
