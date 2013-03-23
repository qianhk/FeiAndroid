package com.njnu.kai.ExceptionTest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 13-3-23
 */
public class KaiService extends Service {
    private static final String LOG_TAG = "KaiService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        startCommand(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startCommand(intent);
        return START_NOT_STICKY;
    }

    private void startCommand(Intent intent) {
        boolean thread = intent.getBooleanExtra("thread", false);
        if (thread) {
            Runnable runnable = new Runnable() {
                public void run() {
                    Log.d(LOG_TAG, "will crash In Service Thread:");
                    int i = 1 / 0;
                }
            };
            new Thread(runnable).start();
        }
        else {
            Log.d(LOG_TAG, "will crash In Service:");
            int i = 1 / 0;
        }
    }
}
