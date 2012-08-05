package com.njnu.kai.divsignin.qianglou;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.njnu.kai.divsignin.R;

public class DivQiangLouStatus extends Activity {
	private EditText mTextEditResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.div_qianglou_status);
//		IntentFilter intentFilter = new IntentFilter(DivConst.ACTION_QIANGLOU_NOTIFY);
//		intentFilter.addAction(DivConst.ACTION_QIANGLOU_ALARM);
//		intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
//		mReceiver = new DivQiangLouReceiver(this);
//		registerReceiver(mReceiver, intentFilter);
		mTextEditResult = (EditText) findViewById(R.id.edittext_result);

	}
}
