package com.njnu.kai.mockclick;

import android.app.Application;

import com.njnu.kai.mockclick.util.DisplayUtils;

/**
 * @author kai
 * @since 16/10/3
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DisplayUtils.init(this);
    }

}
