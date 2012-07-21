package com.njnu.kai.feisms;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class FeiSMSActivity extends ListActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_contact_preview);
		// String[] aa = {"Test1", "Test2"};
		// ArrayAdapter<String> a = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aa);
		// setListAdapter(a);

		List<Hashtable<String, Object>> listContent = new ArrayList<Hashtable<String, Object>>();

		for (int i = 0; i < 5; ++i) {
			Hashtable<String, Object> table = new Hashtable<String, Object>();
			table.put("group", "Group" + i);
			table.put("persons", "Persons" + i);

			listContent.add(table);
		}

		ListAdapter adapter = new SimpleAdapter(this, listContent, android.R.layout.simple_list_item_2,
				new String[] { "group", "persons" }, new int[] { android.R.id.text1, android.R.id.text2});

		setListAdapter(adapter);
	}
}
