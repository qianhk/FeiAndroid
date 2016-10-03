package com.njnu.kai.mockclick.util;

import android.content.Context;
import android.graphics.Point;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Date: 12-8-31
 * Time: 下午4:26
 *
 */
public class ToastUtils {

    private final static int DEFAULT_DURATION = Toast.LENGTH_SHORT;

    private static Toast mToast;
    private static View mToastDefaultView;
    private static int mToastDefaultGravity;
    private static Point mToastDefaultPosition;

    /**
     * 自定义Toast样式
     *
     * @param context  当前上下文
     * @param message  显示的信息内容
     * @param duration 显示时长，单位毫秒，如果输入0，则默认显示时长为 Toast.LENGTH_SHORT
     * @param view     显示toast的View，如果传入view，则必须指定 xOffset 和 yOffset
     * @param xOffset  显示x的偏移量，如果设置，起始点为屏幕左上角  (0,0)
     * @param yOffset  显示y的偏移量，如果设置，起始点为屏幕左上角  (0,0)
     */
    public static void showToast(Context context, CharSequence message, int duration, View view, int xOffset, int yOffset) {
        if (context == null) {
            return;
        }

        if (duration == 0) {
            duration = DEFAULT_DURATION;
        }

        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), message, duration);
            mToastDefaultView = mToast.getView();
            mToastDefaultGravity = mToast.getGravity();
            mToastDefaultPosition = new Point(mToast.getXOffset(), mToast.getYOffset());
        }

        if (view != null) {
            ViewGroup viewGroup = (ViewGroup) view;

            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childView = viewGroup.getChildAt(i);
                if (childView instanceof TextView) {
                    ((TextView) childView).setText(message);
                }
            }

            mToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, xOffset, yOffset);
            mToast.setView(view);

        } else {
            mToast.setView(mToastDefaultView);
            mToast.setGravity(mToastDefaultGravity, mToastDefaultPosition.x, mToastDefaultPosition.y);
            mToast.setText(message);
        }

        mToast.setDuration(duration);
        mToast.show();
    }

    /**
     * 使用系统方式显示 toast， 显示时间长度为 Toast.LENGTH_SHORT
     *
     * @param context           当前上下文
     * @param messageResourceId 显示的信息内容
     */
    public static void showToast(Context context, int messageResourceId) {
        if (context == null) {
            return;
        }
        String message = context.getResources().getString(messageResourceId);
        showToast(context, message, DEFAULT_DURATION, null, 0, 0);
    }

    /**
     * @param context context
     * @param format format
     * @param params params
     */
    public static void showToast(Context context, String format, Object... params) {
        String msg = String.format(format, params);
        showToast(context, msg);
    }

    /**
     * 使用系统方式显示 toast， 显示时间长度为 Toast.LENGTH_SHORT
     *
     * @param context 当前上下文
     * @param message 显示的信息内容
     */
    public static void showToast(Context context, CharSequence message) {
        showToast(context, message, DEFAULT_DURATION, null, 0, 0);
    }

}
