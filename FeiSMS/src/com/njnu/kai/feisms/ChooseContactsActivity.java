package com.njnu.kai.feisms;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;


public class ChooseContactsActivity extends Activity implements OnQueryTextListener {
	private static final String PREFIX = "ChooseContactsActivity";
	private ListView mListViewContacts;
	private ChooseContactsAdapter mAdapter;
	private static final String mTitle = "Choose Contacts";
	private static final String mTitle_choosed_contacts = "Choose Contacts: %d";
	private FeiSMSDataManager mDataManager;
	private int mGroupId;
	private CheckedTextView mCheckedTextViewShowDifference;
	private ContactsData mContactsData;
	private GetSysContactsTask mTask;
	private SearchView mSearchView;
	private Toast mToast;

//	private EditText mEditTextFilter;

//	private TextWatcher mTextWatcher = new TextWatcher() {
//		
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before, int count) {
//		}
//		
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//		}
//		
//		@Override
//		public void afterTextChanged(Editable s) {
//		}
//	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
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
				int oldCount = mAdapter.getCount();
				mAdapter.refreshContactsData(checked);
				int newCount = mAdapter.getCount();
				if (newCount != oldCount) {
					mListViewContacts.clearChoices();
				}
				mCheckedTextViewShowDifference.setText("显示联系人不同号码, 当前共 " + newCount + " 项");
			}
		});
//		mEditTextFilter = (EditText) findViewById(R.id.edittext_filter);
//		mEditTextFilter.setVisibility(View.GONE);
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

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case 100:
//			mEditTextFilter.setVisibility(View.VISIBLE);
//			mCheckedTextViewShowDifference.setVisibility(View.GONE);
			Log.i(PREFIX, "should not be called");
			break;

		case 101:
			Log.i(PREFIX, "searchView iconified=" + mSearchView.isIconified());
			mSearchView.setIconified(!mSearchView.isIconified());
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onBackPressed() {
		if (!mSearchView.isIconified()) {
//			mEditTextFilter.removeTextChangedListener(mTextWatcher);
//			mEditTextFilter.setVisibility(View.GONE);
//			mEditTextFilter.setText("");
			mSearchView.setIconified(true);
			if (!mSearchView.isIconified()) {
				mSearchView.setIconified(true);
			}
			mCheckedTextViewShowDifference.setVisibility(View.VISIBLE);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add(100, 100, 100, "Search");
		item.setIcon(android.R.drawable.ic_menu_search);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		mSearchView = new SearchView(this);
		item.setActionView(mSearchView);
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setQueryHint("Input digital only");
		mSearchView.setInputType(InputType.TYPE_CLASS_NUMBER);
		mSearchView.setOnSearchClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mCheckedTextViewShowDifference.setVisibility(View.GONE);
			}

		});
		mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {

			@Override
			public boolean onClose() {
				mCheckedTextViewShowDifference.setVisibility(View.VISIBLE);
				return false;
			}
		});
		menu.add(100, 101, 101, "Test");
		return super.onCreateOptionsMenu(menu);
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
				ChooseContactsForDisplay item = mAdapter.getItem(pos);
				SMSGroupEntryContactsItem smsItem = new SMSGroupEntryContactsItem(-1, (int) item.mContactsId, item.mName, item.mPhone, false);
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

	@Override
	public boolean onQueryTextSubmit(String query) {
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (!TextUtils.isDigitsOnly(newText)) {
			if (mToast == null) {
				mToast = Toast.makeText(this, newText, Toast.LENGTH_SHORT);
			}
			mToast.setText("Need all text is digits");
			mToast.show();
		} else {
			mAdapter.filter(newText);
//			Log.i(PREFIX, "onQueryTextChange " + newText);
		}
		return true;
	}

}
