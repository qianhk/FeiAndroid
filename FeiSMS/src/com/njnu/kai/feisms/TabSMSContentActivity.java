package com.njnu.kai.feisms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
		mEditTextGroupName = (EditText) findViewById(R.id.edittext_group_name);
		mEditTextGroupSMS = (EditText) findViewById(R.id.edittext_group_sms);
		
		mGroupId = getIntent().getIntExtra(FeiSMSConst.KEY_GROUP_ID, 0);
		
		Log.i(PREFIX, "onCreate groupId=" + mGroupId + " savedState=" + savedInstanceState);
		if (mGroupId >= 0) {
			mGroupEntrySMS = mDataManager.getSMSGroupEntrySMS(mGroupId);
		} else {
			mGroupEntrySMS = new SMSGroupEntrySMS(mGroupId, "", "");
		}
		
		mEditTextGroupName.setText(mGroupEntrySMS.getGroupName());
		mEditTextGroupSMS.setText(mGroupEntrySMS.getSMSContent());
		mEditTextGroupName.setOnFocusChangeListener(mFocusChangeListener);
		mEditTextGroupSMS.setOnFocusChangeListener(mFocusChangeListener);

		mEditTextGroupName.setTag(mGroupEntrySMS.getGroupName());
		mEditTextGroupSMS.setTag(mGroupEntrySMS.getSMSContent());
	}

	View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
//			Log.i(PREFIX, "onFocusChange " + v + " " + hasFocus);
			if (!hasFocus) {
				if (v.getId() == R.id.edittext_group_name) {
					doCheckEditTextGroupNameChanged();
				} else if (v.getId() == R.id.edittext_group_sms) {
					doCheckEditTextGroupSMSChanged();
				}
			}
		}
	};
	private int mGroupId;

	private void doCheckEditTextGroupNameChanged() {
		String curGroupName = mEditTextGroupName.getText().toString();
		if (!curGroupName.equals(mEditTextGroupName.getTag())) {
			Log.i(PREFIX, "GroupName changed: " + curGroupName);
			mGroupEntrySMS.setGroupName(curGroupName);
			mDataManager.updateSMSGroup(mGroupEntrySMS);
			mEditTextGroupName.setTag(curGroupName);
			doSendBroadcastWhenInsertNewGroup();
		}
	}
	
	private void doSendBroadcastWhenInsertNewGroup() {
		if (mGroupId == FeiSMSConst.GROUP_ID_CREATE) {
			mGroupId = mGroupEntrySMS.getGroupId();
			Log.i(PREFIX, "doSendBroadcastWhenInsertNewGroup " + mGroupId);
			Intent intent = new Intent(FeiSMSConst.ACTION_GROUP_ID_UPDATED);
			intent.putExtra(FeiSMSConst.KEY_GROUP_ID, mGroupId);
			sendBroadcast(intent);
		}
	}

	private void doCheckEditTextGroupSMSChanged() {
		String curGroupSMS = mEditTextGroupSMS.getText().toString();
		if (!curGroupSMS.equals(mEditTextGroupSMS.getTag())) {
			Log.i(PREFIX, "SMS content changed: " + curGroupSMS);
			mGroupEntrySMS.setSMSContent(curGroupSMS);
			mDataManager.updateSMSGroup(mGroupEntrySMS);
			mEditTextGroupSMS.setTag(curGroupSMS);
			doSendBroadcastWhenInsertNewGroup();
		}
	}

//	@Override
//	protected void onRestart() {
//		super.onRestart();
//		Log.i(PREFIX, "onRestart groupId=" + mGroupId);
//	}
//	
//	@Override
//	protected void onStart() {
//		super.onStart();
//		Log.i(PREFIX, "onStart groupId=" + mGroupId);
//	}

	@Override
	protected void onPause() {
		super.onPause();
//		Log.i(PREFIX, "onPause groupId=" + mGroupId);

		doCheckEditTextGroupNameChanged();
		doCheckEditTextGroupSMSChanged();
	}

//	@Override
//	protected void onStop() {
//		super.onStop();
//		Log.i(PREFIX, "onStop groupId=" + mGroupId);
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		Log.i(PREFIX, "onResume groupId=" + mGroupId);
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		Log.i(PREFIX, "onDestroy groupId=" + mGroupId);
//	}

}
