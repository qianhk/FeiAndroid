package com.njnu.kai.feisms;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class TabContactsActivity extends BaseSelectedContactsActivity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(200, 200, 200, "Reset Send State");
		menu.add(200, 201, 201, "Exclude Contacts");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 200:
			mDataManager.resetContactsSendState(mGroupId);
			refreshGroupContacts();
			break;
			
		case 201: {
			Intent intent = new Intent(this, ExcludeContactsActivity.class);
			startActivity(intent);
		}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mGroupId == FeiSMSConst.GROUP_ID_CREATE) {
			mGroupId = SMSUtils.getCurrentDetailGroupId();
			if (mGroupId > 0) {
				refreshGroupContacts();
			}
		}
	}
}
