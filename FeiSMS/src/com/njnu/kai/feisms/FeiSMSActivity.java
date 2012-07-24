package com.njnu.kai.feisms;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class FeiSMSActivity extends ListActivity {
	private FeiSMSDataManager mDataManager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_contacts_preview);
		mDataManager = FeiSMSDataManager.getDefaultInstance(this);
//		mDataManager.AppendSMSGroup("groupName1", "groupSmS1");
//		mDataManager.AppendSMSGroup("groupName2", "groupSmS2");
//		mDataManager.AppendSMSGroup("groupName3", "groupSmS3");
//		mDataManager.AppendContactsToGroup(1, 1, "contactsName1", "contactsPhoneNumber1");
//		mDataManager.AppendContactsToGroup(1, 2, "contactsName2", "contactsPhoneNumber2");
//		mDataManager.AppendContactsToGroup(1, 3, "contactsName3", "contactsPhoneNumber3");
//		mDataManager.AppendContactsToGroup(2, 4, "contactsName4", "contactsPhoneNumber4");
//		mDataManager.AppendContactsToGroup(2, 5, "contactsName5", "contactsPhoneNumber5");
//		mDataManager.AppendContactsToGroup(2, 6, "contactsName6", "contactsPhoneNumber6");
//		mDataManager.AppendContactsToGroup(2, 7, "contactsName77", "contactsPhoneNumber7");
//		String[] aa = { "Test1", "Test2" };
//		ArrayAdapter<String> a = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, aa);
//		setListAdapter(a);

		List<Hashtable<String, Object>> listContent = new ArrayList<Hashtable<String, Object>>();
		for (int i = 0; i < 5; ++i) {
			Hashtable<String, Object> table = new Hashtable<String, Object>();
			table.put("group", "Group" + i);
			table.put("persons", "Persons" + i);
			listContent.add(table);
		}
		ListAdapter adapter = new SimpleAdapter(this, listContent, android.R.layout.simple_list_item_2, new String[] { "group", "persons" },
				new int[] { android.R.id.text1, android.R.id.text2 });
		setListAdapter(adapter);
		
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
		menu.add(0, 0, 0, "Add Group");
		menu.add(0, 1, 1, "Modify Group");
		menu.add(0, 2, 2, "Delete Group");
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
}
