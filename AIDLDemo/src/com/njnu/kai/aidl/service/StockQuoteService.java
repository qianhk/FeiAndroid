/**
 * @(#)StockQuoteService.java		2012-11-17
 *
 */
package com.njnu.kai.aidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 *
 * @version 1.0.0
 * @since 2012-11-17
 */
public class StockQuoteService extends Service {
	private static final String LOG_TAG = "StockQuoteService";

	@Override
	public IBinder onBind(Intent intent) {
		Log.v(LOG_TAG, "onBind(). " + intent);
		return new StockQuoteServiceImpl();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v(LOG_TAG, "onCreate().");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(LOG_TAG, "onDestroy().");
	}

	public class StockQuoteServiceImpl extends IStockQuoteService.Stub {

		@Override
		public double getQuote(String ticker) throws RemoteException {
			Log.v(LOG_TAG, "getQuote(" + ticker + ") called.");
			return 21.3;
		}
	}
}
