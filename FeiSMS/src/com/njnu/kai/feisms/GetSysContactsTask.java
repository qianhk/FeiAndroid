package com.njnu.kai.feisms;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class GetSysContactsTask extends AsyncTask<Void, Void, ContactsData> {
	private ChooseContactsActivity mActivity;
	private ProgressDialog mProgressDialog;
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
		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("Getting Sys Contacts...");
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
	}

	@Override
	protected void onPostExecute(ContactsData result) {
		Log.i(PREFIX, "onPostExecute " + result.getContactsCount() + " " + result.getPhoneCount());
		mActivity.refreshListView();
		mProgressDialog.dismiss();
		mProgressDialog = null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
		SMSUtils.clearContactsData();
		Log.i(PREFIX, "onCancelled");
	}

}
