package com.njnu.kai.floatwin;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class FloatWinActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		final TextView tv = new TextView(this);
		FloatingFunc.show(getApplicationContext(), tv);
		tv.setText("Test FloatWin 浮动面板");
		tv.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				FloatingFunc.onTouchEvent(arg1, tv);
				return true;
			}
		});
    }
}
