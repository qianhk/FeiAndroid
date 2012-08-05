package com.njnu.kai.feisms;

import java.security.spec.MGF1ParameterSpec;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class ContentContactsDetailActivity extends TabActivity implements OnCheckedChangeListener {
	private TabHost mTabHost;
	private Intent mIntentSMS;
	private Intent mIntentContacts;
	private static final String PREFIX = "ContentContactsDetailActivity";
	private final String KEY_GROUP_ID = "group_id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_contacts_detail);
		
		int groupId = getIntent().getIntExtra(FeiSMSConst.KEY_GROUP_ID, 0);
		Log.i(PREFIX, "onCreate groupId=" + groupId + " savedState=" + savedInstanceState);
		mIntentSMS = new Intent(this, TabSMSContentActivity.class);
		mIntentSMS.putExtra(FeiSMSConst.KEY_GROUP_ID, groupId);
		mIntentContacts = new Intent(this, TabContactsActivity.class);
		mIntentContacts.putExtra(FeiSMSConst.KEY_GROUP_ID, groupId);
		initRadios();
		setupIntent();
	}

	/**
	 * 初始化底部按钮
	 */
	private void initRadios() {
		((RadioButton) findViewById(R.id.radio_button_sms)).setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_button_contacts)).setOnCheckedChangeListener(this);
	}

	/**
	 * 切换模块
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.radio_button_sms:
				this.mTabHost.setCurrentTabByTag("tab_sms");
				break;
			case R.id.radio_button_contacts:
				this.mTabHost.setCurrentTabByTag("tab_contacts");
				break;
			}
		}
	}

	private void setupIntent() {
		mTabHost = getTabHost();
		mTabHost.addTab(buildTabSpec("tab_sms", mIntentSMS));
		mTabHost.addTab(buildTabSpec("tab_contacts", mIntentContacts));
	}

	private TabHost.TabSpec buildTabSpec(String tag, final Intent content) {
		return mTabHost.newTabSpec(tag).setIndicator(tag).setContent(content);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

//	@Override
//	protected void onRestoreInstanceState(Bundle state) {
//		super.onRestoreInstanceState(state);
//		if (state.containsKey(KEY_GROUP_ID)) {
//			mGroupId = state.getInt(KEY_GROUP_ID);
//			Log.i(PREFIX, "onRestoreInstanceState groupId=" + mGroupId);
//		}
//	}
//
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		outState.putInt(KEY_GROUP_ID, mGroupId);
//		Log.i(PREFIX, "onSaveInstanceState groupId=" + mGroupId);
//	}
//	
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
