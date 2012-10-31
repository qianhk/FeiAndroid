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

public class HttpConnectionUtilityPink {
	private static final String LOG_TAG = "HttpConnectionUtility";
	private final static String _user_agent = "agent_ttkai_own";

	public static final int CONNECTION_TIMEOUT = 10000;
	public static final int READ_TIMEOUT = 10000;

	public static final int NETWORK_INVALID = -1;
	public static final int NETWORK_2G = 0;
	public static final int NETWORK_WAP = 1;
	public static final int NETWORK_WIFI = 2;
	public static final int NETWORK_3G = 3;
	public static final int HTTP_STATE_REDIRECT = 307;
	public static final int PORT = 80;
	private static final int[] NETWORK_MATCH_TABLE = {NETWORK_2G // NETWORK_TYPE_UNKNOWN
			, NETWORK_2G // NETWORK_TYPE_GPRS
			, NETWORK_2G // NETWORK_TYPE_EDGE
			, NETWORK_3G // NETWORK_TYPE_UMTS
			, NETWORK_2G // NETWORK_TYPE_CDMA
			, NETWORK_3G // NETWORK_TYPE_EVDO_O
			, NETWORK_3G // NETWORK_TYPE_EVDO_A
			, NETWORK_2G // NETWORK_TYPE_1xRTT
			, NETWORK_3G // NETWORK_TYPE_HSDPA
			, NETWORK_3G // NETWORK_TYPE_HSUPA
			, NETWORK_3G // NETWORK_TYPE_HSPA
			, NETWORK_2G // NETWORK_TYPE_IDEN
			, NETWORK_3G // NETWORK_TYPE_EVDO_B
			, NETWORK_3G // NETWORK_TYPE_LTE
			, NETWORK_3G // NETWORK_TYPE_EHRPD
			, NETWORK_3G // NETWORK_TYPE_HSPAP
	};
	private static int mNetWorkType = NETWORK_INVALID;

	static public String GetUseAutoEncoding(Context context, String url) {
		String text = "发生错误：";
		try {
			HttpURLConnection connection = getRedirectHttpConnection(context, url, 0);
			InputStream inputStream = connection.getInputStream();
			BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
			byte[] buf = new byte[1024];
			StringBuilder builder = new StringBuilder(1024);
			int readLen = 0;
			while ((readLen = bufferedInputStream.read(buf)) > 0) {
				builder.append(new String(buf, 0, readLen, "UTF-8"));
			}
			text = builder.toString();
			connection.disconnect();
		} catch (Exception e) {
			text += e.getMessage();
		}
		return text;
	}

	public static HttpURLConnection getRedirectHttpConnection(Context context, String url, long range) {

		HttpURLConnection hc = null;
		int mNetWorkType = getNetWorkType(context);
		int status = 0;
		try {
			hc = getHttpConnetionInternel(url, mNetWorkType, range);

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
					hc = getHttpConnetionInternel(location, mNetWorkType, range);
				}
			} else if (status == HttpURLConnection.HTTP_NOT_FOUND) {
//                TTLog.d(LOG_TAG, "http code : " + status + " url: " + url);
				hc.disconnect();
				hc = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
//            TTLog.d(LOG_TAG, "err code : " + status + " url: " + url + " err:" + e.getMessage());
			if (hc != null) {
				hc.disconnect();
				hc = null;
			}
		}
		return hc;
	}

	private static HttpURLConnection getHttpConnetionInternel(String url, int netMode, long range)
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
		case NETWORK_2G:
		case NETWORK_3G:
		case NETWORK_WIFI:
			hc = (HttpURLConnection)httpUrl.openConnection();
			break;
		case NETWORK_WAP:
			String proxyHost = android.net.Proxy.getDefaultHost();

			Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxyHost, PORT));
			hc = (HttpURLConnection)httpUrl.openConnection(proxy);

			// 电信的wap代理地址是10.0.0.200，如果是电信的wap代理，则不用设置X-Online-Host和Accept，否则连接不成功
			if (!proxyHost.equals("10.0.0.200")) {
				hc.setRequestProperty("X-Online-Host", httpUrl.getAuthority());
				hc.setRequestProperty("Accept", "*/*");
			}

			break;
		}

		if (range > 0) {
			hc.setRequestProperty("RANGE", "bytes=" + range + "-");
		}

		hc.setRequestProperty("Connection", "Keep-Alive");
		hc.setRequestProperty("Accept", "*/*");
		hc.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		hc.setRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
		hc.setRequestProperty("User-Agent", "TTPod_Agent_Android_1.0");

		hc.setConnectTimeout(CONNECTION_TIMEOUT);
		hc.setReadTimeout(READ_TIMEOUT);
		hc.connect();
		return hc;
	}

	public static int getNetWorkType(Context context) {
		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		if (!networkConnected(networkInfo)) {
			mNetWorkType = NETWORK_INVALID;
		} else if (isWifiNetwork(networkInfo)) {
			mNetWorkType = NETWORK_WIFI;
		} else if (isWapNetwork(networkInfo)) {
			mNetWorkType = NETWORK_WAP;
		} else {
			TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			mNetWorkType = NETWORK_MATCH_TABLE[telephonyManager.getNetworkType()];
		}
		return mNetWorkType;
	}

	public static boolean isNetWorkAvailable(Context aContext) {
		ConnectivityManager connectManager = (ConnectivityManager)aContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectManager != null && networkConnected(connectManager.getActiveNetworkInfo());
	}

	private static boolean networkConnected(NetworkInfo networkInfo) {
		return networkInfo != null && networkInfo.isConnected();
	}

	private static boolean isMobileNetwork(NetworkInfo networkInfo) {
		return networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
	}

	private static boolean isWapNetwork(NetworkInfo networkInfo) {
		return isMobileNetwork(networkInfo) && !TextUtils.isEmpty(android.net.Proxy.getDefaultHost());
	}

	private static boolean isWifiNetwork(NetworkInfo networkInfo) {
		return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
	}

}
