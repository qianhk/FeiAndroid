package com.njnu.kai.feisms;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.njnu.kai.feisms.ChooseContactsAdapter.ContactsForDisplay;

public class ChooseContactsActivity extends Activity {
	private static final String PREFIX = "ChooseContacts";
	private ListView mListViewContacts;
	private ChooseContactsAdapter mAdapter;
	private static final String mTitle = "Choose Contacts";
	private static final String mTitle_choosed_contacts = "Choose Contacts: %d";
	private FeiSMSDataManager mDataManager;
	private int mGroupId;
	private CheckedTextView mCheckedTextViewShowDifference;
	private ContactsData mContactsData;
	private GetSysContactsTask mTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(mTitle);
		setContentView(R.layout.choose_contacts);
		mGroupId = getIntent().getIntExtra(FeiSMSConst.KEY_GROUP_ID, 0);

		mCheckedTextViewShowDifference = (CheckedTextView) findViewById(R.id.checkedtextview_display_different_number);
		mCheckedTextViewShowDifference.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckedTextView ctv = (CheckedTextView) v;
				ctv.toggle();
				boolean checked = ctv.isChecked();
				Log.i(PREFIX, "Checked=" + checked);
//				mAdapter.refreshContactsData(checked);
//				mCheckedTextViewShowDifference.setText("显示联系人不同号码, 当前共 " + mAdapter.getCount() + " 项");
			}
		});
		mListViewContacts = (ListView) findViewById(R.id.listview_contacts);
		mAdapter = new ChooseContactsAdapter(this);
		mListViewContacts.setAdapter(mAdapter);
		mListViewContacts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mListViewContacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Log.i(PREFIX, "onItemClick " + position + " id=" + id);
				int checkedItemCount = mListViewContacts.getCheckedItemCount();
				if (checkedItemCount > 0) {
					setTitle(String.format(mTitle_choosed_contacts, checkedItemCount));
				} else {
					setTitle(mTitle);
				}
			}
		});

		mDataManager = FeiSMSDataManager.getDefaultInstance(this);
		refreshListView();
	}

	public void refreshListView() {
		mContactsData = SMSUtils.getContactsData();
		if (mContactsData != null) {
			mAdapter.setContactsData(mContactsData);
			mAdapter.refreshContactsData(false);
			mCheckedTextViewShowDifference.setText("显示联系人不同号码, 当前共 " + mAdapter.getCount() + " 项");
		} else {
			mTask = new GetSysContactsTask(this);
			mTask.execute();
		}
	}

	public void button_add_clicked(View view) {
		Log.i(PREFIX, "button_add_clicked");
//		long[] checkedItemIds = mListViewContacts.getCheckedItemIds();
		SparseBooleanArray checkedItemPositions = mListViewContacts.getCheckedItemPositions();
		ArrayList<SMSGroupEntryContactsItem> listContacts = new ArrayList<SMSGroupEntryContactsItem>();
		int size = checkedItemPositions.size();
		for (int idx = 0; idx < size; ++idx) {
			if (checkedItemPositions.valueAt(idx)) {
				int pos = checkedItemPositions.keyAt(idx);
				ContactsForDisplay item = mAdapter.getItem(pos);
				SMSGroupEntryContactsItem smsItem = new SMSGroupEntryContactsItem(-1, (int) item.mContactsId, item.mName, item.mPhone);
				listContacts.add(smsItem);
			}
		}
		if (listContacts.size() > 0) {
//		Log.i(PREFIX, "choosed " + listContactsId.size() + " items.");
			mDataManager.appendContactsToGroup(mGroupId, listContacts);
			Intent intent = new Intent();
//		intent.putExtra(FeiSMSConst.KEY_CONTACTS_ID, listContactsId.toArray(new Long[0]));
//		intent.putStringArrayListExtra(FeiSMSConst.KEY_CONTACTS_NAME, listName);
//		intent.putStringArrayListExtra(FeiSMSConst.KEY_CONTACTS_PHONE, listPhone);
			setResult(RESULT_OK, intent);
		}
		finish();
	}

	public void button_cancel_clicked(View view) {
		Log.i(PREFIX, "button_cancel_clicked");
		setResult(RESULT_CANCELED);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mTask != null) {
			mTask.cancel(true);
		}
	}

}
