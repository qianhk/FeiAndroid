package com.njnu.kai.feiphoneinfo;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class PhoneInfoMainActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources();
        TabHost tabHost = getTabHost();
//        tabHost.setup();
        TabHost.TabSpec spec;
        Intent intent;
        intent = new Intent().setClass(this, GeneralActivity.class);
        spec = tabHost.newTabSpec("general").setIndicator(res.getText(R.string.tab_general), res.getDrawable(R.drawable.ic_generalinfo)).setContent(intent);
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, TaskActivity.class);
        spec = tabHost.newTabSpec("task").setIndicator(res.getText(R.string.tab_task), res.getDrawable(R.drawable.process)).setContent(intent);
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, GPUActivity.class);
        spec = tabHost.newTabSpec("gpu").setIndicator(res.getText(R.string.tab_gpu), res.getDrawable(R.drawable.profiles)).setContent(intent);
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, HardwareActivity.class);
        spec = tabHost.newTabSpec("hardware").setIndicator(res.getText(R.string.tab_hardware), res.getDrawable(R.drawable.hardware)).setContent(intent);
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, AboutActivity.class);
        spec = tabHost.newTabSpec("about").setIndicator(res.getText(R.string.tab_about), res.getDrawable(R.drawable.about)).setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }
}
