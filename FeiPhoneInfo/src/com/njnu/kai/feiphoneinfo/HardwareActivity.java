package com.njnu.kai.feiphoneinfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HardwareActivity extends Activity implements OnItemClickListener {

	private CMDExecute cmd = null;
	ActivityManager activityManager = null;
	ListView lv = null;
	List<Map<String, Object>> mLastData = null;
	String mLastBattery = "unknow";
	HardwareAdapter adapter = null;

	private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int level = intent.getIntExtra("level", 0);
			mLastBattery = level + "%";
			for(Map<String, Object> map : mLastData) {
				if (map.get("title").equals("Battery")) {
					map.put("info", mLastBattery);
					break;
				}
			}
			adapter.reloadData(mLastData);
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		cmd = new CMDExecute();
		activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		lv = new ListView(this);
		mLastData = getData();
		// SimpleAdapter adapter = new SimpleAdapter(this, mLastData,
		// R.layout.list_item_number_two_text,
		// new String[] { "title", "info", "no" }, new int[] { R.id.text1,
		// R.id.text2, R.id.orderno });
		adapter = new HardwareAdapter(this);
		lv.setAdapter(adapter);
		adapter.reloadData(mLastData);
		setContentView(lv);
		lv.setOnItemClickListener(this);
		registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.addSubMenu(0, 3001, 3001, "Flush");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Log.v("TaskActivity", item.getTitle().toString() + " id=" +
		// item.getItemId());
		boolean sucess = true;
		switch (item.getItemId()) {
		case 3001:
			mLastData = getData();
			adapter.reloadData(mLastData);
			break;
		default:
			sucess = super.onOptionsItemSelected(item);
		}
		return sucess;
	}

	public class HardwareAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private List<Map<String, Object>> mLastData = null;

		public HardwareAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		public void reloadData(List<Map<String, Object>> data) {
			mLastData = data;
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mLastData != null ? mLastData.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_item_number_two_text, null);
			}
			Map<String, Object> item = mLastData.get(position);
			TextView tv = (TextView) convertView.findViewById(R.id.text1);
			tv.setText(item.get("title").toString());
			tv = (TextView) convertView.findViewById(R.id.text2);
			tv.setText(item.get("info").toString());
			tv = (TextView) convertView.findViewById(R.id.orderno);
			tv.setText(String.valueOf(position + 1));
			return convertView;
		}

	}

    private String getIp(){
        WifiManager wm=(WifiManager)getSystemService(Context.WIFI_SERVICE);
        //检查Wifi状态
        if(!wm.isWifiEnabled())
            return null;

        WifiInfo wi=wm.getConnectionInfo();
        //获取32位整型IP地址
        int ipAdd=wi.getIpAddress();
        //把整型地址转换成“*.*.*.*”地址
        return intToIp(ipAdd);
    }

    private String intToIp(int i) {
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }

    private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String linuxVer = cmd.executeCat("/proc/version");
		int pos = linuxVer.indexOf(" (");
		if (pos >= 0) {
			linuxVer = linuxVer.substring(0, pos);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "Linux version");
		map.put("info", linuxVer);
		// map.put("no", 1);
		list.add(map);

		String cpuInfo = cmd.executeCat("/proc/cpuinfo");
		pos = cpuInfo.indexOf(": ");
		int pos2 = cpuInfo.indexOf("\n");
		cpuInfo = cpuInfo.substring(pos + 2, pos2);
		map = new HashMap<String, Object>();
		map.put("title", "Cpu info");
		map.put("info", cpuInfo);
		// map.put("no", 2);
		list.add(map);

		ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memInfo);
		String memStr = cmd.executeCat("/proc/meminfo");
		Pattern pattern = Pattern.compile("(\\d+)");
		Matcher match = pattern.matcher(memStr);
		if (match.find()) {
			int allMem = Integer.parseInt(match.group(1));
			memStr = "" + allMem / 1024;
		}
		map = new HashMap<String, Object>();
		map.put("title", "Memory");
		map.put("info", "Total: " + memStr + " MB  Available: " + memInfo.availMem / 1024 / 1024 + " MB");
		// map.put("no", 3);
		list.add(map);

        String netInfo = getIp();
        if (netInfo == null) {
		    netInfo = cmd.execute(new String[] { "/system/bin/netcfg" });
            if (netInfo.length() > 0) {
                netInfo = netInfo.substring(0, netInfo.length() - 1);
            }
        }
		map = new HashMap<String, Object>();
		map.put("title", "Network");
		map.put("info", netInfo);
		// map.put("no", 4);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Screen");
		map.put("info", getDisplayMetrics());
		// map.put("no", 5);
		list.add(map);

		cpuInfo = cmd.executeCat("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
//		Log.v("Hardware Activity", "cpu freq=" + cpuInfo);
		long cpuFreq = -1;
		try {
			cpuInfo = cpuInfo.substring(0, cpuInfo.length() - 1);
			cpuFreq = Long.valueOf(cpuInfo);
		}
		catch (Exception e) {
		}
		map = new HashMap<String, Object>();
		map.put("title", "CPU Freq");
		map.put("info", cpuFreq > 0 ? (cpuFreq / 1000 + " MHz") : "unknow");
		// map.put("no", 6);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Battery");
		map.put("info", mLastBattery);
		// map.put("no", 7);
		list.add(map);

		long[] romInfo = getRomMemroy();
		map = new HashMap<String, Object>();
		map.put("title", "Rom info");
		map.put("info", "All: " + formatSize(romInfo[0]) + "  Available: " + formatSize(romInfo[1]));
		// map.put("no", 8);
		list.add(map);

		long[] cardInfo = getSDCardMemory();
		map = new HashMap<String, Object>();
		map.put("title", "SDCard info");
		map.put("info", "All: " + formatSize(cardInfo[0]) + "  Available: " + formatSize(cardInfo[1]));
		// map.put("no", 9);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Mac Address");
		map.put("info", getMacAddress());
		// map.put("no", 10);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Startup ElapsedTime");
		map.put("info", getStartupElapsedTimes());
		// map.put("no", 11);
		list.add(map);

		return list;
	}

	private String getMacAddress() {
		String mac = "unknow";
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (wifiInfo.getMacAddress() != null) {
			mac = wifiInfo.getMacAddress();
		}
		return mac;
	}

	private String getStartupElapsedTimes() {
		long ut = SystemClock.elapsedRealtime() / 1000;
		if (ut == 0) {
			ut = 1;
		}
		int m = (int) ((ut / 60) % 60);
		int h = (int) ((ut / 3600));
		return h + " h : " + m + " m";
	}

	public String getDisplayMetrics() {
		// DisplayMetrics dm =
		// cx.getApplicationContext().getResources().getDisplayMetrics();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return String.valueOf(dm.widthPixels) + " * " + dm.heightPixels + "    Density : " + dm.density + "    DPI : " + dm.xdpi + ", "
				+ dm.ydpi;
	}

	public long[] getSDCardMemory() {
		long[] sdCardInfo = new long[2];
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long bSize = sf.getBlockSize();
			long bCount = sf.getBlockCount();
			long availBlocks = sf.getAvailableBlocks();

			sdCardInfo[0] = bSize * bCount;// 总大小
			sdCardInfo[1] = bSize * availBlocks;// 可用大小
		}
		return sdCardInfo;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// Log.v("Hardware", "Clicked " + id + " " + ((TextView) view).getText()
		// +" pos=" + position);
		// Log.v("Hardware", "Clicked " + id + " " + view +" pos=" + position);

	}

	public long[] getRomMemroy() {
		long[] romInfo = new long[2];
		// Total rom memory
		romInfo[0] = getTotalInternalMemorySize();

		// Available rom memory
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		romInfo[1] = blockSize * availableBlocks;
		return romInfo;
	}

	public long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	public String formatSize(long size) {
		String suffix = " B";
		float fSize = 0;

		if (size >= 1024) {
			suffix = " KB";
			fSize = size / 1024;
			if (fSize >= 1024) {
				suffix = " MB";
				fSize /= 1024;
			}
			if (fSize >= 1024) {
				suffix = " GB";
				fSize /= 1024;
			}
		} else {
			fSize = size;
		}
		java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
		StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}
}
