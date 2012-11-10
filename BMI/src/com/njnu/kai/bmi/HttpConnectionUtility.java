/**
 *
 */
package com.njnu.kai.bmi;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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

	private static boolean firstConnected = true;

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
	private static StringBuilder strBuilder = new StringBuilder();

	static public String GetUseAutoEncoding(Context context, String url) {
		strBuilder.setLength(0);
		strBuilder.append("HttpConnectionUtility:\nUrl: " + url);
		try {
			HttpURLConnection connection = getRedirectHttpConnection(context, url, 0);
			if (connection != null) {
				InputStream inputStream = connection.getInputStream();
				BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
				byte[] buf = new byte[1024];
				int readLen = 0;
				while ((readLen = bufferedInputStream.read(buf)) > 0) {
					strBuilder.append(new String(buf, 0, readLen, "UTF-8"));
				}
				connection.disconnect();
			} else {
				strBuilder.append("\n上面文字提示中应有异常发生。");
			}
		} catch (Exception e) {
			strBuilder.append("\n发生异常 " + e.toString() + " 原因:" + e.getMessage());
		}
		strBuilder.append('\n');
		return strBuilder.toString();
	}

	public static HttpURLConnection getRedirectHttpConnection(Context context, String url, long range) {

		HttpURLConnection hc = null;
		int mNetWorkType = getNetWorkType(context);
		int status = 0;
		try {
			strBuilder.append("\ngetRedirectHttpConnection A1");
			hc = getHttpConnetionInternel(url, mNetWorkType, range);
			strBuilder.append(" A2");
			if (firstConnected) {
				strBuilder.append(" A3");
				String type = hc.getContentType();
				strBuilder.append(" A4");
				if (type != null && type.indexOf("/vnd.wap.") != -1) {
					strBuilder.append(" A5");
					strBuilder.append("\nfirstConnected reGet Request Response Code = " + hc.getResponseCode() + " contentType=" + type);
					hc.disconnect();
					strBuilder.append(" A6");
					hc = getHttpConnetionInternel(url, mNetWorkType, range);
					strBuilder.append(" A7");
				}
				firstConnected = false;
				strBuilder.append(" A8");
			}

			strBuilder.append(" A9");
			status = hc.getResponseCode();
			strBuilder.append(" A10");
			strBuilder.append("\nFirst Request Response Code = " + status + " contentType=" + hc.getContentType());
//			if (status == 307 || status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM) {
//				String location = hc.getHeaderField("Location");
//				strBuilder.append("\nLocation=" + location);
//				if (location != null) {
//					String lowerCaseLocation = location.toLowerCase(Locale.US);
//					strBuilder.append(" LowerLocation=" + lowerCaseLocation);
//					if (!lowerCaseLocation.startsWith("http://") && !lowerCaseLocation.startsWith("https://")) {
//						int idx = url.lastIndexOf('/');
//						if (idx >= 0) {
//							url = url.substring(0, idx + 1);
//						}
//						location = url + location;
//						strBuilder.append("\nNew Location=" + lowerCaseLocation + " trunkedUrl=" + url);
//					}
//					hc.disconnect();
//					hc = getHttpConnetionInternel(location, mNetWorkType, range);
//					strBuilder.append("\nSecond Request Response Code = " + hc.getResponseCode());
//				}
//			} else
			if (status == HttpURLConnection.HTTP_NOT_FOUND) {
//                TTLog.d(LOG_TAG, "http code : " + status + " url: " + url);
				strBuilder.append(" A11");
				hc.disconnect();
				hc = null;
			}
		} catch (Exception e) {
			strBuilder.append(" A12");
			e.printStackTrace();
//            TTLog.d(LOG_TAG, "err code : " + status + " url: " + url + " err:" + e.getMessage());
			strBuilder.append("\ngetRedirectHttpConnection Exception: " + e.toString() + " 原因:" + e.getMessage() + "\nhc is null ? " + (hc == null));
			if (hc != null) {
				strBuilder.append(" A13");
				InputStream errorStream = hc.getErrorStream();
				strBuilder.append(" A14");
				try {
					InputStreamReader reader = new InputStreamReader(errorStream, "UTF-8");
					char tmpS[] = new char[128];
					int l;
					strBuilder.append(" readErrorStream: ");
					while ((l = reader.read(tmpS)) != -1) {
						strBuilder.append(tmpS, 0, l);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					strBuilder.append(" readErrorStream error: " + e1.toString() + " reason" + e.getMessage());
				}

				strBuilder.append(" A15");
				hc.disconnect();
				strBuilder.append(" A16");
				hc = null;
			}
		}
		strBuilder.append(" A17");
		return hc;
	}

	private static HttpURLConnection getHttpConnetionInternel(String url, int netMode, long range)
			throws Exception {
		HttpURLConnection hc = null;
		strBuilder.append("\ngetHttpConnetionInternel B1 ");
		URL httpUrl = null;
		if (url != null) {
			httpUrl = new URL(url);
		} else {
			return hc;
		}
		strBuilder.append(" B2");
		boolean chinaTelecom = false;
		switch (netMode) {
		default:
		case NETWORK_2G:
		case NETWORK_3G:
		case NETWORK_WIFI:
			hc = (HttpURLConnection)httpUrl.openConnection();
			break;
		case NETWORK_WAP:
			String proxyHost = android.net.Proxy.getDefaultHost();
			strBuilder.append(" B3");
			Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxyHost, PORT));
			strBuilder.append(" B4");
			hc = (HttpURLConnection)httpUrl.openConnection(proxy);
			strBuilder.append(" B5");

			// 电信的wap代理地址是10.0.0.200，如果是电信的wap代理，则不用设置X-Online-Host和Accept，否则连接不成功
			chinaTelecom = proxyHost != null && proxyHost.equalsIgnoreCase("10.0.0.200");
			strBuilder.append(" B6 " + chinaTelecom);
			if (!chinaTelecom) {
				hc.setRequestProperty("X-Online-Host", httpUrl.getAuthority());
			}
			strBuilder.append(" B7 authority=" + httpUrl.getAuthority() + " ");

			break;
		}

		if (range > 0) {
			hc.setRequestProperty("RANGE", "bytes=" + range + "-");
		}
		strBuilder.append(" B8");
		if (!chinaTelecom) {
			hc.setRequestProperty("Accept", "application/xml,application/vnd.wap.xhtml+xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,text/vnd.wap.wml;q=0.6,*/*;q=0.5,UC/145,plugin/1");
		}
		strBuilder.append(" B9");
		hc.setDoInput(true);
		if (hc.getRequestMethod().equalsIgnoreCase("POST")) {
			hc.setDoOutput(true);
		}
		strBuilder.append(" RequestMethod:" + hc.getRequestMethod());
		hc.setRequestProperty("Connection", "Keep-Alive");
		hc.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		hc.setRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
		hc.setRequestProperty("User-Agent", HttpUtility._user_agent);
		hc.setRequestProperty("Cache-Control", "max-age=0");
		strBuilder.append(" B10");
		hc.setConnectTimeout(CONNECTION_TIMEOUT);
		hc.setReadTimeout(READ_TIMEOUT);
		strBuilder.append(" B11");
		hc.setFollowRedirects(true);
		hc.connect();
		strBuilder.append(" B12");
		return hc;
	}

	public static int getNetWorkType(Context context) {
		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		strBuilder
				.append("\nnetworkInfo=" + networkInfo.getType() + " name=" + networkInfo.getTypeName() + " connected=" + networkInfo.isConnected());
		strBuilder.append("\nProxy=" + android.net.Proxy.getDefaultHost() + ":" + android.net.Proxy.getDefaultPort() + " apn="
				+ networkInfo.getExtraInfo());

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
		strBuilder.append("\nTTNetworkType=" + mNetWorkType);
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
