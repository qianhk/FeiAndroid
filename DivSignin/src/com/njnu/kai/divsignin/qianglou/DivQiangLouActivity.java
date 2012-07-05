package com.njnu.kai.divsignin.qianglou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.njnu.kai.divsignin.DivSigninService;
import com.njnu.kai.divsignin.R;

public class DivQiangLouActivity extends Activity {
	
	private static final String PRFEIX = "DivQiangLouActivity";
	private boolean mDoingQiangLou = false;
	private Button mButtonQiangLou;
	private EditText mTextEditResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.div_qianglou);
		
		Log.i(PRFEIX, "onCreate");
		
		mButtonQiangLou = (Button)findViewById(R.id.button_start);
		mTextEditResult = (EditText)findViewById(R.id.edittext_result);
		
		mButtonQiangLou.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
	}

	@Override
	protected void onDestroy() {
		Log.i(PRFEIX, "onDestroy");
		if (mDoingQiangLou) {
			stopService(new Intent(this, DivSigninService.class));
		}
		super.onDestroy();
	}

}
