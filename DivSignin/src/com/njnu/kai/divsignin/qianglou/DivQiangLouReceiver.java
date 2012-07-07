package com.njnu.kai.divsignin.qianglou;

import com.njnu.kai.divsignin.common.DivConst;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DivQiangLouReceiver extends BroadcastReceiver {

	public interface DivQiangLouNotify {
		public abstract void notifyMessage(int type, String message);
	}

	private DivQiangLouNotify mNotify;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(DivConst.ACTION_QIANGLOU_NOTIFY)) {
			mNotify.notifyMessage(intent.getIntExtra("type", 0), intent.getStringExtra("message"));
		}
	}

	public DivQiangLouReceiver(DivQiangLouNotify notify) {
		super();
		mNotify = notify;
	}

}
