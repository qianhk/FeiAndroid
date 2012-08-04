package com.njnu.kai.divsignin.qianglou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.njnu.kai.divsignin.DivSigninService;
import com.njnu.kai.divsignin.common.DivConst;

public class DivQiangLouReceiver extends BroadcastReceiver {

	private static String PREFIX = "[DivQiangLouReceiver]:";
	
	public interface DivQiangLouNotify {
		public abstract void notifyMessage(int type, String message);
	}

	private DivQiangLouNotify mNotify;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(DivConst.ACTION_QIANGLOU_NOTIFY)) {
			mNotify.notifyMessage(intent.getIntExtra("type", 0), intent.getStringExtra("message"));
		} else if (intent.getAction().equals(DivConst.ACTION_QIANGLOU_ALARM)) {
			Log.i(PREFIX, "Alarm time come, start qianglou service...");
			mNotify.notifyMessage(0, "Alarm time come, start qianglou service...");
			Intent intentSigninService = new Intent(context, DivSigninService.class);
			context.startService(intentSigninService);
		}
	}

	public DivQiangLouReceiver(DivQiangLouNotify notify) {
		super();
		mNotify = notify;
	}

}
