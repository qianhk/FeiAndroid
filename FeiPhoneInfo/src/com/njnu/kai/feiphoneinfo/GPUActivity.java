package com.njnu.kai.feiphoneinfo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GPUActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView tv = new TextView(this);
		tv.setText("Gpu");
		setContentView(tv);
	}


}
