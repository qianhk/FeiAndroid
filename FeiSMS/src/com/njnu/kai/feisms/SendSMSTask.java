package com.njnu.kai.feisms;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;

import com.njnu.kai.feisms.FeiSMSDataManager.PhoneDataForSend;
import com.njnu.kai.feisms.FeiSMSDataManager.PhoneInfoForIterator;

public class SendSMSTask extends AsyncTask<Integer, Integer, Boolean> {

	public interface UpdateSMSSendState {
		public void sendedSmsEntry(int contactsDbId, boolean sucess);

		public void sendComplete(boolean sucess);
	}

	private class SentReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int resultCode = getResultCode();
			mLastSentSucess = false;
			Log.e(PREFIX, "onReceive SentReceiver result=" + resultCode);
			switch (resultCode) {
			case Activity.RESULT_OK:
				mLastSentSucess = true;
				break;

			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				break;
			case SmsManager.RESULT_ERROR_RADIO_OFF:
				break;
			case SmsManager.RESULT_ERROR_NULL_PDU:
				break;

			default:
				break;
			}
			mSemaphore.release();
		}
	}

	private static final String PREFIX = "SendSMSTask";
	private UpdateSMSSendState mUpdateSmsSendState;
	private ProgressDialog mProgressDialog;
	private Context mContext;
	private int[] mGroudIds;
	private PhoneDataForSend mDataForSend;
	private FeiSMSDataManager mDataManager;
	private SmsManager mSmsManager;
	private SentReceiver mSentReceiver;
	private Semaphore mSemaphore;
//	private Handler mHandler = new Handler();
	private boolean mLastSentSucess;

	public SendSMSTask(Context context, UpdateSMSSendState updateSmsSendState, int[] groupIds) {
		mContext = context;
		mUpdateSmsSendState = updateSmsSendState;
		mGroudIds = groupIds;
		mDataManager = FeiSMSDataManager.getDefaultInstance(context);
		mSmsManager = SmsManager.getDefault();
	}

	private void sendSMS(String phoneNumber, String smsContent) {
		Log.i(PREFIX, "sendSMS " + phoneNumber + " " + smsContent);
		PendingIntent pIntentSendSms = PendingIntent.getBroadcast(mContext, 0, new Intent(FeiSMSConst.ACTION_SMS_SENT_STATUS), 0);
		mSmsManager.sendTextMessage(phoneNumber, null, smsContent, pIntentSendSms, null);
//			Thread.sleep(500);
//		mHandler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				Log.i(PREFIX, "run sendBroadcast");
//				mContext.sendBroadcast(new Intent(FeiSMSConst.ACTION_SMS_SENT_STATUS));
//			}
//		}, 5000);
	}

	@Override
	protected Boolean doInBackground(Integer... params) {
//		Log.i(PREFIX, "doInBackground: amount=" + params[0] + " allSize=" + params.length + " params type=" + params);

		try {
			for (Iterator<PhoneInfoForIterator> iter = mDataForSend.iterator(); iter.hasNext();) {
				if (isCancelled()) {
					return true;
				}
				PhoneInfoForIterator phoneInfo = iter.next();
				mSemaphore.acquire();
				sendSMS(phoneInfo.mPhoneNumber, phoneInfo.mGroupSMS);
				mSemaphore.acquire();
				mSemaphore.release();
				if (mLastSentSucess) {
					publishProgress(phoneInfo.mDbId);
				} else {
					return false;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDataForSend = mDataManager.getDataForSend(mGroudIds);
		mProgressDialog = new ProgressDialog(mContext);
		if (Build.VERSION.SDK_INT > 10) {
			mProgressDialog.setIconAttribute(android.R.attr.icon);
		}
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
		mSentReceiver = new SentReceiver();
		mContext.registerReceiver(mSentReceiver, new IntentFilter(FeiSMSConst.ACTION_SMS_SENT_STATUS));
		mSemaphore = new Semaphore(1);
	}

	@Override
	protected void onPostExecute(final Boolean result) {
		super.onPostExecute(result);
		Log.i(PREFIX, "onPostExecute");
		mContext.unregisterReceiver(mSentReceiver);
		mSemaphore = null;

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				mProgressDialog.dismiss();
				mProgressDialog = null;
				mUpdateSmsSendState.sendComplete(result);
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
		mContext.unregisterReceiver(mSentReceiver);
		mSemaphore = null;
		Log.i(PREFIX, "onCancelled " + mProgressDialog);
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
		mUpdateSmsSendState.sendComplete(false);
	}

}
