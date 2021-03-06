package com.njnu.kai.bmi;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BMIActivity extends Activity implements OnClickListener, HttpAsyncTaskNotify {
	private final static String LOG_TAG = "BMIActivity";

	private ImageView mIvTestLayer1;
	private ImageView mIvTestLayer2;
	private View mLayout1;
	private View mLayout2;
	private EditText mEdtResult;
	private StringBuilder mBuilder;
	private String mUserInfo;

	public double calcBMI(double weight, double height) {
		double BMI = weight / (height * height);
		return BMI;
	}

	private Button.OnClickListener calcBMI = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.submit) {
				DecimalFormat nf = new DecimalFormat("0.00");
				try {
					EditText fieldHeight = (EditText)findViewById(R.id.stature);
					EditText fieldWeight = (EditText)findViewById(R.id.weight);
					double height = Double.parseDouble(fieldHeight.getText().toString()) / 100;
					double weight = Double.parseDouble(fieldWeight.getText().toString());
					double BMI = calcBMI(weight, height);

					TextView result = (TextView)findViewById(R.id.result);
					result.setText("Your BMI is " + nf.format(BMI));
					TextView fieldSuggest = (TextView)findViewById(R.id.suggest);
					if (BMI > 25) {
						fieldSuggest.setText(R.string.advice_heavy);
					} else if (BMI < 20) {
						fieldSuggest.setText(R.string.advice_light);
					}
					else {
						fieldSuggest.setText(R.string.advice_average);
					}
				}
				catch (Exception err) {
	//				Toast.makeText(BMIActivity.this, "error", Toast.LENGTH_SHORT).show();
				}
	//			openOptionDialog();
//				mBuilder.setLength(0);
//				new HttpAsyncTask(BMIActivity.this, BMIActivity.this).execute(R.id.iv_color_blue);
//				sendQLBeginNotification("正在获取Blue网络数据...");
			} else if (v.getId() == R.id.submit2) {
//				sendQLBeginNotification("正在发送邮件...");
//				new HttpAsyncTask(BMIActivity.this, BMIActivity.this).execute(1, mUserInfo + "\n\n" + mBuilder.toString());
//				sendLogEmail();
			}
		}
	};

	private void openOptionDialog() {
//    	Toast.makeText(BMIActivity.this, "haha", Toast.LENGTH_SHORT).show();
		new AlertDialog.Builder(BMIActivity.this).setTitle(R.string.about_title).setMessage(R.string.about_msg)
				.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
//				Uri uri = Uri.parse("http://wap.google.com");
//				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//				startActivity(intent);
					}
				})
				.show();
	}

	protected static final int MENU_ABOUT = Menu.FIRST;
	protected static final int MENU_Quit = Menu.FIRST + 1;
	protected static final int MENU_Email = Menu.FIRST + 2;
	protected static final int MENU_Email_Clear = Menu.FIRST + 3;
	protected static final int MENU_Email_DIRECT = Menu.FIRST + 4;
	protected static final int MENU_TEST_STH = Menu.FIRST + 5;
	private Drawable mDrawableLauncher2;
	private Drawable mDrawableApple;
	private Drawable mDrawableLauncher1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_Email_DIRECT, 0, "直接发送日志邮件");
		menu.add(0, MENU_Email, 100, "使用系统邮件程序发送");
		menu.add(0, MENU_Email_Clear, 200, "清空测试数据");
		menu.add(0, MENU_ABOUT, 300, "About...");
		menu.add(0, MENU_TEST_STH, 350, "Test sth");
		menu.add(0, MENU_Quit, 400, "Exit");
		return true;
	}

	public static boolean isAppOnForground(Context context) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        String  packageName =  context.getPackageName();
        List<ActivityManager.RunningAppProcessInfo>  appProcess = activityManager.getRunningAppProcesses();
        if (appProcess == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcessInfo: appProcess) {
             if (appProcessInfo.processName.equals(packageName) && appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                 return true;
             }
        }

        return false;
    }

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_ABOUT:
//			openOptionDialog();
		{
			boolean appOnForground = isAppOnForground(this);
			Log.i("BMIActivity", "isAppOnForground=" + appOnForground);
		}
			break;

		case MENU_Quit:
			finish();
			break;

		case MENU_Email:
			sendLogEmail();
			break;

		case MENU_Email_Clear:
			mBuilder.setLength(0);
			break;

		case MENU_Email_DIRECT:
			sendQLBeginNotification("正在发送邮件...");
			new HttpAsyncTask(BMIActivity.this, BMIActivity.this).execute(1, mUserInfo + "\n\n" + mBuilder.toString());
			break;

		case MENU_TEST_STH:
			testSomeThing();
			break;

		default:
			break;
		}

		return true;
	}

	private void testSomeThing() {
		try {
			Log.d(LOG_TAG, "Environment.getExternalStorageDirectory()=" + Environment.getExternalStorageDirectory().getAbsolutePath());

			File file = new File("/sdcard");
			Log.d(LOG_TAG, "getPath=" + file.getPath() + " absPath=" + file.getAbsolutePath() + " canonicalPath=" + file.getCanonicalPath());

			file = new File("/storage/emulated/legacy");
			Log.d(LOG_TAG, "getPath=" + file.getPath() + " absPath=" + file.getAbsolutePath() + " canonicalPath=" + file.getCanonicalPath());

			file = new File("/mnt/sdcard");
			Log.d(LOG_TAG, "getPath=" + file.getPath() + " absPath=" + file.getAbsolutePath() + " canonicalPath=" + file.getCanonicalPath());

			file = new File("/mnt/shell/emulated/0");
			Log.d(LOG_TAG, "getPath=" + file.getPath() + " absPath=" + file.getAbsolutePath() + " canonicalPath=" + file.getCanonicalPath());

			file = new File("/data/local/ltest");
			Log.d(LOG_TAG, "getPath=" + file.getPath() + " absPath=" + file.getAbsolutePath() + " canonicalPath=" + file.getCanonicalPath());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendLogEmail() {
		Intent it = new Intent(Intent.ACTION_SEND);
		String[] toWho = {
				"hongkai.qian@ttpod.com",
				"zhenhua.gao@ttpod.com",
				"anping.yin@ttpod.com"};
		it.putExtra(Intent.EXTRA_EMAIL, toWho);
		it.putExtra(Intent.EXTRA_SUBJECT, "tt http test log");
		it.putExtra(Intent.EXTRA_TEXT, mUserInfo + "\n\n" + mBuilder.toString());
		it.setType("text/plain");
		startActivity(Intent.createChooser(it, "Choose Email Client"));
	}

	private void sendQLBeginNotification(String tickerText) {
		Notification notify = new Notification(R.drawable.ico32, tickerText, System.currentTimeMillis());
//		notify.flags = Notification.FLAG_ONGOING_EVENT;
		Intent intent = new Intent(this, BMIActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(this, R.drawable.ic_launcher, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		notify.setLatestEventInfo(this, tickerText, tickerText, contentIntent);
		NotificationManager notifyMgr = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
		notifyMgr.notify(R.drawable.ic_launcher, notify);
		notifyMgr.cancel(R.drawable.ic_launcher);
	}

//	private void sendQLBeginNotification2() {
//		Notification.Builder notifyBuilder = new Notification.Builder(this);
//		notifyBuilder.setSmallIcon(R.drawable.ico);
//		notifyBuilder.setContentTitle("title kaikai");
//		notifyBuilder.setContentText(" text text kaikai");
//		notifyBuilder.setTicker("tickerText test");
////		notify.flags = Notification.FLAG_ONGOING_EVENT;
//		Intent intent = new Intent(this, BMIActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//		PendingIntent contentIntent = PendingIntent.getActivity(this, R.drawable.ic_launcher, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//		notifyBuilder.setContentIntent(contentIntent);
////		notify.setLatestEventInfo(this, "test notify icon2", "test notify icon3", contentIntent);
//		NotificationManager notifyMgr = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
//		notifyMgr.notify(R.drawable.ic_launcher, notifyBuilder.getNotification());
//		notifyMgr.cancel(R.drawable.ic_launcher);
//	}

	public void clickTestLayer1(View v) {
		LayerDrawable d = (LayerDrawable)mIvTestLayer2.getDrawable();
		d.setDrawableByLayerId(1, mDrawableLauncher2);
		d.invalidateSelf();
		mIvTestLayer2.invalidate();

		d = (LayerDrawable)mIvTestLayer1.getDrawable();
		d.setDrawableByLayerId(1, mDrawableLauncher1);
		d.invalidateSelf();
		mIvTestLayer1.invalidate();

		setSettingPanelColorButtonFlag(2);
	}

	public void clickTestLayer2(View v) {
//    	mIvTestLayer2.setImageDrawable(mIvTestLayer1.getDrawable().mutate());
		LayerDrawable d = (LayerDrawable)mIvTestLayer2.getDrawable();
		d.setDrawableByLayerId(1, mDrawableLauncher1);
		d.invalidateSelf();
		mIvTestLayer2.invalidate();

		d = (LayerDrawable)mIvTestLayer1.getDrawable();
		d.setDrawableByLayerId(1, mDrawableLauncher2);
		d.invalidateSelf();
		mIvTestLayer1.invalidate();

		setSettingPanelColorButtonFlag(3);
	}

	private View[] mArrayColorButton;
	private ColorButtonManager mColorButtonMgr;
	private Drawable mDrawableFlag;
	private Drawable mDrawableTransparent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button button = (Button)findViewById(R.id.submit);
		button.setOnClickListener(calcBMI);
		button = (Button)findViewById(R.id.submit2);
		button.setOnClickListener(calcBMI);

		mIvTestLayer1 = (ImageView)findViewById(R.id.iv_testlayer1);
		mIvTestLayer2 = (ImageView)findViewById(R.id.iv_testlayer2);
		mDrawableApple = getResources().getDrawable(R.drawable.minilyric_btn_color_blue);
		mDrawableLauncher2 = getResources().getDrawable(R.drawable.ic_launcher2);
		mDrawableLauncher1 = getResources().getDrawable(R.drawable.ic_launcher);
		LayerDrawable layerdrawable = new LayerDrawable(new Drawable[]{mDrawableApple, mDrawableLauncher2});
		layerdrawable.setId(1, 1);
		mIvTestLayer1.setImageDrawable(layerdrawable);

		layerdrawable = new LayerDrawable(new Drawable[]{mDrawableApple, mDrawableLauncher2});
		layerdrawable.setId(1, 1);
		mIvTestLayer2.setImageDrawable(layerdrawable);

		initColorButton();

		mLayout1 = findViewById(R.id.layout_1);
		mLayout2 = findViewById(R.id.layout_2);
		mEdtResult = (EditText)findViewById(R.id.edt_result);
		mLayout2.setVisibility(View.INVISIBLE);

		mUserInfo = new GeneralData(this).getData().toString();

		mBuilder = new StringBuilder(512);

		mLayout1.setVisibility(View.INVISIBLE);
		mLayout2.setVisibility(View.VISIBLE);

		mEdtResult.setText("请依此点击下面的按钮，如果某个按钮返回的结果不正常，可以多次点击此按钮重试。\n全部点完后可以通过菜单中的\"直接发送日志邮件\"功能发送邮件，cmwap uniwap ctwap 3gwap等wap网络用户第一个发功功能可能失败，可选择第二个\"使用系统邮件程序发送\"。\n正常结果可能有三种：一种提示:好音质 天天动听，一种有一行文字本身就是测试正常，还有一种有文字Thank you。");

//		EditText fieldHeight = (EditText)findViewById(R.id.stature);
//		SpannableString listSearchPrompt = new SpannableString("请依此点击下面的按钮");
//		listSearchPrompt.setSpan(new RelativeSizeSpan(1.5f), 2, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//		fieldHeight.setText(listSearchPrompt);
	}

	private class ColorButtonManager {
		private ArrayList<ColorButton> mList;

		private class ColorButton {
			private ImageView mImageButton;
			private Drawable mImage;
			private LayerDrawable mDrawable;
			private int mId;

//			private int mRed[] = {R.drawable.minilyric_icon_setting, R.drawable.minilyric_icon_fontzoomin, R.drawable.minilyric_icon_fontzoomout
//					, R.drawable.minilyric_return_main_panel_normal, R.drawable.minilyric_unlocked};

			public ColorButton(View button, Drawable image, int id) {
				mId = id;
				mImageButton = (ImageView)button;
				mImage = image;
				mDrawable = new LayerDrawable(new Drawable[]{mImage, mDrawableTransparent});
				mDrawable.setId(1, 1);
				mImageButton.setImageDrawable(mDrawable);
			}

			public void setFlag(boolean flag) {
				Drawable needDrawable = flag ? mDrawableFlag : mDrawableTransparent;
//				final Drawable drawableTmp = mContext.getResources().getDrawable(mRed[mId]);
//				++mId;
//				if (mId >= 5) {
//					mId = 0;
//				}
//				if (needDrawable != mDrawable.getDrawable(1)) {
//					mDrawable.setDrawableByLayerId(1, needDrawable);
//				}
				mDrawable.setDrawableByLayerId(1, needDrawable);
				mDrawable.invalidateSelf();
//				mImageButton.invalidate();
			}
		}

		void appendColorButton(View button, Drawable image) {
			ColorButton colorButton = new ColorButton(button, image, mList.size());
			mList.add(colorButton);
		}

		void setChoosedButton(int index) {
			for (int idx = mList.size() - 1; idx >= 0; --idx) {
				ColorButton btn = mList.get(idx);
				btn.setFlag(idx == index);
			}
		}

		public ColorButtonManager() {
			mList = new ArrayList<ColorButtonManager.ColorButton>(mArrayColorButton.length);

		}
	}

	private void initColorButton() {
		final Resources resources = this.getResources();
		final int colorButtonAmount = 5;
		mArrayColorButton = new View[colorButtonAmount];
		mColorButtonMgr = new ColorButtonManager();
		mDrawableFlag = resources.getDrawable(R.drawable.minilyric_choosed_color);
		mDrawableTransparent = resources.getDrawable(R.drawable.minilyric_icon_lock);

		setColorButtonPropety(0, R.id.iv_color_blue, R.drawable.minilyric_btn_color_blue);
		setColorButtonPropety(1, R.id.iv_color_yellow, R.drawable.minilyric_btn_color_yellow);
		setColorButtonPropety(2, R.id.iv_color_pink, R.drawable.minilyric_btn_color_pink);
		setColorButtonPropety(3, R.id.iv_color_gray, R.drawable.minilyric_btn_color_gray);
		setColorButtonPropety(4, R.id.iv_color_green, R.drawable.minilyric_btn_color_green);

//		mArrayColorButton[2].setVisibility(View.INVISIBLE);
//		mArrayColorButton[3].setVisibility(View.INVISIBLE);
//		mArrayColorButton[4].setVisibility(View.INVISIBLE);

//		int colorStyle = 2;
//		setSettingPanelColorButtonFlag(colorStyle);
	}

	private void setColorButtonPropety(int index, int buttonId, int imageResId) {
		View button = findViewById(buttonId);
		final Resources resources = getResources();
		button.setOnClickListener(this);
		Drawable drawable = resources.getDrawable(imageResId);
		mArrayColorButton[index] = button;
		mColorButtonMgr.appendColorButton(button, drawable);
	}

	private void setSettingPanelColorButtonFlag(int aColorStyle) {
		mColorButtonMgr.setChoosedButton(aColorStyle);
	}

	@Override
	public void onClick(View view) {
		for (int idx = mArrayColorButton.length - 1; idx >= 0; --idx) {
			if (view.equals(mArrayColorButton[idx])) {
				setSettingPanelColorButtonFlag(idx);
				break;
			}
		}
		doColorButtonClicked(view);
	}

	private void doColorButtonClicked(View v) {
		if (mLayout2.getVisibility() != View.VISIBLE) {
			mLayout1.setVisibility(View.INVISIBLE);
			mLayout2.setVisibility(View.VISIBLE);
		}
		new HttpAsyncTask(this, this).execute(v.getId());
		mEdtResult.setText("正在获取网络数据，请稍等...");
	}

	@Override
	public void notifyResult(int id, String result2) {
//		if (mLayout2.getVisibility() != View.VISIBLE) {
//			mLayout1.setVisibility(View.INVISIBLE);
//			mLayout2.setVisibility(View.VISIBLE);
//		}

		String result = "";
		String from = "\n\n";
		switch (id) {
		case R.id.iv_color_blue:
			from = "";
			result = "1.blue:\n" + result2;
			break;

		case R.id.iv_color_yellow:
			result = "2.yellow:\n" + result2;
			break;

		case R.id.iv_color_pink:
			result = "3. pink:\n" + result2;
			break;

		case R.id.iv_color_gray:
			result = "4.gray:\n" + result2;
			break;

		case R.id.iv_color_green:
			result = "5.green:\n" + result2;
			break;

		case 1:
//			sendQLBeginNotification(result2);
			Toast.makeText(BMIActivity.this, result2, Toast.LENGTH_LONG).show();
			result = result2;
			break;

		default:
			break;
		}
		mEdtResult.setText(result);
		mBuilder.append(from + result);
	}
}
