package com.njnu.kai.activitytest;

import android.content.Intent;
import android.widget.Button;

public class SecondActivity extends BaseActivity {

	@Override
	protected String getButtonText() {
		return "to MainActivity";
	}

	@Override
	protected Intent getStartupIntent() {
		Intent intent = new Intent(this, MainActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		return intent;
	}

}
