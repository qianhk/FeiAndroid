package com.njnu.kai.rn;

import android.os.Bundle;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.microsoft.codepush.react.CodePush;
import com.njnu.kai.rn.view.CustomReactPackage;

import javax.annotation.Nullable;
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
    protected String getJSBundleFile() {
        return CodePush.getJSBundleFile("index.android.bundle");
    }

    /**
     * Returns whether dev mode should be enabled.
     * This enables e.g. the dev menu.
     */
    @Override
    protected boolean getUseDeveloperSupport() {
        return BuildConfig.DEBUG;
    }

    /**
     * A list of packages used by the app. If the app uses additional views
     * or modules besides the default ones, add more packages here.
     */
    @Override
    protected List<ReactPackage> getPackages() {
        String deploymentKey = true ? "hv4DIHXtcGtdChiPqxgwXDFL9kSKVyyaBGmLZ" : "Et9W_AvJDNwcQi8qv4WgwGRe7PD2VyyaBGmLZ";
        ArrayList<ReactPackage> reactPackageList = new ArrayList<>();
        reactPackageList.add(new MainReactPackage());
        reactPackageList.add(new CodePush(deploymentKey, this));
        reactPackageList.add(new CustomReactPackage());
        return reactPackageList;
    }
}
