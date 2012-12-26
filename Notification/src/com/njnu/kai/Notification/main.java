package com.njnu.kai.Notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;


public class main extends Activity {
	public static final String ACTION_COM_NJNU_KAI_TEST_PREVIOUS = "com.njnu.kai.test.previous";
	public static final String ACTION_COM_NJNU_KAI_TEST_PLAYPAUSE = "com.njnu.kai.test.playpause";
	public static final String ACTION_COM_NJNU_KAI_TEST_NEXT = "com.njnu.kai.test.next";
	int notification_id = 198383;
	NotificationManager nm;
	Handler handler = new Handler();
	Notification notification;
	int count = 0;

	private static boolean mDefaultFontHasExtracted = false;
    private static int mDefaultNotificationTitleFontColor;
    private static int mDefaultNotificationTextFontColor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button bt1 = (Button)findViewById(R.id.bt1);
		bt1.setOnClickListener(bt1lis);
		Button bt2 = (Button)findViewById(R.id.bt2);
		bt2.setOnClickListener(bt2lis);
		Button bt3 = (Button)findViewById(R.id.btn_dialog);
		bt3.setOnClickListener(btClicked);
		final IntentFilter filter = new IntentFilter(ACTION_COM_NJNU_KAI_TEST_PREVIOUS);
		filter.addAction(ACTION_COM_NJNU_KAI_TEST_PLAYPAUSE);
		filter.addAction(ACTION_COM_NJNU_KAI_TEST_NEXT);
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();
			}
		}, filter);

		nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		createNotification0();
	}

	private void createNotification1() {
		notification = new Notification(R.drawable.home, "图标边的文字1", System.currentTimeMillis());
		notification.contentView = new RemoteViews(getPackageName(), R.layout.notification);
		PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, new Intent(ACTION_COM_NJNU_KAI_TEST_PREVIOUS), 0);
		notification.contentView.setOnClickPendingIntent(R.id.iv_star, broadcast);
		broadcast = PendingIntent.getBroadcast(this, 102, new Intent(ACTION_COM_NJNU_KAI_TEST_NEXT), 0);
		notification.contentView.setOnClickPendingIntent(R.id.iv_next, broadcast);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			notification.contentView.setViewVisibility(R.id.iv_star, View.GONE);
		}
		Intent notificationIntent = new Intent(this, main.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.contentIntent = contentIntent;
	}

	private void createNotification0() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.home)
				.setContentTitle("My notification")
				.setContentText("Hello World!");
		Intent resultIntent = new Intent(this, main.class);
		Intent notificationIntent = new Intent(this, main.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		mBuilder.setContentIntent(contentIntent);
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
		extractDefaultTextStyle(this); //即使用了不同的sytle，但是由于很多自制固件未修改系统风格代表的颜色值，但却改了通知栏背景色，仍有可能导致看看不清文字颜色
		remoteViews.setTextColor(R.id.title, mDefaultNotificationTitleFontColor);
		mBuilder.setContent(remoteViews);
//		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//
//		String[] events = new String[4];
//		inboxStyle.setBigContentTitle("My notification2");
//		inboxStyle.setSummaryText("Hello World2");
//		// Moves events into the big view
//		for (int i=0; i < events.length; i++) {
//			events[i] = "Kai line: " + i;
//		    inboxStyle.addLine(events[i]);
//		}
//		// Moves the big view style object into the notification object.
//		mBuilder.setStyle(inboxStyle);

		notification = mBuilder.build();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			notification.bigContentView = new RemoteViews(getPackageName(), R.layout.notification_big);
		}
	}

	private void createNotification2() {
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.home)
						.setContentTitle("My notification")
						.setContentText("Hello World!");
		Intent resultIntent = new Intent(this, main.class);

