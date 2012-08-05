package com.njnu.kai.divsignin.qianglou;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.njnu.kai.divsignin.DivSigninActivity;
import com.njnu.kai.divsignin.DivSigninService;
import com.njnu.kai.divsignin.R;
import com.njnu.kai.divsignin.common.DivConst;
import com.njnu.kai.divsignin.common.TimeUtility;
import com.njnu.kai.divsignin.qianglou.DivQiangLouReceiver.DivQiangLouNotify;

public class DivQiangLouActivity extends Activity implements DivQiangLouNotify {

	private static final String KEY_DOING_QL = "doingQL";
	private static final String PREFIX = "DivQiangLouActivity";
	private boolean mDoingQiangLou = false;
	private Button mButtonQiangLou;
	private Button mButtonQiangLou2;
	private EditText mTextEditResult;
	private DivQiangLouReceiver mReceiver;
	private NotificationManager mNotificationManager;
	private AlarmManager mAlarmManager;

	View.OnClickListener mBtnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
//			Log.i(PREFIX, "onClick");
			if (!mDoingQiangLou && !isQiangLouPreferencesValid()) {
				mTextEditResult.append("Check your qianglou preferences.\n");
				return;
			}
			Intent intent = new Intent(DivQiangLouActivity.this, DivSigninService.class);
			mButtonQiangLou.setText(mDoingQiangLou ? "Start2" : "Stop");
			mButtonQiangLou2.setText(mDoingQiangLou ? "NextDay2" : "Stop");
			PendingIntent pintentAlarm = getQiangLouAlarmPendingIntent();
			if (mDoingQiangLou) {
				stopService(intent);
				mAlarmManager.cancel(pintentAlarm);
				mNotificationManager.cancel(R.string.app_name);
			} else {
				mTextEditResult.setText("");
				boolean isTimeQL = true;
				if (v.getId() == R.id.button_start) {
					isTimeQL = TimeUtility.isTimeToQiangLou();
					if (isTimeQL) {
						startService(intent);
					}
				} else {
					notifyMessage(0, "Waiting for next qianglou...");
				}
				Calendar cal = TimeUtility.getNextStartTime(isTimeQL);
				mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintentAlarm);
			}
			mDoingQiangLou = !mDoingQiangLou;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.div_qianglou);
		Log.e(PREFIX, "onCreate() " + mDoingQiangLou + " savedInstanceState=" + savedInstanceState);
		IntentFilter intentFilter = new IntentFilter(DivConst.ACTION_QIANGLOU_NOTIFY);
		intentFilter.addAction(DivConst.ACTION_QIANGLOU_ALARM);
//		intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
		mReceiver = new DivQiangLouReceiver(this);
		registerReceiver(mReceiver, intentFilter);
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

//		Intent intentAlarm = new Intent();
//		intentAlarm.setAction(DivConst.ACTION_QIANGLOU_ALARM);
//		PendingIntent pintentAlarm = PendingIntent.getBroadcast(DivQiangLouActivity.this, 1, intentAlarm, PendingIntent.FLAG_NO_CREATE);
//		Log.e(PREFIX, "onCreate() pintentAlarm is " + pintentAlarm);

		mButtonQiangLou = (Button) findViewById(R.id.button_start);
		mButtonQiangLou2 = (Button) findViewById(R.id.button_nextday);
		mTextEditResult = (EditText) findViewById(R.id.edittext_result);

		mButtonQiangLou.setOnClickListener(mBtnClickListener);
		mButtonQiangLou2.setOnClickListener(mBtnClickListener);
		Button btnTest = (Button) findViewById(R.id.button_test);
		btnTest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(DivQiangLouActivity.this, DivSigninService.class);
//				stopService(intent);
				boolean isTimeQL = TimeUtility.isTimeToQiangLou();
				Calendar cal = TimeUtility.getNextStartTime(isTimeQL);
				Log.i(PREFIX, "" + isTimeQL + " " + cal.getTime().toLocaleString() + " h=" + cal.get(Calendar.HOUR_OF_DAY));

				Intent intentNotify = new Intent();
				intentNotify.setAction(DivConst.ACTION_QIANGLOU_NOTIFY);
				intentNotify.putExtra("type", 0);
				intentNotify.putExtra("message", "test notify.");
				sendBroadcast(intentNotify);
				
//				sendNotification();
//
//				Intent intentAlarm = new Intent();
//				intentAlarm.setAction(DivConst.ACTION_QIANGLOU_ALARM);
//				sendBroadcast(intentAlarm);
			}
		});

