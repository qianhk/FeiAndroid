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

	private static final String PREFIX = "DivQiangLouActivity";
	private boolean mDoingQiangLou = false;
	private Button mButtonQiangLou;
	private EditText mTextEditResult;
	private DivQiangLouReceiver mReceiver;
	private NotificationManager mNotificationManager;
	private AlarmManager mAlarmManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.div_qianglou);

		IntentFilter intentFilter = new IntentFilter(DivConst.ACTION_QIANGLOU_NOTIFY);
		intentFilter.addAction(DivConst.ACTION_QIANGLOU_ALARM);
		intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
		mReceiver = new DivQiangLouReceiver(this);
		registerReceiver(mReceiver, intentFilter);
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		mButtonQiangLou = (Button) findViewById(R.id.button_start);
		mTextEditResult = (EditText) findViewById(R.id.edittext_result);

		mButtonQiangLou.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(PREFIX, "onClick");
				if (!mDoingQiangLou && !isQiangLouPreferencesValid()) {
					mTextEditResult.append("Check your qianglou preferences.\n");
					return;
				}
				Intent intent = new Intent(DivQiangLouActivity.this, DivSigninService.class);
				mButtonQiangLou.setText(mDoingQiangLou ? "Start" : "Stop");
				PendingIntent pintentAlarm = getQiangLouAlarmPendingIntent();
				if (mDoingQiangLou) {
					stopService(intent);
					mAlarmManager.cancel(pintentAlarm);
				} else {
					boolean isTimeQL = TimeUtility.isTimeToQiangLou();
					if (isTimeQL) {
						startService(intent);
					}
					Calendar cal = TimeUtility.getNextStartTime(isTimeQL);
					mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), TimeUtility.ONE_DAY_IN_MILLISECOND,
							pintentAlarm);
				}
				mDoingQiangLou = !mDoingQiangLou;
			}

		});
		Button btnTest = (Button) findViewById(R.id.button_test);
		btnTest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(DivQiangLouActivity.this, DivSigninService.class);
//				stopService(intent);
				boolean isTimeQL = TimeUtility.isTimeToQiangLou();
				Calendar cal = TimeUtility.getNextStartTime(isTimeQL);
				Log.i(PREFIX, "" + isTimeQL + " " + cal.getTime().toLocaleString() + " h=" + cal.get(Calendar.HOUR_OF_DAY));
			}
		});
	}

	private void sendNotification() {

		Notification notify = new Notification(R.drawable.ic_launcher_notify, "divsignin qianglouing", System.currentTimeMillis());
		notify.flags = Notification.FLAG_ONGOING_EVENT;
		Intent intent = new Intent(this, DivQiangLouActivity.class);
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
			mDoingQiangLou = false;
			stopService(new Intent(this, DivSigninService.class));
			mAlarmManager.cancel(getQiangLouAlarmPendingIntent());
		}
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	private PendingIntent getQiangLouAlarmPendingIntent() {
		Intent intentAlarm = new Intent(DivQiangLouActivity.this, DivQiangLouReceiver.class);
		intentAlarm.setAction(DivConst.ACTION_QIANGLOU_ALARM);
		PendingIntent pintentAlarm = PendingIntent.getBroadcast(DivQiangLouActivity.this, 1, intentAlarm, 0);
		return pintentAlarm;
	}

	@Override
	public void notifyMessage(int type, String message) {
//		Log.i(PREFIX, "notifyMessage=" + message);
		mTextEditResult.append(message + "\n");
		if (type != 0) {
			if (type == DivConst.TYPE_BROADCAST_QIANGLOU_SERVICE_START) {
				sendNotification();
				getWindow().getDecorView().setKeepScreenOn(true);
			} else {
				if (type != DivConst.TYPE_BROADCAST_QIANGLOU_SERVICE_STOP) {
					mDoingQiangLou = false;
					mButtonQiangLou.setText("Start");
					mAlarmManager.cancel(getQiangLouAlarmPendingIntent());
				}
				mNotificationManager.cancel(R.string.app_name);
				getWindow().getDecorView().setKeepScreenOn(false);
			}
		}
	}

}
