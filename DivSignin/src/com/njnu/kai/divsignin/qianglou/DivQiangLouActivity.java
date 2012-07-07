package com.njnu.kai.divsignin.qianglou;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.njnu.kai.divsignin.DivSigninService;
import com.njnu.kai.divsignin.R;
import com.njnu.kai.divsignin.common.DivConst;
import com.njnu.kai.divsignin.qianglou.DivQiangLouReceiver.DivQiangLouNotify;

public class DivQiangLouActivity extends Activity implements DivQiangLouNotify {
	
	private static final String PREFIX = "DivQiangLouActivity";
	private boolean mDoingQiangLou = false;
	private Button mButtonQiangLou;
	private EditText mTextEditResult;
	private DivQiangLouReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.div_qianglou);
		
		Log.i(PREFIX, "onCreate");
		
		mButtonQiangLou = (Button)findViewById(R.id.button_start);
		mTextEditResult = (EditText)findViewById(R.id.edittext_result);
		
		mButtonQiangLou.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(PREFIX, "onClick");
				Intent intent = new Intent(DivQiangLouActivity.this, DivSigninService.class);
				mButtonQiangLou.setText(mDoingQiangLou ? "Start" : "Stop");
				if (mDoingQiangLou) {
					stopService(intent);
				} else {
					startService(intent);
				}
				mDoingQiangLou = !mDoingQiangLou;
			}
		});
		
		IntentFilter intentFilter = new IntentFilter(DivConst.ACTION_QIANGLOU_NOTIFY);
		intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
		mReceiver = new DivQiangLouReceiver(this);
		registerReceiver(mReceiver, intentFilter);
	}

	@Override
	protected void onDestroy() {
		Log.i(PREFIX, "onDestroy");
		if (mDoingQiangLou) {
			stopService(new Intent(this, DivSigninService.class));
		}
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	public void notifyMessage(int type, String message) {
//		Log.i(PREFIX, "notifyMessage=" + message);
		mTextEditResult.append(message + "\n");
		if (type != 0) {
			mDoingQiangLou = false;
			mButtonQiangLou.setText("Start");
		}
	}

}
