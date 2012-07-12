package com.njnu.kai.divsignin.qianglou;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.njnu.kai.divsignin.DivSigninActivity;
import com.njnu.kai.divsignin.DivSigninService;
import com.njnu.kai.divsignin.R;
import com.njnu.kai.divsignin.common.DivConst;
import com.njnu.kai.divsignin.qianglou.DivQiangLouReceiver.DivQiangLouNotify;

public class DivQiangLouActivity extends Activity implements DivQiangLouNotify {

	private static final String PREFIX = "DivQiangLouActivity";
	private boolean mDoingQiangLou = false;
	private Button mButtonQiangLou;
	private EditText mTextEditResult;
	private DivQiangLouReceiver mReceiver;
	private NotificationManager mNotificationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.div_qianglou);

		Log.i(PREFIX, "onCreate");

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
				if (mDoingQiangLou) {
					stopService(intent);
					mNotificationManager.cancel(R.string.app_name);
				} else {
					startService(intent);
					sendNotification();
				}
				mDoingQiangLou = !mDoingQiangLou;
			}

		});

		IntentFilter intentFilter = new IntentFilter(DivConst.ACTION_QIANGLOU_NOTIFY);
		intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
		mReceiver = new DivQiangLouReceiver(this);
		registerReceiver(mReceiver, intentFilter);
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	private void sendNotification() {
		
		Notification notify = new Notification(R.drawable.ic_launcher, "divsignin qianglouing", System.currentTimeMillis());
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
			stopService(new Intent(this, DivSigninService.class));
			mNotificationManager.cancel(R.string.app_name);
		}
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	public void notifyMessage(int type, String message) {
//		Log.i(PREFIX, "notifyMessage=" + message);
		mTextEditResult.append(message + "\n");
		if (type != 0) {
			mDoingQiangLou = false;
			mButtonQiangLou.setText("Start");
			mNotificationManager.cancel(R.string.app_name);
		}
	}

}
