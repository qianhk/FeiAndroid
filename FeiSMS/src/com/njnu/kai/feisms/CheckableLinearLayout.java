package com.njnu.kai.feisms;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {
	private static final String PREFIX = "[CheckableLinearLayout]:";
	private boolean mChecked = false;
	private List<CheckedTextView> mListCheckedTextView = new ArrayList<CheckedTextView>();

	public CheckableLinearLayout(Context context) {
		super(context);

	}

	public CheckableLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public CheckableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	private void putSubCheckedTextViewToList(View parent) {
		if (parent instanceof CheckedTextView) {
			mListCheckedTextView.add((CheckedTextView) parent);
		} else if (parent instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) parent;
			int childCount = vg.getChildCount();
			for (int i = 0; i < childCount; ++i) {
				View v = vg.getChildAt(i);
				putSubCheckedTextViewToList(v);
			}
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		putSubCheckedTextViewToList(this);
		Log.i(PREFIX, "when onFinishInflate, all CheckedTextView amout=" + mListCheckedTextView.size());
	}

	@Override
	public void setChecked(boolean checked) {
		if (mChecked != checked) {
			mChecked = checked;
			for (CheckedTextView v : mListCheckedTextView) {
				v.setChecked(mChecked);
			}
		}
	}

	@Override
	public boolean isChecked() {
		return mChecked;
	}

	@Override
	public void toggle() {
		setChecked(!mChecked);
	}

}
