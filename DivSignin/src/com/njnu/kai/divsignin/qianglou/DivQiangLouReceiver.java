package com.njnu.kai.divsignin.qianglou;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.njnu.kai.divsignin.DivSigninService;
import com.njnu.kai.divsignin.R;
import com.njnu.kai.divsignin.common.DivConst;

public class DivQiangLouReceiver extends BroadcastReceiver {

	private static String PREFIX = "[DivQiangLouReceiver]:";
	
	private void sendInternalBroadcast(Context context, int type, String message) {
		Intent intent = new Intent(DivConst.ACTION_QIANGLOU_NOTIFY_INTERNAL);
		intent.putExtra("type", type);
		intent.putExtra("message", message);
		context.sendBroadcast(intent);
	}
	
	private void sendQLBeginNotification(Context context) {
		Notification notify = new Notification(R.drawable.ic_launcher_notify, "divsignin qianglouing", System.currentTimeMillis());
		notify.flags = Notification.FLAG_ONGOING_EVENT;
		Intent intent = new Intent(context, DivQiangLouActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		notify.setLatestEventInfo(context, "divsignin qianglou", "qianglouing......", contentIntent);
		NotificationManager notifyMgr = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		notifyMgr.notify(R.string.app_name, notify);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(DivConst.ACTION_QIANGLOU_NOTIFY)) {
			int type = intent.getIntExtra("type", 0);
			sendInternalBroadcast(context, type, intent.getStringExtra("message"));
			if (type != 0) {
				if (type == DivConst.TYPE_BROADCAST_QIANGLOU_SERVICE_START) {
					sendQLBeginNotification(context);
				} else {
					if (type == DivConst.TYPE_BROADCAST_QIANGLOU_SERVICE_STOP) {
					} else {
						AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
						alarmMgr.cancel(DivQiangLouActivity.getQiangLouAlarmPendingIntent(context));
						SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
						settings.edit().putBoolean(DivConst.KEY_DIV_QIANGLOU_EXCEPTION, true).commit();
					}
					NotificationManager notifyMgr = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
					notifyMgr.cancel(R.string.app_name);
				}
			}
		} else if (intent.getAction().equals(DivConst.ACTION_QIANGLOU_ALARM)) {
			Log.e(PREFIX, "Alarm time come, start qianglou service...");
			sendInternalBroadcast(context, 0, "Alarm time come, start qianglou service...");
			Intent intentSigninService = new Intent(context, DivSigninService.class);
			context.startService(intentSigninService);
		}
	}

}
