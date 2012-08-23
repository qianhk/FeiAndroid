package com.njnu.kai.activitytest;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;

public class MainActivity extends BaseActivity {

	@Override
	protected String getButtonText() {
		return "to Second";
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		sendQLBeginNotification(this);
	}

	@Override
	protected Intent getStartupIntent() {
		Intent intent = new Intent(this, SecondActivity.class);
//		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.weibo.cn"));
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

	private void sendQLBeginNotification(Context context) {
		Notification notify = new Notification(R.drawable.ic_launcher, "divsignin qianglouing", System.currentTimeMillis());
		notify.flags = Notification.FLAG_ONGOING_EVENT;
		Intent intent = new Intent(context, SecondActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		notify.setLatestEventInfo(context, "divsignin qianglou", "qianglouing......", contentIntent);
		NotificationManager notifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notifyMgr.notify(R.string.app_name, notify);
	}

}
