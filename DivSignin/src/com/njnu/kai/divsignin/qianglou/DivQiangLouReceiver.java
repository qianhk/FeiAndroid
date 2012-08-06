package com.njnu.kai.divsignin.qianglou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.njnu.kai.divsignin.DivSigninService;
import com.njnu.kai.divsignin.common.DivConst;

public class DivQiangLouReceiver extends BroadcastReceiver {

	private static String PREFIX = "[DivQiangLouReceiver]:";
	
	private void sendInternalBroadcast(Context context, int type, String message) {
		Intent intent = new Intent(DivConst.ACTION_QIANGLOU_NOTIFY_INTERNAL);
		intent.putExtra("type", type);
		intent.putExtra("message", message);
		context.sendBroadcast(intent);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(DivConst.ACTION_QIANGLOU_NOTIFY)) {
			sendInternalBroadcast(context, intent.getIntExtra("type", 0), intent.getStringExtra("message"));
		} else if (intent.getAction().equals(DivConst.ACTION_QIANGLOU_ALARM)) {
			Log.e(PREFIX, "Alarm time come, start qianglou service...");
			sendInternalBroadcast(context, 0, "Alarm time come, start qianglou service...");
			Intent intentSigninService = new Intent(context, DivSigninService.class);
			context.startService(intentSigninService);
		}
	}

}
