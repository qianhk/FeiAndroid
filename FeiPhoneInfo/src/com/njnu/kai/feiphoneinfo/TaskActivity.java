package com.njnu.kai.feiphoneinfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskActivity extends ListActivity implements OnItemClickListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		packageManager = getApplicationContext().getPackageManager();
		packageUtil = new PackageUtil(this);
		// lstAllProcess = new ArrayList<TaskActivity.BasicProgramUtil>();
		taskAdapter = new TaskAdapter(this);
		setListAdapter(taskAdapter);
//		int amountTask = getProcessInfo();
//		Log.v(TAG, "Process amount = " + amountTask);
//		// showProcessInfo();
//		taskAdapter.reloadData(procList);
		getListView().setOnItemClickListener(this);

        handler.sendMessageDelayed(handler.obtainMessage(METHOD_FLUSH_PROCESS), FLUSH_PROCESS_TIME);
//		handler.post(r);
//		System.out.println("TaskActivity ---> " + Thread.currentThread().getId());
	}

//	@Override
//	public void onStart() {
//		super.onStart();
//		Log.v("TaskActivity", "OnStart");
//	}
//
//	@Override
//	public void onRestart() {
//		super.onRestart();
//		Log.v("TaskActivity", "onRestart");
//	}

    public static final int FLUSH_PROCESS_TIME = 2000;
    private static final int METHOD_FLUSH_PROCESS = 0x800;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(METHOD_FLUSH_PROCESS);
    }

    @Override
	public void onResume() {
		super.onResume();
//		Log.v("TaskActivity", "onResume");
		updateProcessList();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.addSubMenu(0, 2001, 2001, "Flush");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Log.v("TaskActivity", item.getTitle().toString() + " id=" +
		// item.getItemId());
		boolean sucess = true;
		switch (item.getItemId()) {
		case 2001:
			updateProcessList();
			break;
		default:
			sucess = super.onOptionsItemSelected(item);
		}
		return sucess;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// Log.v("Hardware", "Clicked " + id + " " + ((TextView) view).getText()
		// +" pos=" + position);
		BasicProgramUtil processInfo = taskAdapter.getItem(position);
		// Log.v("TaskActivity", "Clicked " + id + " " + view +" pos=" +
		// position + " obj=" + processInfo.getProcessName());
		DetailProgramUtil detailInfo = buildProgramUtilComplexInfo(processInfo.getProcessName());
		Intent intent = new Intent(TaskActivity.this, DetailTaskActivity.class);
		intent.putExtra("detailData", detailInfo);
		startActivity(intent);
	}

	// public void showProcessInfo() {
	// // 更新进程列表
	// List<HashMap<String, Object>> infoList = new ArrayList<HashMap<String,
	// Object>>();
	// // getSystemService(name)
	// for (Iterator<RunningAppProcessInfo> iterator = procList.iterator();
	// iterator.hasNext();) {
	// RunningAppProcessInfo procInfo = iterator.next();
	// HashMap<String, Object> map = new HashMap<String, Object>();
	// map.put("proc_name", procInfo.pid + " " +procInfo.processName);
	// map.put("proc_id", procInfo.pkgList.length + " " + procInfo.pkgList[0]);
	// map.put("order", procInfo.pid);
	// infoList.add(map);
	// }
	// // infoList.
	//
	// SimpleAdapter simpleAdapter = new SimpleAdapter(
	// this,
	// infoList,
	// R.layout.list_item_number_two_text,
	// new String[]{"proc_name", "proc_id", "order"},
	// new int[]{R.id.text1, R.id.text2, R.id.appicon} );
	// setListAdapter(simpleAdapter);
	// }

	public int getProcessInfo() {
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		procList = activityManager.getRunningAppProcesses();
		// Collections.sort(procList, new Comparator<RunningAppProcessInfo>() {
		// @Override
		// public int compare(RunningAppProcessInfo object1,
		// RunningAppProcessInfo object2) {
		// return object1.pid - object2.pid;
		// }
		// });

		return procList.size();
	}

	private static List<RunningAppProcessInfo> procList = null;
	private final static String TAG = "TaskActivity";
	private PackageManager packageManager = null;
	private PackageUtil packageUtil = null;
	private ProcessMemoryUtil processMemoryUtil = null;
	private TaskAdapter taskAdapter = null;

	public class TaskAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private List<BasicProgramUtil> lstAllProcess = null;

		public TaskAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
			lstAllProcess = new ArrayList<BasicProgramUtil>();
		}

		public void reloadData(List<RunningAppProcessInfo> procList) {
			lstAllProcess.clear();
			for (Iterator<RunningAppProcessInfo> iterator = procList.iterator(); iterator.hasNext();) {
				RunningAppProcessInfo procInfo = iterator.next();
				BasicProgramUtil proInfo2 = buildProgramUtilSimpleInfo(procInfo.pid, procInfo.processName);
				lstAllProcess.add(proInfo2);
			}
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return lstAllProcess.size();
		}

		@Override
		public BasicProgramUtil getItem(int position) {
			return lstAllProcess.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_item_task, null);
			}
			BasicProgramUtil processInfo = lstAllProcess.get(position);
			ImageView iv = (ImageView) convertView.findViewById(R.id.appicon);
			iv.setImageDrawable(processInfo.getIcon());
			TextView tv = (TextView) convertView.findViewById(R.id.text1);
			tv.setText(processInfo.getProgramName());
			tv = (TextView) convertView.findViewById(R.id.text2);
			tv.setText(processInfo.getProcessName());
			return convertView;
		}

	}

	public BasicProgramUtil buildProgramUtilSimpleInfo(int procId, String procNameString) {

		BasicProgramUtil programUtil = new BasicProgramUtil();
		programUtil.setProcessName(procNameString);

		// 根据进程名，获取应用程序的ApplicationInfo对象
		ApplicationInfo tempAppInfo = packageUtil.getApplicationInfo(procNameString);

		if (tempAppInfo != null) {
			// 为进程加载图标和程序名称
			programUtil.setIcon(tempAppInfo.loadIcon(packageManager));
			programUtil.setProgramName(tempAppInfo.loadLabel(packageManager).toString());
		} else {
			// 如果获取失败，则使用默认的图标和程序名
			programUtil.setIcon(getApplicationContext().getResources().getDrawable(R.drawable.unknow));
			programUtil.setProgramName(procNameString);
		}

		// String str = processMemoryUtil.getMemInfoByPid(procId);
		// programUtil.setCpuMemString(str);
		programUtil.setCpuMemString("setCpuMemString");
		return programUtil;
	}

	public DetailProgramUtil buildProgramUtilComplexInfo(String procNameString) {

		DetailProgramUtil complexProgramUtil = new DetailProgramUtil();
		// 根据进程名，获取应用程序的ApplicationInfo对象
		ApplicationInfo tempAppInfo = packageUtil.getApplicationInfo(procNameString);
		if (tempAppInfo == null) {
			return null;
		}

		PackageInfo tempPkgInfo = null;
		try {
			tempPkgInfo = packageManager.getPackageInfo(tempAppInfo.packageName, PackageManager.GET_UNINSTALLED_PACKAGES
					| PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES | PackageManager.GET_PERMISSIONS);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (tempPkgInfo == null) {
			return null;
		}

		complexProgramUtil.setProcessName(procNameString);
		complexProgramUtil.setCompanyName(getString(R.string.no_use));
		complexProgramUtil.setVersionName(tempPkgInfo.versionName);
		complexProgramUtil.setVersionCode(tempPkgInfo.versionCode);
		complexProgramUtil.setDataDir(tempAppInfo.dataDir);
		complexProgramUtil.setSourceDir(tempAppInfo.sourceDir);

		// 以下注释部分的信息暂时获取不到
//		 complexProgramUtil.setFirstInstallTime(tempPkgInfo.firstInstallTime);
//		 complexProgramUtil.setLastUpdateTime(tempPkgInfo.lastUpdateTime);

//		 complexProgramUtil.setCodeSize(tempPkgInfo.codeSize);
//		 complexProgramUtil.setDataSize(tempPkgInfo.dataSize);
//		 complexProgramUtil.setCacheSize(packageStats.cacheSize);
		 complexProgramUtil.setExternalDataSize(0);
		 complexProgramUtil.setExternalCacheSize(0);
		 complexProgramUtil.setExternalMediaSize(0);
		 complexProgramUtil.setExternalObbSize(0);

		// 获取以下三个信息，需要为PackageManager进行授权(packageManager.getPackageInfo()方法)
		complexProgramUtil.setUserPermissions(tempPkgInfo.requestedPermissions);
		complexProgramUtil.setServices(tempPkgInfo.services);
		complexProgramUtil.setActivities(tempPkgInfo.activities);

		return complexProgramUtil;
	}

	public class BasicProgramUtil {

		private Drawable icon; // 程序图标
		private String programName; // 程序名称
		private String processName;

		private String cpuMemString;

		public BasicProgramUtil() {
			icon = null;
			programName = "";
			processName = "";
			cpuMemString = "";
		}

		public Drawable getIcon() {
			return icon;
		}

		public void setIcon(Drawable icon) {
			this.icon = icon;
		}

		public String getProgramName() {
			return programName;
		}

		public void setProgramName(String programName) {
			this.programName = programName;
		}

		public String getCpuMemString() {
			return cpuMemString;
		}

		public void setCpuMemString(String cpuMemString) {
			this.cpuMemString = cpuMemString;
		}

		public String getProcessName() {
			return processName;
		}

		public void setProcessName(String processName) {
			this.processName = processName;
		}
	}

	ProgressDialog pd = null;
	Handler handler = new RefreshHandler();

	private void updateProcessList() {
		pd = new ProgressDialog(TaskActivity.this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setTitle(getString(R.string.progress_tips_title));
		pd.setMessage(getString(R.string.progress_tips_content));

		RefreshThread thread = new RefreshThread();
		thread.start();

		pd.show();
	}

	private class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			taskAdapter.reloadData(procList);
			pd.dismiss();
		}
	}

	class RefreshThread extends Thread {
		@Override
		public void run() {
//			Thread.currentThread().sleep(3000);
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			getProcessInfo();
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
		}
	}

	// private Handler handler = new Handler();
	// private Runnable r = new Runnable() {
	// @Override
	// public void run() {
	// try {
	// Thread.sleep(2000);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// System.out.println("TaskActivity Runnalbe ---> " +
	// Thread.currentThread().getId());
	// }
	// };
}
