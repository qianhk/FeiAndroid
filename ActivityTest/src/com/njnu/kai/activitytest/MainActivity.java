package com.njnu.kai.activitytest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;

public class MainActivity extends BaseActivity {

	@Override
	protected String getButtonText() {
		return "to Second";
	}

	@Override
	protected Intent getStartupIntent() {
		Intent intent = new Intent(this, SecondActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		return intent;
	}

}
