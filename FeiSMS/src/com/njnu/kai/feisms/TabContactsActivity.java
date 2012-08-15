package com.njnu.kai.feisms;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class TabContactsActivity extends BaseSelectedContactsActivity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(100, 102, 102, "Exclude Contacts");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 102: {
			Intent intent = new Intent(this, ExcludeContactsActivity.class);
			startActivity(intent);
		}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
