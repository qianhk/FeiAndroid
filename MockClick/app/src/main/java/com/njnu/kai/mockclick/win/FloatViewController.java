package com.njnu.kai.mockclick.win;

/**
 * @author kai
 * @since 16/10/4
 */

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.njnu.kai.mockclick.R;
import com.njnu.kai.mockclick.util.ToastUtils;

public final class FloatViewController implements View.OnClickListener, View.OnTouchListener, ViewContainer.KeyEventHandler {

    private WindowManager mWindowManager;
    private Context mContext;
    private ViewContainer mWholeView;
    private View mContentView;
    private ViewDismissHandler mViewDismissHandler;

    private View.OnClickListener mOutOnclickListener;

    public FloatViewController(Context application, View.OnClickListener onClickListener) {
        mContext = application;
        mOutOnclickListener = onClickListener;
        mWindowManager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
    }

    public void setViewDismissHandler(ViewDismissHandler viewDismissHandler) {
        mViewDismissHandler = viewDismissHandler;
    }

    public void show() {

        mWholeView = (ViewContainer) View.inflate(mContext, R.layout.pop_view, null);

        mContentView = mWholeView.findViewById(R.id.pop_view_content_view);

        // event listeners
        mContentView.setOnClickListener(this);
        mWholeView.setOnTouchListener(this);
//        mWholeView.setKeyEventHandler(this);

        int floatWindowSize = mContext.getResources().getDimensionPixelSize(R.dimen.float_window_size);

        // 设置Window flag
//        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
         * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
         * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
         */

        int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        int type;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            type = WindowManager.LayoutParams.TYPE_TOAST;
//        } else {
//            type = WindowManager.LayoutParams.TYPE_PHONE;
//        }
        type = WindowManager.LayoutParams.TYPE_PHONE;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                , type, flags, PixelFormat.RGBA_8888);
        layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;

        mWindowManager.addView(mWholeView, layoutParams);
    }

    @Override
    public void onClick(View v) {
//        removePoppedViewAndClear();
        if (mOutOnclickListener != null) {
            mOutOnclickListener.onClick(v);
        } else {
            ToastUtils.showToast(mContext, "浮动按钮被点击, 请设置click处理事件");
        }
    }

    public void removePoppedViewAndClear() {

        // remove view
        if (mWindowManager != null && mWholeView != null) {
            mWindowManager.removeView(mWholeView);
        }

        if (mViewDismissHandler != null) {
            mViewDismissHandler.onViewDismiss();
        }

        // remove listeners
        mContentView.setOnClickListener(null);
        mWholeView.setOnTouchListener(null);
        mWholeView.setKeyEventHandler(null);
    }

    /**
     * touch the outside of the content view, remove the popped view
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
//        Rect rect = new Rect();
//        mContentView.getGlobalVisibleRect(rect);
//        if (!rect.contains(x, y)) {
//            removePoppedViewAndClear();
//        }
        return false;
    }

    @Override
    public void onKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            removePoppedViewAndClear();
        }
    }

    public interface ViewDismissHandler {
        void onViewDismiss();
    }
}