/**
 *
 */
package com.njnu.kai.bmi;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Locale;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class HttpConnectionUtility {
	private static final String LOG_TAG = "HttpConnectionUtility";
	private final static String _user_agent = "agent_ttkai_own";

	static public String GetUseAutoEncoding(Context context, String url, boolean useAgent) {
		String text = "发生错误：";
		try {
			HttpURLConnection connection = getRedirectHttpConnection(context, url, 0, 10000, 10000, useAgent);
			InputStream inputStream = connection.getInputStream();
			BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
			byte[] buf = new byte[1024];
			StringBuilder builder = new StringBuilder(1024);
			int readLen = 0;
			while ((readLen = bufferedInputStream.read(buf)) > 0) {
				builder.append(new String(buf, "UTF8"));
			}
			text = builder.toString();
			connection.disconnect();
		} catch (Exception e) {
			text += e.getMessage();
		}
		return text;
	}

	public static HttpURLConnection getRedirectHttpConnection(Context context, String url, long range, int connectTimeout, int readTimeout, boolean useAgent) {
		// 第一次网络连接很可能出现咨费页面
		// 如果是资费页面，则再发�?�?��请求
		// 并不能排除某些情�?
//		if (url.startsWith("http://lrc.ttpod.com")) {
//			TTLog.i(LOG_TAG, "xxxxxx  lyric url = " + url);
//		}
		HttpURLConnection hc = null;
		int mNetWorkType = getNetWorkType(context);
		int status = 0;
		try {
			hc = getHttpConnetionInternel(url, mNetWorkType, range, connectTimeout, readTimeout, useAgent);

			status = hc.getResponseCode();
			if (status == 307
					|| status == HttpURLConnection.HTTP_MOVED_TEMP
					|| status == HttpURLConnection.HTTP_MOVED_PERM) {
				String location = hc.getHeaderField("Location");
				if (location != null) {
					String lowerCaseLocation = location.toLowerCase(Locale.US);
					if (!lowerCaseLocation.startsWith("http://") && !lowerCaseLocation.startsWith("https://")) {
						int idx = url.lastIndexOf('/');
						if (idx >= 0) {
							url = url.substring(0, idx + 1);
						}
						location = url + location;
					}
					hc.disconnect();
					hc = getHttpConnetionInternel(location, mNetWorkType, range, connectTimeout, readTimeout, useAgent);
				}
			} else if (status == HttpURLConnection.HTTP_NOT_FOUND) {
//                TTLog.d(LOG_TAG, "http code : " + status + " url: " + url);
				hc.disconnect();
				hc = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 出现错误，最好打印出错误的url，方便查错, yu.liu
//            TTLog.d(LOG_TAG, "err code : " + status + " url: " + url + " err:" + e.getMessage());
			if (hc != null) {
				hc.disconnect();
				hc = null;
			}
		}
		return hc;
	}

	private static HttpURLConnection getHttpConnetionInternel(String url, int netMode, long range, int connectTimeout, int readTimeout, boolean useAgent)
			throws Exception {
		HttpURLConnection hc = null;
		URL httpUrl = null;
		if (url != null) {
			httpUrl = new URL(url);
		} else {
			return hc;
		}
		switch (netMode) {
		default:
		case NETWORKTYPE_2G:
		case NETWORKTYPE_3G:
		case NETWORKTYPE_WIFI:
			hc = (HttpURLConnection)httpUrl.openConnection();
			break;
		case NETWORKTYPE_WAP:
			String proxyHost = android.net.Proxy.getDefaultHost();

			Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxyHost, 80));
			hc = (HttpURLConnection)httpUrl.openConnection(proxy);

			if (!proxyHost.equals("10.0.0.200")) {
				hc.setRequestProperty("X-Online-Host", httpUrl.getAuthority());
			}

			break;
		}

		if (range > 0) {
			hc.setRequestProperty("RANGE", "bytes=" + range + "-");
		}
		if (useAgent) {
			hc.setRequestProperty("Accept", "*/*");
			hc.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			hc.setRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
			hc.setRequestProperty("User-Agent", "TTPod_Agent_Android_1.0");
		}
		hc.setConnectTimeout(connectTimeout);
		hc.setReadTimeout(readTimeout);
		return hc;
	}

	private static int getNetWorkType(Context context) {

		int networktye = NETWORKTYPE_INVALID;
		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			String type = networkInfo.getTypeName();

			if (type.equalsIgnoreCase("WIFI")) {
				networktye = NETWORKTYPE_WIFI;
			} else if (type.equalsIgnoreCase("MOBILE")) {
				String proxyHost = android.net.Proxy.getDefaultHost();

				networktye = TextUtils.isEmpty(proxyHost)
						? (isFastMobileNetwork(context) ? NETWORKTYPE_3G : NETWORKTYPE_2G)
						: NETWORKTYPE_WAP;
			}
		} else {
			networktye = NETWORKTYPE_INVALID;
		}

		return networktye;
	}

	private static boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false; // ~ 14-64 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return true; // ~ 400-1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return true; // ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return false; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return true; // ~ 2-14 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return true; // ~ 700-1700 kbps
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return true; // ~ 1-23 Mbps
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return true; // ~ 400-7000 kbps
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return true; // ~ 1-2 Mbps
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return true; // ~ 5 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return true; // ~ 10-20 Mbps
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return false; // ~25 kbps
		case TelephonyManager.NETWORK_TYPE_LTE:
			return true; // ~ 10+ Mbps
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		default:
			return false;
		}
	}

	/** 没有网络 */
	public static final int NETWORKTYPE_INVALID = 0;
	/** wap网络 */
	public static final int NETWORKTYPE_WAP = 1;
	/** 2G网络 */
	public static final int NETWORKTYPE_2G = 2;
	/** 3G和3G以上网络，或统称为快速网络 */
	public static final int NETWORKTYPE_3G = 3;
	/** wifi网络 */
	public static final int NETWORKTYPE_WIFI = 4;

}
