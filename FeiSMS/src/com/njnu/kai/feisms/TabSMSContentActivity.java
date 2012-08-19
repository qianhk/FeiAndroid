package com.njnu.kai.feisms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TabSMSContentActivity extends Activity {
	private static final String PREFIX = "TabSMSContentActivity";
	private EditText mEditTextGroupName;
	private EditText mEditTextGroupSMS;
	private SMSGroupEntrySMS mGroupEntrySMS;
	private FeiSMSDataManager mDataManager;
	private TextView mTextViewSMSContent;

	private void setTextViewSMSContent(int length) {
		String prompt = getResources().getString(R.string.sms_content);
		if (length > 0) {
			prompt += length + "/70";
		}
		mTextViewSMSContent.setText(prompt);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_sms);
		mDataManager = FeiSMSDataManager.getDefaultInstance(this);
		mEditTextGroupName = (EditText) findViewById(R.id.edittext_group_name);
		mEditTextGroupSMS = (EditText) findViewById(R.id.edittext_group_sms);
		mTextViewSMSContent = (TextView)findViewById(R.id.textview_smscontent);
		
//		mGroupId = getIntent().getIntExtra(FeiSMSConst.KEY_GROUP_ID, 0);
		int groupId = SMSUtils.getCurrentDetailGroupId();
		
		Log.i(PREFIX, "onCreate groupId=" + groupId + " savedState=" + savedInstanceState);
		if (groupId >= 0) {
			mGroupEntrySMS = mDataManager.getSMSGroupEntrySMS(groupId);
		} else {
			mGroupEntrySMS = new SMSGroupEntrySMS(groupId, "", "");
		}
		
		mEditTextGroupName.setText(mGroupEntrySMS.getGroupName());
		String smsContent = mGroupEntrySMS.getSMSContent();
		mEditTextGroupSMS.setText(smsContent);
		setTextViewSMSContent(smsContent.length());
		mEditTextGroupName.setOnFocusChangeListener(mFocusChangeListener);
		mEditTextGroupSMS.setOnFocusChangeListener(mFocusChangeListener);

		mEditTextGroupName.setTag(mGroupEntrySMS.getGroupName());
		mEditTextGroupSMS.setTag(smsContent);
		
		mEditTextGroupSMS.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				Log.i(PREFIX, "onTextChanged " + s.toString() + " " + start + " " + before + " " + count);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//				Log.i(PREFIX, "beforeTextChanged " + s.toString() + " " + start + " " + count + " " + after);
			}
			
			@Override
			public void afterTextChanged(Editable s) {
//				Log.i(PREFIX, "afterTextChanged " + s.toString());
				setTextViewSMSContent(s.length());
			}
		});
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
		if (SMSUtils.getCurrentDetailGroupId() == FeiSMSConst.GROUP_ID_CREATE) {
			int groupId = mGroupEntrySMS.getGroupId();
			SMSUtils.setCurrentDetailGroupId(groupId);
//			Log.i(PREFIX, "doSendBroadcastWhenInsertNewGroup " + groupId);
//			Intent intent = new Intent(FeiSMSConst.ACTION_GROUP_ID_UPDATED);
//			intent.putExtra(FeiSMSConst.KEY_GROUP_ID, groupId);
//			sendBroadcast(intent);
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
