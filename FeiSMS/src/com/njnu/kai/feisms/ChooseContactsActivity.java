package com.njnu.kai.feisms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ListView;

public class ChooseContactsActivity extends Activity {
	private static final String PREFIX = "ChooseContacts";
	private ListView mListViewContacts;
	private ChooseContactsAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Choose Contacts");
		setContentView(R.layout.choose_contacts);
		((CheckedTextView)findViewById(R.id.checkedtextview_display_different_number)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckedTextView ctv = (CheckedTextView)v;
				ctv.toggle();
				boolean checked = ctv.isChecked();
				Log.i(PREFIX, "Checked=" + checked);
			}
		});
		mListViewContacts = (ListView)findViewById(R.id.listview_contacts);
		mAdapter = new ChooseContactsAdapter(this);
		mListViewContacts.setAdapter(mAdapter);
		mListViewContacts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mAdapter.refreshContactsData(false);
	}
	
	public void button_add_clicked(View view) {
		Log.i(PREFIX, "button_add_clicked");
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}
	
	public void button_cancel_clicked(View view) {
		Log.i(PREFIX, "button_cancel_clicked");
		setResult(RESULT_CANCELED);
		finish();
	}
	
}
