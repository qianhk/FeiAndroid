package com.njnu.kai.bmi;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Build;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

public class GeneralData {

	private Context mContext;

	public GeneralData(Context context) {
		super();
		mContext = context;
	}

	public List<String> getData() {
		List<String> list = new ArrayList<String>();

		list.add("测试软件版本: 201211161143");
		list.add("\nProduct: " + Build.PRODUCT);
      	list.add("\nCPU_ABI: " + Build.CPU_ABI);
		list.add("\nModel number: " + Build.MODEL);
		list.add("\nAndroid version: " + Build.VERSION.RELEASE);
		list.add("\nBuild Number: " + Build.DISPLAY);
		list.add("\nSDK API Level: " + Build.VERSION.SDK_INT);

		TelephonyManager phoneMgr = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
//		list.add("\nPhone number" + phoneMgr.getLine1Number());
		list.add("\nSimOperatorName: " + phoneMgr.getSimOperatorName());

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
		list.add("\nSim Type: " + simType);

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
			GsmCellLocation logsm = (GsmCellLocation)lo;
			cellType = String.format("GSM Cid=%08X Lac=%08X Mcc=%s Mnc=%s", logsm.getCid(), logsm.getLac(), gsmMcc, gsmMnc);
		} else if (lo != null) {
			CdmaCellLocation cdma = (CdmaCellLocation)lo;
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
		list.add("\nCellLocation: " + cellType);

		list.add("\nMCC MNC: " + "mcc=" + mContext.getResources().getConfiguration().mcc + " mnc=" + mContext.getResources().getConfiguration().mnc);
		list.add("\nIMEI/MEID: " + phoneMgr.getDeviceId());

//		String phoneInfo = "Product: " + android.os.Build.PRODUCT;
//        phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI;
//        phoneInfo += ", TAGS: " + android.os.Build.TAGS;
//        phoneInfo += ", VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE;
//        phoneInfo += ", MODEL: " + android.os.Build.MODEL;
//        phoneInfo += ", SDK: " + android.os.Build.VERSION.SDK;
//        phoneInfo += ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
//        phoneInfo += ", DEVICE: " + android.os.Build.DEVICE;
//        phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY;
//        phoneInfo += ", BRAND: " + android.os.Build.BRAND;
//        phoneInfo += ", BOARD: " + android.os.Build.BOARD;
//        phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT;
//        phoneInfo += ", ID: " + android.os.Build.ID;
//        phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER;
//        phoneInfo += ", USER: " + android.os.Build.USER;
//
//        list.add("\n" + phoneInfo);
		return list;
	}
}