//		Button btnTest2 = (Button) findViewById(R.id.button_test2);
//		btnTest2.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(DivQiangLouActivity.this, DivSigninService.class);
//				stopService(intent);
//				mNotificationManager.cancel(R.string.app_name);
//			}
//		});
		
		if (savedInstanceState != null && savedInstanceState.containsKey(KEY_DOING_QL)) {
			mDoingQiangLou = savedInstanceState.getBoolean(KEY_DOING_QL);
			Log.i(PREFIX, "onCreate savedState " + mDoingQiangLou + "\n" + savedInstanceState);
			notifyMessage(0, "onCreate savedState " + mDoingQiangLou + "\n" + savedInstanceState);
			if (mDoingQiangLou) {
				mButtonQiangLou.setText("Stop2");
				mButtonQiangLou2.setText("Stop2");
			}
		}
		
	}

	private void sendNotification() {
		Notification notify = new Notification(R.drawable.ic_launcher_notify, "divsignin qianglouing", System.currentTimeMillis());
		notify.flags = Notification.FLAG_ONGOING_EVENT;
		Intent intent = new Intent(this, DivQiangLouStatus.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(this, R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		notify.setLatestEventInfo(this, "divsignin qianglou", "qianglouing......", contentIntent);
		mNotificationManager.notify(R.string.app_name, notify);
	}

	private boolean isQiangLouPreferencesValid() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String devAccountUid = settings.getString("div_user_uid", "");
		String devAccountName = settings.getString("div_user_name", "");
		String devAccountPw = settings.getString("div_user_pw", "");
		String qiangLouTitle = settings.getString("divsignin_qianglou_title", "");
		String qiangLouContent = settings.getString("divsignin_qianglou_content", "");
		boolean invalid = TextUtils.isEmpty(devAccountUid) || TextUtils.isEmpty(devAccountName) || TextUtils.isEmpty(devAccountPw)
				|| TextUtils.isEmpty(qiangLouTitle) || TextUtils.isEmpty(qiangLouContent);
		return !invalid;
	}

	@Override
	protected void onDestroy() {
		Log.i(PREFIX, "onDestroy");
		if (mDoingQiangLou) {
			Log.e(PREFIX, "still Doing Qianglou, but onDestroy().");
			mDoingQiangLou = false;
			stopService(new Intent(this, DivSigninService.class));
			mAlarmManager.cancel(getQiangLouAlarmPendingIntent());
		}
		unregisterReceiver(mReceiver);
//		mNotificationManager.cancel(R.string.app_name);
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.e(PREFIX, "onSaveInstanceState() " + mDoingQiangLou);
		outState.putBoolean(KEY_DOING_QL, mDoingQiangLou);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.e(PREFIX, "onRestoreInstanceState() " + mDoingQiangLou);
		if (savedInstanceState.containsKey(KEY_DOING_QL)) {
			mDoingQiangLou = savedInstanceState.getBoolean(KEY_DOING_QL);
			notifyMessage(0, "onRestoreInstanceState " + mDoingQiangLou + "\n" + savedInstanceState);
			if (mDoingQiangLou) {
				mButtonQiangLou.setText("Stop3");
				mButtonQiangLou2.setText("Stop3");
			}
		}
	}

//	@Override
//	public Object onRetainNonConfigurationInstance() {
//		Log.e(PREFIX, "onRetainNonConfigurationInstance() " + mDoingQiangLou);
//		return super.onRetainNonConfigurationInstance();
//	}

	private PendingIntent getQiangLouAlarmPendingIntent() {
		Intent intentAlarm = new Intent();
		intentAlarm.setAction(DivConst.ACTION_QIANGLOU_ALARM);
		PendingIntent pintentAlarm = PendingIntent.getBroadcast(DivQiangLouActivity.this, 1, intentAlarm, 0);
		return pintentAlarm;
	}

	@Override
	public void notifyMessage(int type, String message) {
//		Log.i(PREFIX, "notifyMessage=" + message);
		Calendar cal = Calendar.getInstance();
		mTextEditResult.append("[" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND) + "." + cal.get(Calendar.MILLISECOND) + "]: " + message + "\n");
		if (type != 0) {
			if (type == DivConst.TYPE_BROADCAST_QIANGLOU_SERVICE_START) {
				sendNotification();
				getWindow().getDecorView().setKeepScreenOn(true);
			} else {
				if (type == DivConst.TYPE_BROADCAST_QIANGLOU_SERVICE_STOP) {
					mTextEditResult.append("Waiting for next qianglou...\n");
				} else {
					mDoingQiangLou = false;
					mButtonQiangLou.setText("Start3");
					mButtonQiangLou2.setText("NextDay3");
					mAlarmManager.cancel(getQiangLouAlarmPendingIntent());
				}
				mNotificationManager.cancel(R.string.app_name);
				getWindow().getDecorView().setKeepScreenOn(false);
			}
		}
		mTextEditResult.setSelection(mTextEditResult.length());
	}

}
