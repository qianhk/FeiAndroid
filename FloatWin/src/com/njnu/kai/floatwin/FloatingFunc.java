package com.njnu.kai.floatwin;

/**
 * Date: 12-12-28
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.*;
import android.view.WindowManager.LayoutParams;


public class FloatingFunc {

	private static float x = 0;

	private static float y = 200;

	private static float state = 0;

	private static float mTouchStartX = 0;

	private static float mTouchStartY = 0;

	private static WindowManager wm = null;


	private static View floatingViewObj = null;

	public static WindowManager.LayoutParams params = new WindowManager.LayoutParams();

	private static View view_obj = null;


	public static void show(Context context, View floatingViewObj) {

		close(context);
		FloatingFunc.floatingViewObj = floatingViewObj;

		view_obj = floatingViewObj;

		wm = (WindowManager) context.getSystemService(Activity.WINDOW_SERVICE);
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;

		// 设置悬浮窗口长宽数据
		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.WRAP_CONTENT;
		// 设定透明度
		params.alpha = 80;
		// 设定内部文字对齐方式
		params.gravity = Gravity.LEFT | Gravity.TOP;

		// 以屏幕左上角为原点，设置x、y初始值ֵ
		params.x = (int) x;
		params.y = (int) y;
		// tv = new MyTextView(TopFrame.this);
		wm.addView(floatingViewObj, params);

	}

	/**
	 * 跟谁滑动移动
	 *
	 * @param event
	 *            事件对象
	 * @param view
	 *            弹出对象实例（View）
	 * @return
	 */
	public static boolean onTouchEvent(MotionEvent event, View view) {

		// 获取相对屏幕的坐标，即以屏幕左上角为原点
		x = event.getRawX();
		y = event.getRawY() - 25; // 25是系统状态栏的高度
//		Log.i("currP", "currX" + x + "====currY" + y);// 调试信息
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				state = MotionEvent.ACTION_DOWN;
				// panTime();
				// 获取相对View的坐标，即以此View左上角为原点
				mTouchStartX = event.getX();
				mTouchStartY = event.getY();
				Log.i("startP", "startX" + mTouchStartX + "====startY"
						+ mTouchStartY);// 调试信息

				break;
			case MotionEvent.ACTION_MOVE:
				state = MotionEvent.ACTION_MOVE;
				updateViewPosition(view);
				break;

			case MotionEvent.ACTION_UP:
				state = MotionEvent.ACTION_UP;
				updateViewPosition(view);
				mTouchStartX = mTouchStartY = 0;
				break;
		}
		return true;
	}

	/**
	 * 关闭浮动显示对象
	 */
	public static void close(Context context) {

		if (view_obj != null && view_obj.isShown()) {
			WindowManager wm = (WindowManager) context.getSystemService(Activity.WINDOW_SERVICE);
			wm.removeView(view_obj);
		}
	}

	/**
	 * 更新弹出窗口位置
	 */
	private static void updateViewPosition(View view) {
		// 更新浮动窗口位置参数
		params.x = (int) (x - mTouchStartX);
		params.y = (int) (y - mTouchStartY);
		wm.updateViewLayout(FloatingFunc.floatingViewObj, params);
	}

}