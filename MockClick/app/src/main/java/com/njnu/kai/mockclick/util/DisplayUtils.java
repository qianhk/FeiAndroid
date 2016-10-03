package com.njnu.kai.mockclick.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

/**
 * @version 1.0.0
 */
public class DisplayUtils {

    private static DisplayMetrics sDisplayMetrics = null;
    private static Configuration sConfiguration = null;
    private static Typeface sIconTypeFace;
    private static float sSystemStatusBarHeight;

    /**
     * ldpi
     */
    public static final String EX_DENSITY_LOW = "_ldpi";
    /**
     * mdpi
     */
    public static final String EX_DENSITY_MEDIUM = "_mdpi";
    /**
     * hdpi
     */
    public static final String EX_DENSITY_HIGH = "_hdpi";
    /**
     * xhdpi
     */
    public static final String EX_DENSITY_XHIGH = "_xhdpi";
    /**
     * xxhdpi
     */
    public static final String EX_DENSITY_XXHIGH = "_xxhdpi";

    private static final float ROUND_DIFFERENCE = 0.5f;
    private static final int DENSITY_400 = 400;

    private static Resources sResources;

    /**
     * 初始化操作
     * @param context context
     */
    public static void init(Context context) {
        sResources = context.getResources();
        sDisplayMetrics = sResources.getDisplayMetrics();
        sConfiguration = sResources.getConfiguration();
        initStatusBarHeight(context);
        try {
            sIconTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/IconFont.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 配置发生变化
     * @param context context
     * @param newConfiguration newConfiguration
     */
    public static void onConfigurationChanged(Context context, Configuration newConfiguration) {
        sDisplayMetrics = context.getResources().getDisplayMetrics();
        sConfiguration = newConfiguration;
    }

    /**
     * 获取屏幕宽度 单位：像素
     * @return 屏幕宽度
     */
    public static int getWidthPixels() {
        return sDisplayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度 单位：像素
     * @return 屏幕高度
     */
    public static int getHeightPixels() {
        return sDisplayMetrics.heightPixels;
    }

    /**
     * 获取Density
     * @return Density
     */
    public static float getDensity() {
        return sDisplayMetrics.density;
    }

    /**
     * 获取DensityDpi
     * @return DensityDpi
     */
    public static int getDensityDpi() {
        return sDisplayMetrics.densityDpi;
    }

    /**
     * dp 转 px
     * 注意正负数的四舍五入规则
     * @param dp dp值
     * @return 转换后的像素值
     */
    public static int dp2px(int dp) {
        return (int)(dp * sDisplayMetrics.density + (dp > 0 ? ROUND_DIFFERENCE : -ROUND_DIFFERENCE));
    }

    /**
     * @param resId res id
     * @return dimen文件相关id里指定的值，返回px
     */
    public static int dimenPx(int resId) {
        return sResources.getDimensionPixelSize(resId);
    }

    /**
     * px 转 dp
     * 注意正负数的四舍五入规则
     * @param px px值
     * @return 转换后的dp值
     */
    public static int px2dp(int px) {
        return (int)(px / sDisplayMetrics.density + (px > 0 ? ROUND_DIFFERENCE : -ROUND_DIFFERENCE));
    }

    /**
     * get bitmap density
     * @return String
     */
    public static String getBitmapDensityStr() {
        switch (getBitmapDensity()) {
            case DisplayMetrics.DENSITY_LOW:
                return EX_DENSITY_LOW;
            case DisplayMetrics.DENSITY_MEDIUM:
                return EX_DENSITY_MEDIUM;
            case DisplayMetrics.DENSITY_HIGH:
                return EX_DENSITY_HIGH;
            case DisplayMetrics.DENSITY_XHIGH:
                return EX_DENSITY_XHIGH;
            case DisplayMetrics.DENSITY_XXHIGH:
                return EX_DENSITY_XXHIGH;
            default:
                return "";
        }
    }

    /**
     * @return 图形字体
     */
    public static Typeface getIconTypeFace() {
        return sIconTypeFace;
    }

    /**
     * 获取bitmapDensity
     * @return bitmapDensity
     */
    public static int getBitmapDensity() {
        int densityDpi = sDisplayMetrics.densityDpi;
        if (densityDpi <= DisplayMetrics.DENSITY_LOW) {
            return DisplayMetrics.DENSITY_LOW;
        } else if (densityDpi <= DisplayMetrics.DENSITY_MEDIUM) {
            return DisplayMetrics.DENSITY_MEDIUM;
        } else if (densityDpi <= DisplayMetrics.DENSITY_HIGH) {
            return DisplayMetrics.DENSITY_HIGH;
        } else if (densityDpi <= DisplayMetrics.DENSITY_XHIGH) {
            return DisplayMetrics.DENSITY_XHIGH;
        } else if (densityDpi <= /*DisplayMetrics.DENSITY_400*/DENSITY_400) {
            return DisplayMetrics.DENSITY_XHIGH;
        } else {
            return DisplayMetrics.DENSITY_XXHIGH;
        }
    }

    /**
     * 是否为竖屏
     * @return true/false
     */
    public static boolean isPortrait() {
        return sConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT
                || (sConfiguration.orientation == Configuration.ORIENTATION_UNDEFINED && getHeightPixels() > getWidthPixels());
    }

    public static float getStatusBarHeight() {
        return sSystemStatusBarHeight;
    }

    private static void initStatusBarHeight(Context context) {
        sSystemStatusBarHeight = (float)dp2px(25);

        try {
            Class c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            sSystemStatusBarHeight = (float)context.getResources().getDimensionPixelSize(x);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }
}
