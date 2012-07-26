package com.njnu.kai.widget.time;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetTimeProvider extends AppWidgetProvider {

	private static final String FRESH = "com.njnu.kai.widgettime.fresh";
	private Context mContext;
	private static boolean mRun = false;
	private Thread myThread;
	
	public static class UpdateService extends Service {

		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}

		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			RemoteViews updateViews = new RemoteViews(this.getPackageName(), R.layout.main);
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			updateViews.setTextViewText(R.id.textview_time, sdf.format(new Date()));
			ComponentName thisWidget = new ComponentName(this, WidgetTimeProvider.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			manager.updateAppWidget(thisWidget, updateViews);
			return START_STICKY;
		}

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		String action = intent.getAction();
//		Log.v("TimeWidget", "onReceive " + action + " mRun=" + mRun);
		if (action.equals(FRESH)) {			
			Intent intent2 = new Intent(context, UpdateService.class);
			context.startService(intent2);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.v("TimeProvider", "onUpdate22222 context=" + context);
		mContext = context;
		mRun = true;
		Intent intent = new Intent(context, UpdateService.class);
		context.startService(intent);
//		super.onUpdate(context, appWidgetManager, appWidgetIds);
		myThread = new Thread() {

			@Override
			public void run() {
				while (mRun) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						// TODO: handle exception
					}
//					Log.v("WidgetTiemProvider", "Thread sleep 1000 " + mRun);
					mContext.sendBroadcast(new Intent(FRESH));
				}
			}
			
		};
		myThread.start();
//		context.registerReceiver()
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.v("TimeProvider", "onDeleted " + this + " context=" + mContext);
		super.onDeleted(context, appWidgetIds);
		mRun = false;
	}

	@Override
	public void onEnabled(Context context) {
		Log.v("TimeProvider", "onEnabled " + this + " context=" + mContext);
		super.onEnabled(context);
		mRun = true;
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		Log.v("TimeProvider", "onDisable " + this + " context=" + mContext);
		mRun = false;
	}

}
