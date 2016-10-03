package com.njnu.kai.mockclick;

import android.app.Instrumentation;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

import com.njnu.kai.mockclick.util.DisplayUtils;
import com.njnu.kai.mockclick.util.Injector;

import java.io.IOException;

/**
 * @author kai
 * @since 16/10/3
 */
public class ClickHandler extends Handler {

    public static final int METHOD_INSTRUMENTATION = 1;
    public static final int METHOD_SU_INPUT = 2;
    public static final int METHOD_INVOKE_JAVA = 3;

    private static final String TAG = "ClickHandler";

    public ClickHandler(Looper looper) {
        super(looper);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int what = msg.what;

        try {
            if (what == R.id.nav_manage) {

            } else if (what == R.id.nav_test_click) {
                doTestClick(METHOD_INSTRUMENTATION, 672, 112);
            } else if (what == R.id.nav_test_click_multi) {
                doTestClickMulti(METHOD_INSTRUMENTATION);
            } else if (what == R.id.nav_su_test_click) {
                doTestClick(METHOD_SU_INPUT, 672, 112);
            } else if (what == R.id.nav_su_test_click_multi) {
                doTestClickMulti(METHOD_SU_INPUT);
            } else if (what == R.id.nav_su_test_click_multi_1000) {
                doTestClickMulti1000(METHOD_SU_INPUT);
            }
        } catch (Throwable t) {
            Log.e(TAG, "handleMessage kai found exception", t);
        }
    }

    private void doTestClick(int method, int x, int y) throws IOException, InterruptedException {
        if (method == METHOD_INSTRUMENTATION) {
            Instrumentation inst = new Instrumentation();
            inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0));
            inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0));
        } else if (method == METHOD_SU_INPUT) {
            Injector.touch(x, y);
        }
    }

    private void doTestClickMulti(int method) throws IOException, InterruptedException {
        int widthPixels = DisplayUtils.getWidthPixels();
        for (int idx = 0; idx < 100; ++idx) {
            doTestClick(method, widthPixels - idx - 1, idx + 200);
        }
    }

    private void doTestClickMulti1000(int method) throws IOException, InterruptedException {
        int widthPixels = DisplayUtils.getWidthPixels();
        for (int idx = 0; idx < 1000; ++idx) {
            int x = widthPixels - idx - 1;
            if (x < 0) {
                x = -x;
            } else if (x == 0) {
                x = 88;
            }
            doTestClick(method, x, idx + 200);
        }
    }
}
