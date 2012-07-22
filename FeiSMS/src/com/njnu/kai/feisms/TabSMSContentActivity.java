package com.njnu.kai.feisms;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

public class TabSMSContentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_sms);
	}

	@Override
	protected void onPause() {
//		View currentFocus = getCurrentFocus();
//		IBinder windowToken = currentFocus != null ? currentFocus.getWindowToken() : null;
//		Log.i("haha", " focusView=" + currentFocus + " token=" + windowToken);
		super.onPause();
//		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		// 得到InputMethodManager的实例
//		if (imm.isActive()) {
//			// 如果开启
//			Log.i("TabSmsContent", "activie");
//			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
//			// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
//		}
//		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//				InputMethodManager.HIDE_NOT_ALWAYS);
	}

}
