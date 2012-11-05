/**
 * @(#)HttpBySocket.java		2012-11-5
 *
 */
package com.njnu.kai.bmi;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 *
 * @version 1.0.0
 * @since 2012-11-5
 */
public class HttpBySocket {
	private static final String LOG_TAG = "HttpBySocket";

	private static StringBuilder strBuilder = new StringBuilder();

	static public String GetUseAutoEncoding(Context context, String url) {
		strBuilder.setLength(0);
		strBuilder.append("\nUrl: " + url);
		try {
			URL url2 = new URL(url);
			Socket socket = createSocket(context, url2);
			int urlPort = url2.getPort() < 0 ? url2.getDefaultPort() : url2.getPort();
			strBuilder.append("\nafter createSocket");

			OutputStream oos = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();
			String willWrite = "GET " + url2.getPath() + " HTTP/1.1\r\n";
			willWrite += "Host: " + url2.getHost() + ":" + urlPort + "\r\n";
			willWrite += "Accept: */*\r\n";
			willWrite += "Accept-Language: zh-cn\r\n";
			willWrite += "User-Agent: ttkai_by_socket_1.0\r\n";
			willWrite += "\r\n";
//			strBuilder.append("\nwill write: \n" + willWrite);
			oos.write(willWrite.getBytes());
			oos.flush();
			strBuilder.append("after flush oos\n");
//			byte[] buf = new byte[1024];
//			int readLen = 0;
//			while ((readLen = bufferedInputStream.read(buf)) > 0 && bufferedInputStream.) {
//				strBuilder.append(new String(buf, 0, readLen, "UTF-8"));
//			}
			strBuilder.append(readResponse(inputStream));
			oos.close();
			strBuilder.append("\nafter close oos");
			inputStream.close();
			strBuilder.append("\nafter read bis");
			socket.close();
			strBuilder.append("\nafter close bis");
		} catch (Exception e) {
			strBuilder.append("\n发生异常 " + e.toString() + " 原因:" + e.getMessage());
		}
		strBuilder.append('\n');
//		try {
//			new HttpBySocket().testHttp();
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return strBuilder.toString();
	}

	static Socket createSocket(Context context, URL url2) throws Exception {
		Properties properties = System.getProperties();
		String proxyHost = properties.getProperty("http.proxyHost", null);
		String proxyPort = properties.getProperty("http.proxyPort", null);
		strBuilder.append("\ncreateSocket: proxyHost=" + proxyHost + ":" + proxyPort);
		Socket socket = null;
		int urlPort = url2.getPort() < 0 ? url2.getDefaultPort() : url2.getPort();
		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		strBuilder.append("\nnetworkInfo=" + networkInfo.getType() + " name=" + networkInfo.getTypeName() + " connected=" + networkInfo.isConnected());
		socket = new Socket();
		strBuilder.append("\nin createSocket after new Socket");
		if (TextUtils.isEmpty(proxyHost)) {
			socket.connect(new InetSocketAddress(url2.getHost(), urlPort), 10000);
		} else {
//			socket = new Socket(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyHost, TextUtils.isEmpty(proxyPort) ? 80: Integer.parseInt(proxyPort))));
			socket.connect(new InetSocketAddress(proxyHost, TextUtils.isEmpty(proxyPort) ? 80: Integer.parseInt(proxyPort)), 10000);
		}
		strBuilder.append("\nin createSocket after connect");
		return socket;
	}


//	private final static byte[] CRLF = {'\r', '\n'};

    private static String readResponse(InputStream in) throws IOException {
        // 读取状态行
        String statusLine = readStatusLine(in);
        System.out.println("statusLine :" + statusLine);

        // 消息报头
        Map<String, String> headers = readHeaders(in);

        int contentLength = Integer.valueOf(headers.get("Content-Length"));

        // 可选的响应正文
        byte[] body = readResponseBody(in, contentLength);

        String charset = headers.get("Content-Type");
        if(charset.matches(".+;charset=.+")) {
            charset = charset.split(";")[1].split("=")[1];
        } else {
//            charset = "ISO-8859-1";     // 默认编码
            charset = "UTF-8";     // 默认编码
        }

//        System.out.println("content:\n" + new String(body, charset));
        return new String(body, charset);
    }

    private static byte[] readResponseBody(InputStream in, int contentLength) throws IOException {

        ByteArrayOutputStream buff = new ByteArrayOutputStream(contentLength);

        int b;
        int count = 0;
        while(count++ < contentLength) {
            b = in.read();
            buff.write(b);
        }

        return buff.toByteArray();
    }

    private static Map<String, String> readHeaders(InputStream in) throws IOException {
        Map<String, String> headers = new HashMap<String, String>();

        String line;

        while(!("".equals(line = readLine(in)))) {
            String[] nv = line.split(": ");     // 头部字段的名值都是以(冒号+空格)分隔的
            headers.put(nv[0], nv[1]);
        }

        return headers;
    }

    private static String readStatusLine(InputStream in) throws IOException {
        return readLine(in);
    }

    /**
     * 读取以CRLF分隔的一行，返回结果不包含CRLF
     */
    private static String readLine(InputStream in) throws IOException {
        int b;

        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        while((b = in.read()) != '\r') {
            buff.write(b);
        }

        in.read();      // 读取 LF

        String line = buff.toString();

        return line;
    }
}
