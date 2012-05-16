package com.njnu.kai.feiphoneinfo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class GeneralActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		ListView listView = new ListView(this);
//		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getData1()));
//		setContentView(listView);

		 ListView lv = new ListView(this);
		 SimpleAdapter adapter = new SimpleAdapter(this, getData(),
		 R.layout.list_item_number_two_text,
		 new String[] { "title", "info", "no" }, new int[] { R.id.text1,
		 R.id.text2, R.id.orderno });
		 lv.setAdapter(adapter);
		 setContentView(lv);
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "Model number");
		map.put("info", Build.MODEL);
		map.put("no", 1);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Android version");
		map.put("info", Build.VERSION.RELEASE);
		map.put("no", 2);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Build Number");
		map.put("info", Build.DISPLAY);
		map.put("no", 3);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "SDK API Level");
		map.put("info", Build.VERSION.SDK_INT);
		map.put("no", 4);
		list.add(map);

		TelephonyManager phoneMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		map = new HashMap<String, Object>();
		map.put("title", "Phone number");
		map.put("info", phoneMgr.getLine1Number());
		map.put("no", 5);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "SimOperatorName");
		map.put("info", phoneMgr.getSimOperatorName());
		map.put("no", 6);
		list.add(map);

		String simType = "unknow";
		int type = phoneMgr.getNetworkType();
		if (type == TelephonyManager.NETWORK_TYPE_UMTS) {
			simType = "USIM/WCDMA";// 类型为UMTS定义为wcdma的USIM卡
		} else if (type == TelephonyManager.NETWORK_TYPE_GPRS) {
			simType = "SIM/GPRS";// 类型为GPRS定义为GPRS的SIM卡
		} else if (type == TelephonyManager.NETWORK_TYPE_EDGE) {
			simType = "SIM/EDGE";// 类型为EDGE定义为EDGE的SIM卡
		} else {
			simType = "UIM/CDMA";// 类型为unknown定义为cdma的UIM卡
		}
		map = new HashMap<String, Object>();
		map.put("title", "Sim Type");
		map.put("info", simType);
		map.put("no", 7);
		list.add(map);

		CellLocation lo = phoneMgr.getCellLocation();
		String cellType = "unknow";
		if (lo instanceof GsmCellLocation) {
			String mccMnc = phoneMgr.getNetworkOperator();
			String gsmMcc = "";
			String gsmMnc = "";
			if (mccMnc != null && mccMnc.length() >= 5) {
				gsmMcc = mccMnc.substring(0, 3);
				gsmMnc = mccMnc.substring(3, 5);
			}
			GsmCellLocation logsm = (GsmCellLocation) lo;
			cellType = String.format("GSM Cid=%08X Lac=%08X Mcc=%s Mnc=%s", logsm.getCid(), logsm.getLac(), gsmMcc, gsmMnc);
		} else {
			CdmaCellLocation cdma = (CdmaCellLocation) lo;
			String mccMnc = phoneMgr.getNetworkOperator();
			String cdmaMcc = "";
			String cdmaMnc = "";
			if (mccMnc != null && mccMnc.length() >= 5) {
				cdmaMcc = mccMnc.substring(0, 3);
				cdmaMnc = mccMnc.substring(3, 5);
			}
			cellType = String.format("CDMA StationId=%d NetworkId=%d SysId=%d Mcc=%s Mnc=%s", cdma.getBaseStationId(), cdma.getNetworkId(),
					cdma.getSystemId(), cdmaMcc, cdmaMnc);
		}
		map = new HashMap<String, Object>();
		map.put("title", "CellLocation");
		map.put("info", cellType);
		map.put("no", 8);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "IMEI/MEID");
		map.put("info", phoneMgr.getDeviceId());
		map.put("no", 9);
		list.add(map);

		return list;
	}

	public String getSerialNumber() {
		String serial = null;
//		try {
//			Class<?> c = Class.forName("android.os.SystemProperties");
//			Method get = c.getMethod("get", String.class);
//			serial = (String) get.invoke(c, "ro.serialno");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		serial = Secure.getString(getContentResolver(),
//				Secure.ANDROID_ID);
		serial = new DeviceUuidFactory(this).getDeviceUuid().toString();
		return serial;
	}

	private List<String> getData1() {

		List<String> data = new ArrayList<String>();
		data.add(Build.DISPLAY); // Build Number
		data.add(Build.CPU_ABI);
		data.add(Build.DEVICE);
		// data.add(Build.HARDWARE); //2.1-update1 java.lang.NoSuchFieldError
		data.add(Build.ID);
		data.add(Build.MODEL); // sdk //手机型号 Model number
		data.add(Build.PRODUCT); // sdk
		// data.add(Build.SERIAL); //2.1 no
data.add(getSerialNumber());
		data.add(Build.VERSION.CODENAME);
		data.add(Build.VERSION.SDK + " int=" + Build.VERSION.SDK_INT); // sdk版本号
																		// api
		// level, ex: 5 10 15
		// data.add(Build.CPU_ABI2); //2.1 no
		data.add(Build.BRAND); // Baseband version?
		data.add(Build.MANUFACTURER);
		// data.add(Build.BOOTLOADER); //2.1 no
		data.add(Build.BOARD);
		data.add(Build.FINGERPRINT);
		// data.add(Build.RADIO); //2.1 no
		data.add(Build.TYPE);
		data.add(Build.HOST);
		data.add(Build.VERSION.RELEASE);// Firmware/OS 版本号 //2.3.3 Android
										// version

		TelephonyManager phoneMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		data.add(phoneMgr.getLine1Number()); // Phone number
		data.add(phoneMgr.getDeviceId()); // MEID
		String softV = phoneMgr.getDeviceSoftwareVersion();
		data.add(softV != null ? softV : "<no Device Software Version>");
		data.add(phoneMgr.getNetworkCountryIso());
		data.add(phoneMgr.getNetworkOperator() + " name=" + phoneMgr.getNetworkOperatorName()); // 运营商
																								// 英文
		data.add(phoneMgr.getSimOperatorName()); // 手机卡运营商 中文
		CellLocation lo = phoneMgr.getCellLocation();
		data.add(lo != null ? lo.toString() : "<no location>");
		String subid = phoneMgr.getSubscriberId();
		data.add(subid != null ? subid : "<no subscriberId>");

		return data;
	}

	public class DeviceUuidFactory {
	    protected static final String PREFS_FILE = "device_id.xml";
	    protected static final String PREFS_DEVICE_ID = "device_id";

	    protected UUID uuid;

	    public DeviceUuidFactory(Context context) {

	        if( uuid ==null ) {
	            synchronized (DeviceUuidFactory.class) {
	                if( uuid == null) {
	                    final SharedPreferences prefs = context.getSharedPreferences( PREFS_FILE, 0);
	                    final String id = prefs.getString(PREFS_DEVICE_ID, null );

	                    if (id != null) {
	                        // Use the ids previously computed and stored in the prefs file
	                        uuid = UUID.fromString(id);

	                    } else {

	                        final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

	                        // Use the Android ID unless it's broken, in which case fallback on deviceId,
	                        // unless it's not available, then fallback on a random number which we store
	                        // to a prefs file
	                        try {
	                             if ( "9774d56d682e549c".equals(androidId) || (androidId == null) ) {
	                                final String deviceId = ((TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();

	                                if (deviceId != null)
	                                {
	                                    uuid = UUID.nameUUIDFromBytes(deviceId.getBytes("utf8"));
	                                }
	                                else
	                                {
	                                    uuid = UUID.randomUUID();

	                                    // Write the value out to the prefs file so it persists
	                                    prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString() ).commit();
	                                }
	                            }
	                            else
	                            {
	                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
	                            }
	                        } catch (UnsupportedEncodingException e) {
	                            throw new RuntimeException(e);
	                        }



	                    }

	                }
	            }
	        }

	    }


	    /**
	     * Returns a unique UUID for the current android device.  As with all UUIDs, this unique ID is "very highly likely"
	     * to be unique across all Android devices.  Much more so than ANDROID_ID is.
	     *
	     * The UUID is generated by using ANDROID_ID as the base key if appropriate, falling back on
	     * TelephonyManager.getDeviceID() if ANDROID_ID is known to be incorrect, and finally falling back
	     * on a random UUID that's persisted to SharedPreferences if getDeviceID() does not return a
	     * usable value.
	     *
	     * In some rare circumstances, this ID may change.  In particular, if the device is factory reset a new device ID
	     * may be generated.  In addition, if a user upgrades their phone from certain buggy implementations of Android 2.2
	     * to a newer, non-buggy version of Android, the device ID may change.  Or, if a user uninstalls your app on
	     * a device that has neither a proper Android ID nor a Device ID, this ID may change on reinstallation.
	     *
	     * Note that if the code falls back on using TelephonyManager.getDeviceId(), the resulting ID will NOT
	     * change after a factory reset.  Something to be aware of.
	     *
	     * Works around a bug in Android 2.2 for many devices when using ANDROID_ID directly.
	     *
	     * @see http://code.google.com/p/android/issues/detail?id=10603
	     *
	     * @return a UUID that may be used to uniquely identify your device for most purposes.
	     */
	    public UUID getDeviceUuid() {
	        return uuid;
	    }
	}
}
