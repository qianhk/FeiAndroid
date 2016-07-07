package com.njnu.kai.rn.modules;

import android.app.Activity;
import android.util.Log;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 */
public class ReactAppManagerModule extends ReactContextBaseJavaModule {

    private static final String TAG = "ReactAppManagerModule";

    public ReactAppManagerModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "TestAppManager";
    }

    @ReactMethod
    public void restartPage(Promise promise) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            Log.e(TAG, "restartPage activity is null");
        } else {
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    currentActivity.recreate();
                }
            });
        }
    }
}
