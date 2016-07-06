package com.njnu.kai.rn;

import android.os.Bundle;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.microsoft.codepush.react.CodePush;
import com.njnu.kai.rn.view.CustomReactPackage;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ReactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
    }

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "RnPractice";
    }

    @Nullable
    @Override
    protected Bundle getLaunchOptions() {
        Bundle bundle = new Bundle();
        bundle.putString("key_from", "native_main_activity");
        bundle.putLong("key_long", 1044L);
        bundle.putInt("key_int", 83);
        bundle.putFloat("key_float", 66.66f);
        bundle.putDouble("key_double", 66.66);
        bundle.putStringArray("key_string_array", new String[] {"item1", "item2"});
        bundle.putIntArray("key_int_array", new int[] {4, 5}); //不可long型数组

        Bundle bundleInfo = new Bundle();
        bundleInfo.putLong("key_info_long", 100L);
        bundleInfo.putString("key_stu_name", "中文参数");
        bundle.putBundle("key_info", bundleInfo);

        bundle.putBoolean("native_build_debug", BuildConfig.DEBUG);

//        String version = "unknown";
//        try {
//            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//            version = pInfo.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        bundle.putString("native_version", version);
        bundle.putString("native_version", BuildConfig.VERSION_NAME);

        return bundle;
    }

    @Nullable
    @Override
    protected String getJSBundleFile() {
//        return CodePush.getJSBundleFile("index.android.bundle");
//        return null;
        return getFilesDir().getAbsolutePath() + File.separator + "ReactNativeDevBundle.js";
    }

    /**
     * Returns whether dev mode should be enabled.
     * This enables e.g. the dev menu.
     */
    @Override
    protected boolean getUseDeveloperSupport() {
        return false;
    }

    /**
     * A list of packages used by the app. If the app uses additional views
     * or modules besides the default ones, add more packages here.
     */
    @Override
    protected List<ReactPackage> getPackages() {
        ArrayList<ReactPackage> reactPackageList = new ArrayList<>();
        reactPackageList.add(new MainReactPackage());
        if (!BuildConfig.DEBUG) {
            String deploymentKey = true ? "hv4DIHXtcGtdChiPqxgwXDFL9kSKVyyaBGmLZ" : "Et9W_AvJDNwcQi8qv4WgwGRe7PD2VyyaBGmLZ";
            reactPackageList.add(new CodePush(deploymentKey, this, BuildConfig.DEBUG));
        }
        reactPackageList.add(new CustomReactPackage());
        return reactPackageList;
    }
}
