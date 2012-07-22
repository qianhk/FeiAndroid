package com.njnu.kai.feisms;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class ContentContactDetailActivity extends TabActivity implements OnCheckedChangeListener {
	private TabHost mTabHost;
	private Intent mIntentSMS;
	private Intent mIntentContact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_contact_detail);
		
		mIntentSMS = new Intent(this, TabSMSContentActivity.class);
		mIntentContact = new Intent(this, TabContactActivity.class);
		initRadios();
		setupIntent();
	}

	/**
	 * 初始化底部按钮
	 */
	private void initRadios() {
		((RadioButton) findViewById(R.id.radio_button_sms)).setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_button_contact)).setOnCheckedChangeListener(this);
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
			case R.id.radio_button_contact:
				this.mTabHost.setCurrentTabByTag("tab_contact");
				break;
			}
		}
	}

	private void setupIntent() {
		mTabHost = getTabHost();
		mTabHost.addTab(buildTabSpec("tab_sms", mIntentSMS));
		mTabHost.addTab(buildTabSpec("tab_contact", mIntentContact));
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

}
