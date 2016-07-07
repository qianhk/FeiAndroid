package com.njnu.kai.rn.modules;

import android.app.Activity;
import android.util.Log;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.devsupport.DevSupportManager;
import com.facebook.react.devsupport.DisabledDevSupportManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 */
public class ReactAppManagerModule extends ReactContextBaseJavaModule {

    private static final String TAG = "ReactAppManagerModule";

    private static final String REACT_APPLICATION_CLASS_NAME = "com.facebook.react.ReactApplication";
    private static final String REACT_NATIVE_HOST_CLASS_NAME = "com.facebook.react.ReactNativeHost";

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
                    reloadBundle();
                }
            });
        }
    }

    private void reloadBundle() {
        final Activity currentActivity = getCurrentActivity();

        if (!ReactActivity.class.isInstance(currentActivity)) {
            // Our preferred reload logic relies on the user's Activity inheriting
            // from the core ReactActivity class, so if it doesn't, we fallback
            // early to our legacy behavior.
            loadBundleLegacy(currentActivity);
        } else {
            try {
                ReactActivity reactActivity = (ReactActivity)currentActivity;
                ReactInstanceManager instanceManager;

                // #1) Get the ReactInstanceManager instance, which is what includes the
                //     logic to reload the current React context.
                try {
                    // In RN 0.29, the "mReactInstanceManager" field yields a null value, so we try
                    // to get the instance manager via the ReactNativeHost, which only exists in 0.29.
                    Method getApplicationMethod = ReactActivity.class.getMethod("getApplication");
                    Object reactApplication = getApplicationMethod.invoke(reactActivity);
                    Class reactApplicationClass = tryGetClass(REACT_APPLICATION_CLASS_NAME);
                    Method getReactNativeHostMethod = reactApplicationClass.getMethod("getReactNativeHost");
                    Object reactNativeHost = getReactNativeHostMethod.invoke(reactApplication);
                    Class reactNativeHostClass = tryGetClass(REACT_NATIVE_HOST_CLASS_NAME);
                    Method getReactInstanceManagerMethod = reactNativeHostClass.getMethod("getReactInstanceManager");
                    instanceManager = (ReactInstanceManager)getReactInstanceManagerMethod.invoke(reactNativeHost);
                } catch (Exception e) {
                    // The React Native version might be older than 0.29, so we try to get the
                    // instance manager via the "mReactInstanceManager" field.
                    Field instanceManagerField = ReactActivity.class.getDeclaredField("mReactInstanceManager");
                    instanceManagerField.setAccessible(true);
                    instanceManager = (ReactInstanceManager)instanceManagerField.get(reactActivity);
                }
                //restartPage instanceManager is: com.facebook.react.ReactInstanceManagerImpl
                Log.i(TAG, "restartPage instanceManager is: " + instanceManager.getClass().getName());
                try {
                    Field devSupportManagerField = instanceManager.getClass().getDeclaredField("mDevSupportManager");
                    devSupportManagerField.setAccessible(true);
                    DevSupportManager devSupportManager = (DevSupportManager)devSupportManagerField.get(instanceManager);
                    if (devSupportManager instanceof DisabledDevSupportManager) {
                        loadBundleLegacy(currentActivity);
                    } else {
                        devSupportManager.handleReloadJS();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                // Our reflection logic failed somewhere
                // so fall back to restarting the Activity
                loadBundleLegacy(currentActivity);
            }
        }
    }

    private void loadBundleLegacy(final Activity currentActivity) {
        currentActivity.recreate();
    }

    private Class tryGetClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
