package com.njnu.kai.feisms;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {
	private static final String PREFIX = "[CheckableLinearLayout]:";
	private boolean mChecked;
	private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

	public CheckableLinearLayout(Context context) {
		super(context);

	}

	public CheckableLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public CheckableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	public void setChecked(boolean checked) {
		if (mChecked != checked) {
			mChecked = checked;
			refreshDrawableState();
		}
//		Log.i(PREFIX, "setChecked " + checked);
	}

	@Override
	public boolean isChecked() {
		return mChecked;
	}

	@Override
	public void toggle() {
		setChecked(!mChecked);
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		if (isChecked()) {
//			Log.i(PREFIX, "drawableStateChanged");
//            mCheckMarkDrawable.setState(myDrawableState);
			this.getBackground().setState(CHECKED_STATE_SET);
			invalidate();
		}
	}

}
