package com.njnu.kai.bmi;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpUtility {

	public final static String TTGETHEADER = "\u0068\u0074\u0074\u0070\u003a\u002f\u002f\u0062\u0062\u0073\u002e\u0074\u0074\u0070\u006f\u0064\u002e\u0063\u006f\u006d\u002f\u0067\u0065\u0074\u0068\u0065\u0061\u0064\u0065\u0072\u002e\u0070\u0068\u0070";
	public final static String TTLRCSEARCH = "\u0068\u0074\u0074\u0070\u003a\u002f\u002f\u006c\u0072\u0063\u002e\u0074\u0074\u0070\u006f\u0064\u002e\u0063\u006f\u006d\u002f\u0073\u0065\u0061\u0072\u0063\u0068\u003f\u0074\u0069\u0074\u006c\u0065\u003d\u0054\u0068\u0065\u0025\u0032\u0030\u0044\u0061\u0079\u0025\u0032\u0030\u0059\u006f\u0075\u0025\u0032\u0030\u0057\u0065\u006e\u0074\u0025\u0032\u0030\u0041\u0077\u0061\u0079\u0026\u0061\u0072\u0074\u0069\u0073\u0074\u003d\u0026\u0072\u0061\u0077\u003d\u0032";
	public final static String TTLRCDOWN = "\u0068\u0074\u0074\u0070\u003a\u002f\u002f\u006c\u0072\u0063\u002e\u0074\u0074\u0070\u006f\u0064\u002e\u0063\u006f\u006d\u002f\u0064\u006f\u0077\u006e";
	public final static String TTLRCMIME = "\u0068\u0074\u0074\u0070\u003a\u002f\u002f\u006c\u0072\u0063\u002e\u0074\u0074\u0070\u006f\u0064\u002e\u0063\u006f\u006d\u002f\u006d\u0069\u006d\u0065\u0074\u0065\u0073\u0074\u002f\u0069\u006e\u0064\u0065\u0078\u002e\u0068\u0074\u006d\u006c";

	private static final String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private static final String HTTP_HEADER_VALUE_ACCEPT_ENCODING_GZIP = "gzip";

	static private String encodeChineseUrl(String url, String charsetName) {
		String encodeUrl;
		try {
			encodeUrl = URLEncoder.encode(url, charsetName);
			encodeUrl = encodeUrl.replace("%2F", "/");
			encodeUrl = encodeUrl.replace("%3A", ":");
			encodeUrl = encodeUrl.replace("+", "%20");
			encodeUrl = encodeUrl.replace("%3F", "?");
			encodeUrl = encodeUrl.replace("%3D", "=");
			encodeUrl = encodeUrl.replace("%26", "&");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			encodeUrl = url;
		}
		return encodeUrl;
	}

	private static boolean isFirstConnect = true;

	static public String GetUseAutoEncoding(String url) {
		AbstractHttpClient client = new DefaultHttpClient();
		// client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		HttpParams httpParam = client.getParams();
		httpParam.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Http_Timeout_Time);
		httpParam.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, Http_Timeout_Time);
		httpParam.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		// System.out.println(httpParam.getParameter(ClientPNames.COOKIE_POLICY));
		String ls_content = "";
		try {
			String encodeUrl = encodeChineseUrl(url, "UTF8");
			HttpGet httpGet = new HttpGet(encodeUrl);
			httpGet.setHeader("Referer", _lastUrl);
			httpGet.setHeader("User-Agent", _user_agent);
			client.setCookieStore(_cookieStore);
			// httpGet.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
			HttpResponse response = client.execute(httpGet);
			_lastUrl = encodeUrl;
			HttpEntity entity = response.getEntity();
			// System.out.println(response.getStatusLine());
			if (entity != null) {
				if (isFirstConnect) {
					isFirstConnect = false;
					Header type = entity.getContentType();
					if (type != null && type.getValue().indexOf("/vnd.wap.") != -1) {
						return GetUseAutoEncoding(url);
					}
				}
				if (entity != null) {
					String charset = EntityUtils.getContentCharSet(entity);
//					InputStream is = entity.getContent();
//					byte bb[] = new byte[512];
//					int rLen = 0;
//					StringBuilder strB = new StringBuilder();
//					while ((rLen = is.read(bb)) > 0) {
//						strB.append(new String(bb, 0, rLen, (charset == null) ? "UTF-8" : charset));
//					}
					ls_content = EntityUtils.toString(entity, "UTF-8");
//					is.close();
//					Header[] headers = response.getHeaders("Accept-Ranges");
//					System.out.println(headers);;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ls_content = e.toString() + " " + e.getMessage();
		} finally {
			client.getConnectionManager().shutdown();
		}

		return "HttpUtility:\nUrl: " + url + "\n" + ls_content;
	}

	private static InputStream gZipDecompression(InputStream inputStreamWithGZip) throws IOException {
		InputStream inputStream;
		// 取前两个字节
		byte[] header = new byte[2];
		BufferedInputStream bis = new BufferedInputStream(inputStreamWithGZip);
		bis.mark(2);
		int result = bis.read(header);
		// reset输入流到开始位置
		bis.reset();
		// 判断是否是 GZIP 格式
		final int flag = 0xff;
		final int offset = 8;
		int ss = (header[0] & flag) | ((header[1] & flag) << offset);
		if (result != -1 && ss == GZIPInputStream.GZIP_MAGIC) {
			inputStream = new GZIPInputStream(bis);
		} else {
			// 如果不是，gzip格式，还需要取前两个字节
			inputStream = bis;
		}

		return inputStream;
	}

	static public String WriteToFile(String url, String filePath) {
		AbstractHttpClient client = new DefaultHttpClient();
		// client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		HttpParams httpParam = client.getParams();
		httpParam.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Http_Timeout_Time);
		httpParam.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, Http_Timeout_Time);
		httpParam.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		// System.out.println(httpParam.getParameter(ClientPNames.COOKIE_POLICY));
		String ls_content = "";
		try {
			String encodeUrl = encodeChineseUrl(url, "UTF8");
//			String encodeUrl = url.replace(' ', '+');
			HttpGet httpGet = new HttpGet(encodeUrl);
			httpGet.setHeader("Referer", _lastUrl);
			httpGet.setHeader("User-Agent", _user_agent);
			httpGet.setHeader(HTTP_HEADER_ACCEPT_ENCODING, HTTP_HEADER_VALUE_ACCEPT_ENCODING_GZIP);
			client.setCookieStore(_cookieStore);
			// httpGet.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
			HttpResponse response = client.execute(httpGet);
			_lastUrl = encodeUrl;
			HttpEntity entity = response.getEntity();
			// System.out.println(response.getStatusLine());
			if (entity != null) {
				if (isFirstConnect) {
					isFirstConnect = false;
					Header type = entity.getContentType();
					if (type != null && type.getValue().indexOf("/vnd.wap.") != -1) {
						return GetUseAutoEncoding(url);
					}
				}
				if (entity != null) {
//					String charset = EntityUtils.getContentCharSet(entity);

					InputStream is = gZipDecompression(entity.getContent());
					File of = new File(filePath);
					if (of.exists()) {
						of.delete();
					}
					FileOutputStream fos = new FileOutputStream(filePath);
					byte bb[] = new byte[512];
					int rLen = 0;
					int allLen = 0;
					while ((rLen = is.read(bb)) > 0) {
						allLen += rLen;
						fos.write(bb, 0, rLen);
					}
					fos.flush();
					fos.close();
					Header ctHeader = entity.getContentType();
					String hhh = ctHeader != null ? ctHeader.getValue() : null;
					ctHeader = entity.getContentEncoding();
					String encoding = ctHeader != null ? ctHeader.getValue() : null;
					ls_content = "Down Complete: " + filePath + " downlen=" + allLen + " contentLen=" + entity.getContentLength()
							+ " ContentType=" + hhh + " encoding=" + encoding;
					is.close();
//					Header[] headers = response.getHeaders("Accept-Ranges");
//					System.out.println(headers);;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ls_content = e.toString() + " " + e.getMessage();
		} finally {
			client.getConnectionManager().shutdown();
		}

		return "HttpUtility:\nUrl: " + url + "\n" + ls_content;
	}

	static public String PostUseAutoEncoding(String url, String postData, String encoding) {
		String content = "";
		try {
			StringEntity postEntity = new StringEntity(postData, encoding);
			postEntity.setContentType(URLEncodedUtils.CONTENT_TYPE + HTTP.CHARSET_PARAM
					+ (encoding != null ? encoding : HTTP.DEFAULT_CONTENT_CHARSET));
			content = PostUseAutoEncoding(url, postEntity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return content;
	}

	static public String PostUseAutoEncoding(String url, List<NameValuePair> formparams, String encoding) {
		String postData = URLEncodedUtils.format(formparams, encoding);
		return PostUseAutoEncoding(url, postData, encoding);
	}

	static private String PostUseAutoEncoding(String url, HttpEntity postEntity) {
		AbstractHttpClient client = new DefaultHttpClient();
		HttpParams httpParam = client.getParams();
		// HttpConnectionParams.setConnectionTimeout(httpParam,
		// Http_Timeout_Time);
		httpParam.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Http_Timeout_Time);
		httpParam.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, Http_Timeout_Time);
		httpParam.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		String ls_content = "";
		try {
			String encodeUrl = encodeChineseUrl(url, "UTF8");
			HttpPost httpPost = new HttpPost(encodeUrl);
			httpPost.setHeader("Referer", _lastUrl);
			httpPost.setHeader("User-Agent", _user_agent);
			httpPost.setEntity(postEntity);
			client.setCookieStore(_cookieStore);
			HttpResponse response = client.execute(httpPost);
			_lastUrl = encodeUrl;
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String charset = EntityUtils.getContentCharSet(entity);
				ls_content = EntityUtils.toString(entity, ((charset == null) ? "UTF-8" : charset));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}

		return ls_content;
	}

	public static String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
										// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
											// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
											// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
															// >>>
															// 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	private static String _lastUrl = "";
	private static CookieStore _cookieStore = new BasicCookieStore();

	private final static int Http_Timeout_Time = 30000;
	public final static String _user_agent = "Mozilla/5.0 (Linux; U; Android 2.3.6; en-us; Nexus S Build/GRK39F) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
//	private final static String _user_agent = "agent_ttkai";

}
