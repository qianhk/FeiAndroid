package com.njnu.kai.feisms;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class TabSMSContentActivity extends Activity {
	private static final String PREFIX = "TabSMSContentActivity";
	private EditText mEditTextGroupName;
	private EditText mEditTextGroupSMS;
	private SMSGroupEntrySMS mGroupEntrySMS;
	private FeiSMSDataManager mDataManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_sms);
		mDataManager = FeiSMSDataManager.getDefaultInstance(this);
		int groupId = getIntent().getIntExtra(FeiSMSConst.GROUP_ID, 0);
		mGroupEntrySMS = mDataManager.getSMSGroupEntrySMS(groupId);
		mEditTextGroupName = (EditText)findViewById(R.id.edittext_group_name);
		mEditTextGroupSMS = (EditText)findViewById(R.id.edittext_group_sms);
		mEditTextGroupName.setText(mGroupEntrySMS.getGroupName());
		mEditTextGroupSMS.setText(mGroupEntrySMS.getSMSContent());
	}

//	@Override
//	protected void onPause() {
//		super.onPause();
//		Log.i(PREFIX, "onPause");
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		Log.i(PREFIX, "onResume");
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		Log.i(PREFIX, "onDestroy");
//	}

}
