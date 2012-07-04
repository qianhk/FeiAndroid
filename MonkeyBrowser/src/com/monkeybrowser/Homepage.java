package com.monkeybrowser;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.LinearLayout;

public class Homepage extends LinearLayout {
	GridView mHomepageGridView;
	HomepageBtnAdapter mBtnAdapter;
	private Context mContext;

	int[] mHpIcon = { R.drawable.apple, R.drawable.baidu, R.drawable.facebook,
			R.drawable.google, R.drawable.qq, R.drawable.yahoo,
			R.drawable.youtube };

	int[] mHpPressIcon = { R.drawable.applepress, R.drawable.baidupress,
			R.drawable.facebookpress, R.drawable.googlepress,
			R.drawable.qqpress, R.drawable.yahoopress, R.drawable.youtubepress };
	
	ArrayList<ShakeButton> mShakeButtons = new ArrayList<ShakeButton>();

	public Homepage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		// LayoutInflater inflater =
		// (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflater.inflate(R.layout.homepage, null);
		mContext = context;
		
		this.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		LayoutInflater factory = LayoutInflater.from(context);
		factory.inflate(R.layout.homepage_layout, this);

		mHomepageGridView = (GridView) findViewById(R.id.homepage);
		
		initShakeButtons();

		mBtnAdapter = new HomepageBtnAdapter(context, mShakeButtons);
		mBtnAdapter.setHpDefaultIcon(mHpIcon);
		mBtnAdapter.setHpPressIcon(mHpPressIcon);

		mHomepageGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mHomepageGridView.setAdapter(mBtnAdapter);
		
		//Log.d("Homepage",String.valueOf(mHomepageGridView.getCount()));

	}
	
	public void initShakeButtons(){
		int count = mHpIcon.length;
		
		for(int i = 0; i< count; i++){
			ShakeButton button = new ShakeButton(mContext);
			button.setContainer(mHomepageGridView);
			button.setLayoutParams(new GridView.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			
			mShakeButtons.add(button);
		}
	}
	
	public void stopShakeing(){
		int count = mHomepageGridView.getCount();
		
		if(mHomepageGridView != null && count != 0){			
			for (int i = 0; i < count; i++) {
				ShakeButton sb = (ShakeButton) (mHomepageGridView.getItemAtPosition(i));
				if (sb.mState == ShakeButton.ANIM) {					
					sb.stopShakeAnimation();
					sb.mState = ShakeButton.NORAML;
					sb.postInvalidate();
				}
			}
		}
	}

	public GridView getHomePageGridView() {
		if (mHomepageGridView != null)
			return mHomepageGridView;
		return null;
	}

}