//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//		stackBuilder.addParentStack(main.class);
//		stackBuilder.addNextIntent(resultIntent);
//		PendingIntent resultPendingIntent =
//				stackBuilder.getPendingIntent(
//						0,
//						PendingIntent.FLAG_UPDATE_CURRENT
//						);
		Intent notificationIntent = new Intent(this, main.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		mBuilder.setContentIntent(contentIntent);

		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		String[] events = new String[6];
		inboxStyle.setBigContentTitle("My notification2");
		inboxStyle.setSummaryText("Hello World2");
		// Moves events into the big view
		for (int i = 0; i < events.length; i++) {
			events[i] = "Kai line: " + i;
			inboxStyle.addLine(events[i]);
		}
		// Moves the big view style object into the notification object.
		mBuilder.setStyle(inboxStyle);
		mBuilder.addAction(R.drawable.home, "Test2", contentIntent);
		mBuilder.addAction(R.drawable.home, "Test3", contentIntent);
		mBuilder.addAction(R.drawable.home, "Test4", contentIntent);
		mBuilder.addAction(R.drawable.home, "Test5", contentIntent);

		notification = mBuilder.build();
	}

	private void createNotification3() {
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.home)
						.setContentTitle("My notification")
						.setContentText("Hello World!");
		Intent resultIntent = new Intent(this, main.class);
		Intent notificationIntent = new Intent(this, main.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		mBuilder.setContentIntent(contentIntent);

		NotificationCompat.BigPictureStyle bigPicStyle = new NotificationCompat.BigPictureStyle();
		Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.home);
		bigPicStyle.bigPicture(bit);
		bigPicStyle.setBigContentTitle("My notification3");
		bigPicStyle.setSummaryText("Hello World3");

		// Moves the big view style object into the notification object.
		mBuilder.setStyle(bigPicStyle);
		mBuilder.addAction(R.drawable.home, "Test2", contentIntent);
		mBuilder.addAction(R.drawable.home, "Test3", contentIntent);
		mBuilder.addAction(R.drawable.home, "Test4", contentIntent);
		mBuilder.addAction(R.drawable.home, "Test5", contentIntent);

		notification = mBuilder.build();
	}

	OnClickListener bt1lis = new OnClickListener() {

		@Override
		public void onClick(View v) {
			showNotification();
			handler.post(run);
		}

	};
	Runnable run = new Runnable() {

		@Override
		public void run() {
//            count++;
//            notification.contentView.setProgressBar(R.id.pb, 100,count, false);
//            showNotification();
//            if(count<100) handler.postDelayed(run, 200);
		}

	};
	OnClickListener bt2lis = new OnClickListener() {

		@Override
		public void onClick(View v) {
			nm.cancel(notification_id);
 		}

	};
	OnClickListener btClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			MenuMoreDialog dlg = new MenuMoreDialog(main.this, R.style.transparent_dialog);
			dlg.show();
		}

	};

	public void showNotification() {
		nm.notify(notification_id, notification);
	}

	 private final static String NOTIFICATION_TITLE_TEST_TAG = "{notification_title_test_tag}";
	    private final static String NOTIFICATION_TEXT_TEST_TAG = "{notification_text_test_tag}";

	private static void extractDefaultTextStyle(Context context) {
        if (!mDefaultFontHasExtracted) {
            try {
                Notification ntf = new Notification();
                ntf.setLatestEventInfo(context, NOTIFICATION_TITLE_TEST_TAG, NOTIFICATION_TEXT_TEST_TAG, null);
                LinearLayout group = new LinearLayout(context);
                ViewGroup event = (ViewGroup) ntf.contentView.apply(context, group);
                recurseGroup(context, event);
                group.removeAllViews();
                mDefaultFontHasExtracted = true;
            } catch (Exception ignored) {
                mDefaultFontHasExtracted = false;
            }
        }
    }

	private static void recurseGroup(Context context, ViewGroup gp) {
        Resources res = context.getResources();
        final int count = gp.getChildCount();
        for (int i = 0; i < count; ++i) {
            if (gp.getChildAt(i) instanceof TextView) {
                final TextView text = (TextView) gp.getChildAt(i);
                final String szText = text.getText().toString();
                if (NOTIFICATION_TITLE_TEST_TAG.equals(szText)) {
                    mDefaultNotificationTitleFontColor = text.getTextColors().getDefaultColor();
//                    mDefaultNotificationTitleFontSize = TypedValue.applyDimension(
//                            TypedValue.COMPLEX_UNIT_SP, text.getTextSize(), res.getDisplayMetrics());
                } else if (NOTIFICATION_TEXT_TEST_TAG.equals(szText)) {
                    mDefaultNotificationTextFontColor = text.getTextColors().getDefaultColor();
//                    mDefaultNotificationTextFontSize = TypedValue.applyDimension(
//                            TypedValue.COMPLEX_UNIT_SP, text.getTextSize(), res.getDisplayMetrics());
                }
            } else if (gp.getChildAt(i) instanceof ViewGroup) {
                recurseGroup(context, (ViewGroup) gp.getChildAt(i));
            }
        }
    }
}
