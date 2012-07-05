package com.njnu.kai.divsignin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class DivSigninService extends Service {

	private static String PREFIX = "DivSigninService";
	private boolean mThreadContinue = true;

	private Thread mThreadQiangLou = new Thread(new Runnable() {

		@Override
		public void run() {
			doThreadQiangLou();
		}
	});

	private void doThreadQiangLou() {
		while (mThreadContinue) {
			Log.i(PREFIX, "doThreadQiangLou " + mThreadContinue);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mThreadQiangLou.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

}
