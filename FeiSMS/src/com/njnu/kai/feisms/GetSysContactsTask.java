package com.njnu.kai.feisms;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class GetSysContactsTask extends AsyncTask<Void, Void, ContactsData> {
	private ChooseContactsActivity mActivity;
	private static final String PREFIX = "GetSysContactsTask";

	public GetSysContactsTask(ChooseContactsActivity activity) {
		mActivity = activity;
	}

	@Override
	protected ContactsData doInBackground(Void... params) {
		Log.i(PREFIX, "doInBackground");
		return SMSUtils.getContactsData(mActivity);
	}

	@Override
	protected void onPreExecute() {
		Log.i(PREFIX, "onPreExecute");
	}

	@Override
	protected void onPostExecute(ContactsData result) {
		Log.i(PREFIX, "onPostExecute " + result.getContactsCount() + " " + result.getPhoneCount());
		mActivity.refreshListView();
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		SMSUtils.clearContactsData();
		Log.i(PREFIX, "onCancelled");
	}

}
