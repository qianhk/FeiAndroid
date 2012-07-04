package com.monkeybrowser;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.LevelListDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ToolbarMenu extends LinearLayout {
	private Activity mActivity;
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;

	private ImageView mcursor;
	private GridView mMenuContentGridView;

	private MenuItemData mMenuItemData;
	private MenuAdapter mAadapter;

	private int offset = 0;
	private int bmpW;
	private int mIndex = 0;
	private int one;
	private int two;

	public ToolbarMenu(Context context) {
		super(context);

		LayoutInflater factory = LayoutInflater.from(context);
		factory.inflate(R.layout.popmenu, this);
	}

	public void init(Activity activity) {
		mActivity = activity;
		tv1 = (TextView) findViewById(R.id.text1);
		tv2 = (TextView) findViewById(R.id.text2);
		tv3 = (TextView) findViewById(R.id.text3);

		tv1.setOnClickListener(new TitleOnClickListener(0));
		tv2.setOnClickListener(new TitleOnClickListener(1));
		tv3.setOnClickListener(new TitleOnClickListener(2));

		mMenuContentGridView = (GridView) findViewById(R.id.menuGridContent);

		String[] menu_name_array2 = getResources().getStringArray(
				R.array.menu_item_name_array2);
		LevelListDrawable levelListDrawable2 = (LevelListDrawable) getResources()
				.getDrawable(R.drawable.menu_image_list2);
		mMenuItemData = new MenuItemData(levelListDrawable2, menu_name_array2,
				menu_name_array2.length);

		mAadapter = new MenuAdapter(activity, mMenuItemData);
		mMenuContentGridView.setAdapter(mAadapter);

		InitImageView();
	}

	private void InitImageView() {
		mcursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 3 - bmpW) / 2;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		mcursor.setImageMatrix(matrix);

		one = offset * 2 + bmpW;
		two = one * 2;
	}

	public class TitleOnClickListener implements View.OnClickListener {
		private int index = 0;

		public TitleOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			// mPager.setCurrentItem(index);
			onTitleClick(index);
		}
	};

	public void onTitleClick(int i) {
		Animation animation = null;

		switch (i) {
		case 0:
			if (mIndex == 1) {
				animation = new TranslateAnimation(one, 0, 0, 0);
			} else if (mIndex == 2) {
				animation = new TranslateAnimation(two, 0, 0, 0);
			}
			break;
		case 1:
			if (mIndex == 0) {
				animation = new TranslateAnimation(offset, one, 0, 0);
			} else if (mIndex == 2) {
				animation = new TranslateAnimation(two, one, 0, 0);
			}
			break;
		case 2:
			if (mIndex == 0) {
				animation = new TranslateAnimation(offset, two, 0, 0);
			} else if (mIndex == 1) {
				animation = new TranslateAnimation(one, two, 0, 0);
			}
			break;
		}
		mIndex = i;
		if (animation != null) {
			animation.setFillAfter(true);
			animation.setDuration(100);
			mcursor.startAnimation(animation);
		}

	}

}
