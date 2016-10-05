package com.njnu.kai.mockclick.win;

/**
 * @author kai
 * @since 16/10/4
 */

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.njnu.kai.mockclick.R;
import com.njnu.kai.mockclick.util.DisplayUtils;
import com.njnu.kai.mockclick.util.LogUtils;
import com.njnu.kai.mockclick.util.ToastUtils;

public final class FloatViewController implements View.OnClickListener, View.OnTouchListener, ViewContainer.KeyEventHandler {

    private static final String TAG = "FloatViewController";
    private WindowManager mWindowManager;
    private Context mContext;
    private ViewContainer mWholeView;
    private View mContentView;
    private ViewDismissHandler mViewDismissHandler;

    private View.OnClickListener mOutOnclickListener;
    private WindowManager.LayoutParams mFloatLayoutParams;

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
//        mContentView.setOnClickListener(this);
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

        mFloatLayoutParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                , type, flags, PixelFormat.RGBA_8888);
//        mFloatLayoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        mFloatLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mFloatLayoutParams.x = DisplayUtils.getWidthPixels() - floatWindowSize;
        mFloatLayoutParams.y = DisplayUtils.getHeightPixels() / 2;

        mWindowManager.addView(mWholeView, mFloatLayoutParams);
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

    private int mTouchStartX;
    private int mTouchStartY;

    private int mRawX;
    private int mRawY;

    /**
     * touch the outside of the content view, remove the popped view
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

//        int x = event.getX();
//        int y = event.getY();
//        Rect rect = new Rect();
//        mContentView.getGlobalVisibleRect(rect);
//        if (!rect.contains(x, y)) {
//            removePoppedViewAndClear();
//        }

        mRawX = (int) event.getRawX();
        mRawY = (int) event.getRawY();
        LogUtils.e(TAG, "onTouch rawX=%d rawY=%d action=%d", mRawX, mRawY, event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:    //捕获手指触摸按下动作
                //获取相对View的坐标，即以此View左上角为原点
                mTouchStartX = (int) event.getX();
                mTouchStartY = (int) (event.getY() + DisplayUtils.getStatusBarHeight());
//                Log.i("startP", "startX" + mTouchStartX + "====startY" + mTouchStartY);
                break;

            case MotionEvent.ACTION_MOVE:   //捕获手指触摸移动动作
                updateViewPosition();
                break;

            case MotionEvent.ACTION_UP:    //捕获手指触摸离开动作
                updateViewPosition();
                mTouchStartX = mTouchStartY = 0;
                break;
        }
        return false;
    }

    private void updateViewPosition() {
        //更新浮动窗口位置参数
        mFloatLayoutParams.x = mRawX - mTouchStartX;
        mFloatLayoutParams.y = mRawY - mTouchStartY;
        mWindowManager.updateViewLayout(mWholeView, mFloatLayoutParams);
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