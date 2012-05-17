package com.monkeybrowser;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ShakeButton extends LinearLayout {
	private Button mButton;
	private ImageView mCrossIV;
	private Context mContext;

	private Animation animation1;
	private Animation animation2;

	public GridView mContainer;

	public static final int ANIM = 100;
	public static final int NORAML = 101;
	public int mState = NORAML;

	public ShakeButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		LayoutInflater factory = LayoutInflater.from(context);
		factory.inflate(R.layout.shake_button_layout, this);

		mContext = context;		

		mButton = (Button) findViewById(R.id.shake_button);

		mCrossIV = (ImageView) findViewById(R.id.shake_cross);
		mCrossIV.setBackgroundResource(R.drawable.shake_cross);
		mCrossIV.setVisibility(View.INVISIBLE);

		mButton.setOnLongClickListener(new LongListener());
	}

	public ShakeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setContainer(GridView view) {
		mContainer = view;
	}

	public StateListDrawable setBackground(int normalResId, int pressedResId,
			int focusedResId) {

		StateListDrawable bg = new StateListDrawable();

		Drawable normal = getResources().getDrawable(normalResId);
		Drawable selected = getResources().getDrawable(focusedResId);
		Drawable pressed = getResources().getDrawable(pressedResId);

		bg.addState(View.PRESSED_ENABLED_STATE_SET, pressed);
		bg.addState(View.ENABLED_FOCUSED_STATE_SET, selected);
		bg.addState(View.ENABLED_STATE_SET, normal);
		bg.addState(View.FOCUSED_STATE_SET, selected);
		bg.addState(View.EMPTY_STATE_SET, normal);

		return bg;
	}

	public void setButtonBackGroundDrawable(Drawable d) {
		mButton.setBackgroundDrawable(d);
	}

	/*
	 * public void setShakeButtonBackGround(int buttonDrawableID) {
	 * mButton.setBackgroundResource(buttonDrawableID); }
	 */

	public void setShakeCrossBackGround(int crossDrawableID) {
		mCrossIV.setBackgroundResource(crossDrawableID);
	}

	public void setShakeAnimation(int anim1, int anim2) {
		animation1 = AnimationUtils.loadAnimation(mContext, anim1);
		// mButton.setAnimation(animation1);

		animation2 = AnimationUtils.loadAnimation(mContext, anim2);
		// mCrossIV.setAnimation(animation2);
	}

	public void stopShakeAnimation() {
		mButton.clearAnimation();
		mCrossIV.clearAnimation();
		mState = NORAML;
	}

	public void startShakeAnim() {
		if (animation1 != null) {
			mButton.setAnimation(animation1);
			animation1.start();
		}

		if (animation2 != null) {
			mCrossIV.setAnimation(animation2);
			animation2.start();
		}
	}

	public void shakeAllButtons() {
		if (mContainer != null) {
			int count = mContainer.getCount();
			for (int i = 0; i < count; i++) {
				ShakeButton sb = (ShakeButton) (mContainer.getItemAtPosition(i));
				if (sb.mState != ANIM) {
					sb.mState = ANIM;
					sb.startShakeAnim();
					sb.postInvalidate();
				}
			}
		}
	}


	class LongListener implements OnLongClickListener {
		@Override
		public boolean onLongClick(View v) {
			Log.d("button", "onLongClick");
			shakeAllButtons();
		
			return true;
		}
	}

}