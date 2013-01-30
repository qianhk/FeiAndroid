package com.njnu.kai.letterview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class AZSideBar extends View {

	private OnLetterChangedListener mOnLetterChangedListener;
	private static String[] mSegmentChar = {"#", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z"};
	private int mChoose = -1;
	private Paint mPaint = new Paint();
	private int mColorBkg = Color.parseColor("#40000000");
	private int mColorTextHighlight = Color.parseColor("#3399ff");
	private boolean showBkg = false;

	public AZSideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		Resources resource = context.getResources();
		float textSize = 13.3f;
		textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, resource.getDisplayMetrics());
		mPaint.setTextSize(textSize);
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
		mPaint.setTextAlign(Paint.Align.CENTER);
		mPaint.setAntiAlias(true);
	}

	public AZSideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public AZSideBar(Context context) {
		super(context);
		init(context);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (showBkg) {
			canvas.drawColor(mColorBkg);
		}

		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / mSegmentChar.length;
		for (int idx = 0; idx < mSegmentChar.length; ++idx) {
			mPaint.setColor(idx == mChoose ? mColorTextHighlight : Color.BLACK);
			mPaint.setFakeBoldText(idx == mChoose);

			float yPos = singleHeight * idx + singleHeight;
			canvas.drawText(mSegmentChar[idx], width >> 1, yPos, mPaint);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = mChoose;
		final OnLetterChangedListener listener = mOnLetterChangedListener;
		final int c = (int)(y / getHeight() * mSegmentChar.length);
//		Log.d("xx", "s:" + y + " c=" + c + " getHeight()" + getHeight() + " y/height=" + (y / getHeight()));

		switch (action) {
			case MotionEvent.ACTION_DOWN:
				showBkg = true;
				if (oldChoose != c && listener != null) {
					if (c >= 0 && c < mSegmentChar.length) {
						listener.onLetterChanged(mSegmentChar[c]);
						mChoose = c;
						invalidate();
					}
				}

				break;
			case MotionEvent.ACTION_MOVE:
				if (oldChoose != c && listener != null) {
					if (c >= 0 && c < mSegmentChar.length) {
						listener.onLetterChanged(mSegmentChar[c]);
						mChoose = c;
						invalidate();
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				showBkg = false;
				mChoose = -1;
				invalidate();
				break;
		}
		return true;
	}

	/**
	 * @param onLetterChangedListener
	 */
	public void setOnLetterChangedListener(OnLetterChangedListener onLetterChangedListener) {
		mOnLetterChangedListener = onLetterChangedListener;
	}

	/**
	 * �ӿ�
	 *
	 * @author coder
	 */
	public interface OnLetterChangedListener {
		public void onLetterChanged(String s);
	}

}
