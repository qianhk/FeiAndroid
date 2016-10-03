package com.njnu.kai.mockclick;

import android.app.Instrumentation;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.MotionEvent;

import com.njnu.kai.mockclick.util.DisplayUtils;

/**
 * @author kai
 * @since 16/10/3
 */
public class ClickHandler extends Handler {

    public ClickHandler(Looper looper) {
        super(looper);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int what = msg.what;

        if (what == R.id.nav_camera) {
        } else if (what == R.id.nav_manage) {

        } else if (what == R.id.nav_test_click) {
            doTestClick(672, 112);
        } else if (what == R.id.nav_test_click_multi) {
            doTestClickMulti();
        } else if (what == R.id.nav_test_click_multi_1000) {
            doTestClickMulti1000();
        }
    }

    private void doTestClick(int x, int y) {
        Instrumentation inst = new Instrumentation();
        inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0));
        inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0));
    }

    private void doTestClickMulti() {
        int widthPixels = DisplayUtils.getWidthPixels();
        for (int idx = 0; idx < 100; ++idx) {
            doTestClick(widthPixels - idx, idx + 200);
        }
    }

    private void doTestClickMulti1000() {
        int widthPixels = DisplayUtils.getWidthPixels();
        for (int idx = 0; idx < 1000; ++idx) {
            int x = widthPixels - idx;
            if (x < 0) {
                x = -x;
            } else if (x == 0) {
                x = 88;
            }
            doTestClick(x, idx + 200);
        }
    }
}
