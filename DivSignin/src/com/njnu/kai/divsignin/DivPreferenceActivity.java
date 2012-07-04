package com.njnu.kai.divsignin;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class DivPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.div_preference);
	}

}
