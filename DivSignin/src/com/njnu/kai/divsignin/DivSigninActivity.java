package com.njnu.kai.divsignin;

import com.njnu.kai.divsignin.qianglou.DivQiangLouActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DivSigninActivity extends ListActivity {

	private static final String PREFIX = "DivSigninActivity";
	private String[] mFunction = { "QiangLou", "Other" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mFunction);
		setListAdapter(arrayAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.i(PREFIX, "onListItemClick position=" + position);

		switch (position) {
		case 0:
			Intent intent = new Intent(this, DivQiangLouActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		boolean operate = true;
		switch (item.getItemId()) {
		case 0:
			Intent intent = new Intent(this, DivPreferenceActivity.class);
			startActivity(intent);
			break;
			
		case 1:
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
			String divUserName = settings.getString("div_user_name", "");
			String divUserPw = settings.getString("div_user_pw", "");
			Log.i(PREFIX, "name:" + divUserName + " pw:" + divUserPw);

		default:
			operate = super.onMenuItemSelected(featureId, item);
			break;
		}
		return operate;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Preferences");
		menu.add(0, 1, 1, "Test Preferences");
		return super.onCreateOptionsMenu(menu);
	}
}