/**
 * 
 */
package com.njnu.kai.list;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * @author allin
 *
 */
public class MyListView2 extends Activity {

	private ListView listView;
	//private List<String> data = new ArrayList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		listView = new ListView(this);
		
		Cursor cursor = getContentResolver().query(People.CONTENT_URI, null, null, null, null);
		startManagingCursor(cursor);
		
		ListAdapter listAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_expandable_list_item_1, 
				cursor,
				new String[]{People.NAME}, 
				new int[]{android.R.id.text1});
		
		listView.setAdapter(listAdapter);
		setContentView(listView);
	}
	
	
}
