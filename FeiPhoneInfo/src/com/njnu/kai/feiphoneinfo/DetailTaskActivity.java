package com.njnu.kai.feiphoneinfo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DetailTaskActivity extends Activity {
	DetailProgramUtil detailInfo = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// public void OnCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.task_detail);

		Intent intent = getIntent();
		detailInfo = (DetailProgramUtil) intent.getSerializableExtra("detailData");
		Log.v("DetailTaskActivity", "detailInfo=" + detailInfo);
		TextView tv = null;
		tv = (TextView) findViewById(R.id.detail_process_name);
		tv.setText(detailInfo.getProcessName());

		tv = (TextView) findViewById(R.id.detail_process_copyright);
		tv.setText(detailInfo.getCompanyName());

		tv = (TextView) findViewById(R.id.detail_process_install_dir);
		tv.setText(detailInfo.getSourceDir());

		tv = (TextView) findViewById(R.id.detail_process_data_dir);
		tv.setText(detailInfo.getDataDir());

		tv = (TextView) findViewById(R.id.detail_process_package_size);
		tv.setText(detailInfo.getPackageSize());

		tv = (TextView) findViewById(R.id.detail_process_permission);
		tv.setText(detailInfo.getUserPermissions());

		tv = (TextView) findViewById(R.id.detail_process_service);
		tv.setText(detailInfo.getServices());

		tv = (TextView) findViewById(R.id.detail_process_activity);
		tv.setText(detailInfo.getActivities());

		Button btnKillProcess = (Button) findViewById(R.id.btn_kill_process);
		btnKillProcess.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
				if (detailInfo.getProcessName().equals("com.njnu.kai.feiphoneinfo")) {
					returnToHome();
//					android.os.Process.killProcess(android.os.Process.myPid());
//					System.exit(0);
				} else {
					activityManager.killBackgroundProcesses(detailInfo.getProcessName());
				}
			}
		});
	}

	private void returnToHome() {
		Intent home = new Intent(Intent.ACTION_MAIN);
		home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		home.addCategory(Intent.CATEGORY_HOME);
		startActivity(home);
	}

	// 结束其他程序
	// ActivityManager.killBackgroundProcesses(PackageName)
}
