package com.njnu.kai.feiphoneinfo;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class GPUActivity extends Activity implements OnItemClickListener {
	private ListView listView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		listView = new ListView(this);

		// Cursor cursor = getContentResolver().query(Contacts.CONTENT_URI,
		// null, null, null, null);
		Cursor cursor = getContentResolver().query(Contacts.CONTENT_URI, new String[] { Contacts._ID, Contacts.DISPLAY_NAME }, null, null,
				null);
		startManagingCursor(cursor);

		ListAdapter listAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor,
				new String[] { Contacts.DISPLAY_NAME }, new int[] { android.R.id.text1 });

		listView.setAdapter(listAdapter);
		setContentView(listView);

		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.v("GPU", "Clicked " + id + " " + ((TextView) view).getText());

	}

}
