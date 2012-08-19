package com.njnu.kai.feisms;

import java.util.Iterator;

import com.njnu.kai.feisms.FeiSMSDataManager.PhoneDataForSend;
import com.njnu.kai.feisms.FeiSMSDataManager.PhoneInfoForIterator;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class SendSMSTask extends AsyncTask<Integer, Integer, Integer> {

	public interface UpdateSMSSendState {
		public void sendedSmsEntry(int contactsDbId, boolean sucess);
		public void sendComplete(boolean sucess);
	}

	private static final String PREFIX = "SendSMSTask";
	private UpdateSMSSendState mUpdateSmsSendState;
	private ProgressDialog mProgressDialog;
	private Context mContext;
	private int[] mGroudIds;
	private PhoneDataForSend mDataForSend;
	private FeiSMSDataManager mDataManager;

	public SendSMSTask(Context context, UpdateSMSSendState updateSmsSendState, int[] groupIds) {
		mContext = context;
		mUpdateSmsSendState = updateSmsSendState;
		mGroudIds = groupIds;
		mDataManager = FeiSMSDataManager.getDefaultInstance(context);
	}

	private void sendSMS(String phoneNumber, String smsContent) {
		try {
			Log.i(PREFIX, "sendSMS " + phoneNumber + " " + smsContent);
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
	}
	
	@Override
	protected Integer doInBackground(Integer... params) {
//		Log.i(PREFIX, "doInBackground: amount=" + params[0] + " allSize=" + params.length + " params type=" + params);
		for (Iterator<PhoneInfoForIterator> iter = mDataForSend.iterator(); iter.hasNext(); ) {
			if (isCancelled()) {
				return 0;
			}
			PhoneInfoForIterator phoneInfo = iter.next();
			sendSMS(phoneInfo.mPhoneNumber, phoneInfo.mGroupSMS);
			publishProgress(phoneInfo.mDbId);
		}
		return 0;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDataForSend = mDataManager.getDataForSend(mGroudIds);
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setIconAttribute(android.R.attr.icon);
		mProgressDialog.setTitle("Sending sms...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		Log.i(PREFIX, "onPreExecute max is: " + mDataForSend.getCount());
		mProgressDialog.setMax(mDataForSend.getCount());
//		mProgressDialog.setProgress(0);
//		mProgressDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel", new AlertDialog.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//			}
//		});
		mProgressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
//				Log.i(PREFIX, "dialog onCancel");
				SendSMSTask.this.cancel(true);
			}
		});
		mProgressDialog.show();
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		Log.i(PREFIX, "onPostExecute");
		mUpdateSmsSendState.sendComplete(true);
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
		}, 300);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
//		Log.i(PREFIX, "onProgressUpdate " + values + " size=" + values.length + " 0=" + values[0]);
//		mProgressDialog.setProgress(values[0]);
		mProgressDialog.incrementProgressBy(1);
		mDataManager.setContactsSendState(values[0], true);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		Log.i(PREFIX, "onCancelled " + mProgressDialog);
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
		mUpdateSmsSendState.sendComplete(false);
	}

}
