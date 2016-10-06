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
import com.njnu.kai.mockclick.util.KaiInput;

import java.io.IOException;

/**
 * @author kai
 * @since 16/10/3
 */
public class ClickHandler extends Handler {

    public static final int METHOD_INSTRUMENTATION = 1;
    public static final int METHOD_SU_INPUT = 2;
    public static final int METHOD_SEND_EVENT = 3;
    public static final int METHOD_INVOKE_SYS_JAVA = 4;
    public static final int METHOD_INVOKE_OWNER_JAVA = 5;

    private static final String TAG = "ClickHandler";

    private static KaiInput mInput;

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

            } else if (what == R.id.nav_instrumentation_click) {
                doTestClick(METHOD_INSTRUMENTATION, 672, 112);
            } else if (what == R.id.nav_instrumentation_click_multi_100) {
                doTestClickMulti(METHOD_INSTRUMENTATION, 100);
            } else if (what == R.id.nav_instrumentation_click_multi_1000) {
                doTestClickMulti(METHOD_INSTRUMENTATION, 1000);

            } else if (what == R.id.nav_su_input_click) {
                doTestClick(METHOD_SU_INPUT, 672, 112);
            } else if (what == R.id.nav_su_input_click_multi_10) {
                doTestClickMulti(METHOD_SU_INPUT, 10);
            } else if (what == R.id.nav_su_input_click_multi_100) {
                doTestClickMulti(METHOD_SU_INPUT, 100);
            } else if (what == R.id.nav_su_input_click_multi_1000) {
                doTestClickMulti(METHOD_SU_INPUT, 1000);

            } else if (what == R.id.nav_sys_event_click) {
                doTestClick(METHOD_SEND_EVENT, 672, 112);
            } else if (what == R.id.nav_sys_event_click_multi_10) {
                doTestClickMulti(METHOD_SEND_EVENT, 10);
            } else if (what == R.id.nav_sys_event_click_multi_100) {
                doTestClickMulti(METHOD_SEND_EVENT, 100);

            } else if (what == R.id.nav_sys_java_input_click) {
                doTestClick(METHOD_INVOKE_SYS_JAVA, 672, 112);
            } else if (what == R.id.nav_sys_java_input_click_multi_10) {
                doTestClickMulti(METHOD_INVOKE_SYS_JAVA, 10);
            } else if (what == R.id.nav_sys_java_input_click_multi_100) {
                doTestClickMulti(METHOD_INVOKE_SYS_JAVA, 100);
            } else if (what == R.id.nav_sys_java_input_click_multi_1000) {
                doTestClickMulti(METHOD_INVOKE_SYS_JAVA, 1000);
            } else if (what == R.id.nav_sys_java_input_click_second_5) {
                if (mInput == null) {
                    mInput = new KaiInput();
                }
                mInput.sendTapTime(444, 777, 5000);
            } else if (what == R.id.nav_kai_java_input_click_second_8) {
                Injector.kaiInputTapTime(350, 700, 8000);
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
            boolean canLargeExecuteCommand = Injector.canLargeExecuteCommand();
            if (canLargeExecuteCommand) {
                Injector.beginCommand2();
            }
            Injector.touch2UseInputTap(x, y);
            if (canLargeExecuteCommand) {
                Injector.endCommand2();
            }
        } else if (method == METHOD_SEND_EVENT) {
            boolean canLargeExecuteCommand = Injector.canLargeExecuteCommand();
            if (canLargeExecuteCommand) {
                Injector.beginCommand2();
            }
            Injector.touch2UseSendEvent(x, y);
            if (canLargeExecuteCommand) {
                Injector.endCommand2();
            }
        } else if (method == METHOD_INVOKE_SYS_JAVA) {
            if (mInput == null) {
                mInput = new KaiInput();
            }
            mInput.sendTap(x, y);
        }
    }

    private void doTestClickMulti(int method, int times) throws IOException, InterruptedException {

        if (method == METHOD_INVOKE_SYS_JAVA) {
            if (mInput == null) {
                mInput = new KaiInput();
            }
            mInput.sendTapTimes(333, 666, times);
            return;
        }

        if (method == METHOD_SU_INPUT || method == METHOD_SEND_EVENT) {
            Injector.beginCommand2();
        }

        int widthPixels = DisplayUtils.getWidthPixels();
        int heightPixels = DisplayUtils.getHeightPixels();
        for (int idx = 0; idx < times; ++idx) {
            int x = idx % widthPixels;
            x = widthPixels - x;
            if (x <= 0 || x >= widthPixels) {
                x = 88;
            }
            int y = idx % (heightPixels - 222) + 222;
            if (y >= heightPixels) {
                y = 288;
            }
            doTestClick(method, x, y);
        }
        if (method == METHOD_SU_INPUT || method == METHOD_SEND_EVENT) {
            Injector.endCommand2();
        }
    }
}
