package me.longerian.demo.singlechoice.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;
import me.longerian.demo.singlechoice.R;

public class CheckableLinearLayoutWithoutCheckedTextView extends LinearLayout implements Checkable {

	private boolean mChecked;
    
    public CheckableLinearLayoutWithoutCheckedTextView(Context context, AttributeSet attrs) {
    	super(context, attrs);
    	setChecked(false);
    }
    
	@Override
	public boolean isChecked() {
		return mChecked;
	}

	@Override
	public void setChecked(boolean checked) {
		if (mChecked != checked) {
            mChecked = checked;
            if(checked) {
            	setBackgroundResource(R.drawable.item_checked);
            } else {
            	setBackgroundResource(R.drawable.item_normal);
            }
            refreshDrawableState();
        }
	}

	@Override
	public void toggle() {
		setChecked(!mChecked);
	}

}
