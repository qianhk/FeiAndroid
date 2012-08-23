package com.njnu.kai.activitytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public abstract class BaseActivity extends Activity {
	protected TextView mTextView;
	protected Button mButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.text_button);
        mTextView = (TextView)findViewById(R.id.textview);
        mButton = (Button)findViewById(R.id.button);
        String className = this.getClass().getSimpleName();
        setTitle(className);
        mButton.setText(getButtonText());
        mTextView.setText(String.format("flg=0x%08X\ntaskid=%d\n%s", getIntent().getFlags(), getTaskId(), this.toString()));
        mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent startupIntent = getStartupIntent();
				startActivity(startupIntent);
			}
		});
        TextView tv2 = (TextView)findViewById(R.id.textview2);
        tv2.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		Log.i("OOXX_OOXX", "onNewIntent " + intent);
//		Log.i(getClass().getSimpleName(), "onNewIntent " + intent);
		mTextView.append(String.format("\nonNewIntent flg=0x%08X", intent.getFlags()));
	}

	abstract protected String getButtonText();
	
	abstract protected Intent getStartupIntent();
}
