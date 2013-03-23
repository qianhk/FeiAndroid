package com.njnu.kai.ExceptionTest;

import android.app.Application;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 13-3-23
 */
public class KaiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        KaiCrashHandler.getInstance().init(getApplicationContext());
    }
}
