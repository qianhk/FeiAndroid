package com.njnu.kai.bmi;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BMIActivity extends Activity implements OnClickListener {

	private ImageView mIvTestLayer1;
	private ImageView mIvTestLayer2;

	public double calcBMI(double weight, double height) {
    	double BMI = weight / (height * height);
    	return BMI;
    }

    private Button.OnClickListener calcBMI = new OnClickListener()
    {
		@Override
		public void onClick(View v) {
			DecimalFormat nf = new DecimalFormat("0.00");
			try{
			EditText fieldHeight = (EditText)findViewById(R.id.stature);
			EditText fieldWeight = (EditText)findViewById(R.id.weight);
			double height = Double.parseDouble(fieldHeight.getText().toString()) / 100;
			double weight = Double.parseDouble(fieldWeight.getText().toString());
			double BMI = calcBMI(weight, height);

			TextView result = (TextView)findViewById(R.id.result);
			result.setText("Your BMI is " + nf.format(BMI));
			TextView fieldSuggest = (TextView)findViewById(R.id.suggest);
			if (BMI > 25){
				fieldSuggest.setText(R.string.advice_heavy);
			} else if(BMI < 20){
				fieldSuggest.setText(R.string.advice_light);
			}
			else{
				fieldSuggest.setText(R.string.advice_average);
			}
			}
			catch(Exception err)
			{
				Toast.makeText(BMIActivity.this, "error", Toast.LENGTH_SHORT).show();
			}
//			openOptionDialog();
		}
    };

    private void openOptionDialog(){
//    	Toast.makeText(BMIActivity.this, "haha", Toast.LENGTH_SHORT).show();
    	new AlertDialog.Builder(BMIActivity.this).setTitle(R.string.about_title).setMessage(R.string.about_msg)
    	.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				Uri uri = Uri.parse("http://wap.google.com");
//				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//				startActivity(intent);
			}
		})
    	.show();
    }

    protected static final int MENU_ABOUT = Menu.FIRST;
    protected static final int MENU_Quit = Menu.FIRST + 1;
	private Drawable mDrawableLauncher2;
	private Drawable mDrawableApple;
	private Drawable mDrawableLauncher1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(0, MENU_ABOUT, 0, "About...");
    	menu.add(0, MENU_Quit, 0, "Exit");
    	return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
    	super.onOptionsItemSelected(item);
    	switch (item.getItemId()){
    	case MENU_ABOUT:
    		openOptionDialog();
    		break;

    	case MENU_Quit:
    		finish();
    		break;
    	}

    	return true;
    }

    public void clickTestLayer1(View v) {
    	LayerDrawable d = (LayerDrawable)mIvTestLayer2.getDrawable();
    	d.setDrawableByLayerId(1, mDrawableLauncher2);
    	d.invalidateSelf();
    	mIvTestLayer2.invalidate();

    	d = (LayerDrawable)mIvTestLayer1.getDrawable();
    	d.setDrawableByLayerId(1, mDrawableLauncher1);
    	d.invalidateSelf();
    	mIvTestLayer1.invalidate();

    	setSettingPanelColorButtonFlag(2);
    }

    public void clickTestLayer2(View v) {
//    	mIvTestLayer2.setImageDrawable(mIvTestLayer1.getDrawable().mutate());
    	LayerDrawable d = (LayerDrawable)mIvTestLayer2.getDrawable();
    	d.setDrawableByLayerId(1, mDrawableLauncher1);
    	d.invalidateSelf();
    	mIvTestLayer2.invalidate();

    	d = (LayerDrawable)mIvTestLayer1.getDrawable();
    	d.setDrawableByLayerId(1, mDrawableLauncher2);
    	d.invalidateSelf();
    	mIvTestLayer1.invalidate();

    	setSettingPanelColorButtonFlag(3);
    }

	private View[] mArrayColorButton;
	private ColorButtonManager mColorButtonMgr;
	private Drawable mDrawableFlag;
	private Drawable mDrawableTransparent;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button button = (Button)findViewById(R.id.submit);
        button.setOnClickListener(calcBMI);

        mIvTestLayer1 = (ImageView)findViewById(R.id.iv_testlayer1);
        mIvTestLayer2 = (ImageView)findViewById(R.id.iv_testlayer2);
        mDrawableApple = getResources().getDrawable(R.drawable.minilyric_btn_color_blue);
		mDrawableLauncher2 = getResources().getDrawable(R.drawable.ic_launcher2);
		mDrawableLauncher1 = getResources().getDrawable(R.drawable.ic_launcher);
		LayerDrawable layerdrawable = new LayerDrawable(new Drawable[] {mDrawableApple, mDrawableLauncher2});
        layerdrawable.setId(1, 1);
        mIvTestLayer1.setImageDrawable(layerdrawable);

        layerdrawable = new LayerDrawable(new Drawable[] {mDrawableApple, mDrawableLauncher2});
        layerdrawable.setId(1, 1);
		mIvTestLayer2.setImageDrawable(layerdrawable);

		initColorButton();
    }

	private class ColorButtonManager {
		private ArrayList<ColorButton> mList;

		private class ColorButton {
			private ImageView mImageButton;
			private Drawable mImage;
			private LayerDrawable mDrawable;
			private int mId;
//			private int mRed[] = {R.drawable.minilyric_icon_setting, R.drawable.minilyric_icon_fontzoomin, R.drawable.minilyric_icon_fontzoomout
//					, R.drawable.minilyric_return_main_panel_normal, R.drawable.minilyric_unlocked};

			public ColorButton(View button, Drawable image, int id) {
				mId = id;
				mImageButton = (ImageView)button;
				mImage = image;
				mDrawable = new LayerDrawable(new Drawable[] {mImage, mDrawableTransparent});
				mDrawable.setId(1, 1);
				mImageButton.setImageDrawable(mDrawable);
			}

			public void setFlag(boolean flag) {
				Drawable needDrawable = flag ? mDrawableFlag : mDrawableTransparent;
//				final Drawable drawableTmp = mContext.getResources().getDrawable(mRed[mId]);
//				++mId;
//				if (mId >= 5) {
//					mId = 0;
//				}
//				if (needDrawable != mDrawable.getDrawable(1)) {
//					mDrawable.setDrawableByLayerId(1, needDrawable);
//				}
				mDrawable.setDrawableByLayerId(1, needDrawable);
				mDrawable.invalidateSelf();
//				mImageButton.invalidate();
			}
		}

		void appendColorButton(View button, Drawable image) {
			ColorButton colorButton = new ColorButton(button, image, mList.size());
			mList.add(colorButton);
		}

		void setChoosedButton(int index) {
			for (int idx = mList.size() - 1; idx >= 0; --idx) {
				ColorButton btn = mList.get(idx);
				btn.setFlag(idx == index);
			}
		}

		public ColorButtonManager() {
			mList = new ArrayList<ColorButtonManager.ColorButton>(mArrayColorButton.length);

		}
	}

	private void initColorButton() {
		final Resources resources = this.getResources();
		final int colorButtonAmount = 5;
		mArrayColorButton = new View[colorButtonAmount];
		mColorButtonMgr = new ColorButtonManager();
		mDrawableFlag = resources.getDrawable(R.drawable.minilyric_choosed_color);
		mDrawableTransparent = resources.getDrawable(R.drawable.minilyric_icon_lock);

		setColorButtonPropety(0, R.id.iv_color_blue, R.drawable.minilyric_btn_color_blue);
		setColorButtonPropety(1, R.id.iv_color_yellow, R.drawable.minilyric_btn_color_yellow);
		setColorButtonPropety(2, R.id.iv_color_pink, R.drawable.minilyric_btn_color_pink);
		setColorButtonPropety(3, R.id.iv_color_gray, R.drawable.minilyric_btn_color_gray);
		setColorButtonPropety(4, R.id.iv_color_green, R.drawable.minilyric_btn_color_green);

//		int colorStyle = 2;
//		setSettingPanelColorButtonFlag(colorStyle);
	}

	private void setColorButtonPropety(int index, int buttonId, int imageResId) {
		View button = findViewById(buttonId);
		final Resources resources = getResources();
		button.setOnClickListener(this);
		Drawable drawable = resources.getDrawable(imageResId);
		mArrayColorButton[index] = button;
		mColorButtonMgr.appendColorButton(button, drawable);
	}

	private void setSettingPanelColorButtonFlag(int aColorStyle) {
		mColorButtonMgr.setChoosedButton(aColorStyle);
	}

	@Override
	public void onClick(View view) {
		for (int idx = mArrayColorButton.length - 1; idx >= 0; --idx) {
			if (view.equals(mArrayColorButton[idx])) {
				setSettingPanelColorButtonFlag(idx);
				break;
			}
		}
	}
}
