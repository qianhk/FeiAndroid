package com.monkeybrowser;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.Button;

public class StateButton extends Button {

	public StateButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
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
}