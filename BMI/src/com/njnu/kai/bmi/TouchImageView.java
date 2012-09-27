package com.njnu.kai.bmi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class TouchImageView extends ImageView {
	private static final String PREFIX = "TouchImageView";
	Paint mPaintCyan;
	Paint mPaintYellow;
	Paint mPaintMagenta;
	Paint mPaintGray;
	Rect mRect;
	RectF mRectF;

	public TouchImageView(Context context) {
		super(context);
		Log.i(PREFIX, "TouchImageView construct 1");
		init();
		makeGestureDetector();
	}

	public TouchImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.i(PREFIX, "TouchImageView construct 2");
		init();
		makeGestureDetector();
	}

	public TouchImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i(PREFIX, "TouchImageView construct 3");
		init();
		makeGestureDetector();
	}

	private void init() {
		mRect = new Rect();
		mRectF = new RectF();

		mPaintGray = new Paint();
		mPaintGray.setColor(Color.GRAY);
		mPaintGray.setStyle(Paint.Style.STROKE);
//		mPaintGray.setStrokeCap(Paint.Cap.BUTT);
//		mPaintGray.setStrokeJoin(Paint.Join.ROUND);
//		mPaintGray.setStrokeMiter(2);
		mPaintGray.setStrokeWidth(2);

		mPaintCyan = new Paint();
		mPaintCyan.setColor(Color.CYAN);

		mPaintYellow = new Paint();
		mPaintYellow.setColor(Color.YELLOW);

		mPaintMagenta = new Paint();
		mPaintMagenta.setColor(Color.MAGENTA);
	}

	private void makeGestureDetector() {

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int width = getWidth();
		int height = getHeight();
		int rectSizeX = 16;
		int rectSizeY = 16;

		mRectF.set(0, 0, width, height);
		canvas.drawRoundRect(mRectF, 5, 5, mPaintGray);

		mRect.set(10, 10, 10 + rectSizeX, 10 + rectSizeY);
		canvas.drawRect(mRect, mPaintCyan);

		mRect.set(width - 10 - rectSizeX, (height - rectSizeY) / 2, width - 10, (height + rectSizeY) / 2);
		canvas.drawRect(mRect, mPaintYellow);

		mRect.set((width - rectSizeX) / 2, height - 10 - rectSizeY, (width + rectSizeX) / 2, height - 10);
		canvas.drawRect(mRect, mPaintMagenta);

		FontMetrics fm = mPaintMagenta.getFontMetrics();
		canvas.drawText("abg好好学习", 0, height - fm.descent, mPaintMagenta);
		canvas.drawText("abg好好学习", 0, 0, mPaintMagenta);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

}
