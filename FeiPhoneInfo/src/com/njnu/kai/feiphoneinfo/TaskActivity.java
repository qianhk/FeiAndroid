package com.njnu.kai.feiphoneinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class TaskActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView tv = new TextView(this);
		tv.setText("Task");
		setContentView(tv);

		int amountTask = getProcessInfo();
		Log.v(TAG, "Process amount = " + amountTask);
	}

	public void showProcessInfo() {
		// 更新进程列表
		List<HashMap<String, String>> infoList = new ArrayList<HashMap<String, String>>();
		for (Iterator<RunningAppProcessInfo> iterator = procList.iterator(); iterator
				.hasNext();) {
			RunningAppProcessInfo procInfo = iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("proc_name", procInfo.processName);
			map.put("proc_id", procInfo.pid + "");
			infoList.add(map);
		}

		// SimpleAdapter simpleAdapter = new SimpleAdapter(
		// this,
		// infoList,
		// R.layout.proc_list_item,
		// new String[]{"proc_name", "proc_id"},
		// new int[]{R.id.proc_name, R.id.proc_id} );
		// setListAdapter(simpleAdapter);
	}

	public int getProcessInfo() {
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		procList = activityManager.getRunningAppProcesses();
		return procList.size();
	}

	private static List<RunningAppProcessInfo> procList = null;
	private final static String TAG = "TaskActivity";
}
