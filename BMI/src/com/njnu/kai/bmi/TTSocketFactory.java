package com.njnu.kai.bmi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

/**
 * @(#)TTSocketFactory.java		2012-11-2
 *
 */

/**
 *
 * @version 1.0.0
 * @since 2012-11-2
 */
public class TTSocketFactory extends SocketFactory {
	private static final String LOG_TAG = "TTSocketFactory";

	private static TTSocketFactory defaultFactory;

	public static synchronized SocketFactory getDefault() {
        if (defaultFactory == null) {
            defaultFactory = new TTSocketFactory();
        }
        return defaultFactory;
    }

	@Override
	public Socket createSocket() throws IOException {
		Proxy proxy = new Proxy(java.net.Proxy.Type.SOCKS, new InetSocketAddress("10.0.0.172", 80));
        return new Socket(proxy);
    }

	@Override
	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		return null;
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
		return null;
	}

	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {
		return null;
	}

	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
		return null;
	}

}
