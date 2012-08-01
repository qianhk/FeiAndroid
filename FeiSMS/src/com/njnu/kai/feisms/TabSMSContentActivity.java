package com.njnu.kai.feisms;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class TabSMSContentActivity extends Activity {

	private EditText mEditTextGroupName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_sms);
		long groupId = getIntent().getLongExtra(FeiSMSConst.GROUP_ID, 0);
		mEditTextGroupName = (EditText)findViewById(R.id.edittext_group_name);
		mEditTextGroupName.setText("GroupName" + groupId);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}
