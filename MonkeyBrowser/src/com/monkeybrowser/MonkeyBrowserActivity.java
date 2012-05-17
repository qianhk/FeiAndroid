package com.monkeybrowser;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MonkeyBrowserActivity extends Activity {
	// /Toolbar
	private ToolBarItemData mToolBarItemData;
	private GridView mToolBarGrid;
	private ToolBarAdapter mToolBarAdapter;
	// /Popmenu
	private PopupWindow mPopupWindow;
	// screen2
	// /////main Layout screen2
	private RelativeLayout mScreen2;
	private ViewPager mPager2;
	private List<View> mlistViews2;

	private ImageView mPageCountIv;
	// ////Address layout
	private TextView mAddressTV;
	// screen1
	// /////main Layout screen1
	private RelativeLayout mScreen1;
	private ImageView mCloseWebViewIV;
	// URL
	private String mURL;
	// WebView
	private WebView mWebViewMain;
	// Progress Bar
	private ProgressBar mProgressBar;
	private ImageView mFavor_icon;
	private TextView mFavor_title;
	private LinearLayout mPage_title;
	private Bitmap mFavorIcon;
	private String mFavorTitle;
	private boolean mUpdatePageTitle = false;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		init();
	}

	public void init() {
		initPageCountIv();// must first,because MyOnPageChangeListener will use
							// mPageCountIv.
		initViewPager();
		initToolbar();
		initWidgets();
		initPopMenu();
		initSetting();
	}

	public void initSetting(){
		WebIconDatabase.getInstance ().open(getDir( "icons" , MODE_PRIVATE ).getPath());
	}

	public void initPageCountIv() {
		mPageCountIv = (ImageView) findViewById(R.id.pagecount);
	}

	// ///////////////////popmenu/////////////////////
	private void initPopMenu() {
		ToolbarMenu toolbarmenu = new ToolbarMenu(this);
		toolbarmenu.init(this);
		mPopupWindow = new PopupWindow(toolbarmenu, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(true);

		ColorDrawable dw = new ColorDrawable(0x00000000);
		mPopupWindow.setBackgroundDrawable(dw);
		mPopupWindow.setAnimationStyle(android.R.style.Animation_Toast);
	}

	private void showMenuWindow() {
		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		} else {
			mPopupWindow.showAtLocation(findViewById(R.id.root), Gravity.TOP,
					0, 0);

		}
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		showMenuWindow();
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add("menu");
		return super.onCreateOptionsMenu(menu);

	}

	// ///////////////////popmenu/////////////////////

	private void initToolbar() {
		String[] toolbar_name_array = getResources().getStringArray(
				R.array.toolbar_name_array);
		LevelListDrawable levelListDrawable = (LevelListDrawable) getResources()
				.getDrawable(R.drawable.toolbar_image_list);
		mToolBarItemData = new ToolBarItemData(levelListDrawable,
				toolbar_name_array, toolbar_name_array.length);

		mToolBarGrid = (GridView) findViewById(R.id.GridView_toolbar);
		mToolBarAdapter = new ToolBarAdapter(this, mToolBarItemData);
		mToolBarGrid.setAdapter(mToolBarAdapter);
		mToolBarGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					mWebViewMain.goBack();
					break;
				case 1:
					mWebViewMain.goForward();
					break;
				}
			}
		});

		// mToolBarGrid.setSelection(-1);
	}

	public void initViewPager() {
		// screen two
		mPager2 = (ViewPager) findViewById(R.id.vPager);
		mlistViews2 = new ArrayList<View>();

		mlistViews2.add(new Leftpage(getApplicationContext()));
		mlistViews2.add(new Homepage(getApplicationContext()));
		mlistViews2.add(new Rightpage(getApplicationContext()));

		mPager2.setAdapter(new ViewPagerAdapter(mlistViews2));
		mPager2.setOnPageChangeListener(new MyOnPageChangeListener());
		mPager2.setCurrentItem(1);
		// screen one
		// Initialize WebView
		initWebViews();
	}

	public void initWidgets() {
		mScreen1 = (RelativeLayout) findViewById(R.id.screen1);// progress
		mScreen2 = (RelativeLayout) findViewById(R.id.screen2);// address

		mAddressTV = (TextView) findViewById(R.id.title);
		mAddressTV.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d("Monkey", "onClick");
				/*
				 * mScreen2.setVisibility(View.INVISIBLE);
				 * mScreen1.setVisibility(View.VISIBLE);
				 */
				Intent intent = new Intent();
				intent.setClass(MonkeyBrowserActivity.this,
						URLListActivity.class);
				startActivity(intent);
			}
		});

		mCloseWebViewIV = (ImageView) findViewById(R.id.closeWebView);

		mCloseWebViewIV.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mScreen2.setVisibility(View.VISIBLE);
				mScreen1.setVisibility(View.INVISIBLE);
			}
		});

	}

	private void initWebViews() {
		mWebViewMain = (WebView) findViewById(R.id.webviewmain);
		mFavor_icon = (ImageView)findViewById(R.id.favor_icon);
		mPage_title = (LinearLayout)findViewById(R.id.page_title);
		mFavor_title = (TextView)findViewById(R.id.favor_title);

		ProgressBarLayout progressBarLayout = (ProgressBarLayout) findViewById(R.id.progressbar);
		mProgressBar = (ProgressBar) progressBarLayout
				.findViewById(R.id.progress);

		mWebViewMain.getSettings().setJavaScriptEnabled(true);
		mWebViewMain.setVerticalScrollBarEnabled(true);
		mWebViewMain.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
		mWebViewMain.getSettings().setCacheMode(
				WebSettings.LOAD_CACHE_ELSE_NETWORK);

		mWebViewMain.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.d("monkey", url);

				view.loadUrl(url);
				return true;
			}
			public void onPageStarted(WebView view, String url, Bitmap favicon){
				mProgressBar.setVisibility(View.VISIBLE);
				mUpdatePageTitle = false;
				mPage_title.setVisibility(View.INVISIBLE);
			}

			public void onPageFinished(WebView view, String url) {
				mProgressBar.setVisibility(View.INVISIBLE);
			}
		});

		mWebViewMain.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int newProgress) {
				mProgressBar.setProgress(newProgress);
			}

			public void onReceivedIcon(WebView view, Bitmap icon){
				mPage_title.setVisibility(View.VISIBLE);
				mFavor_icon.setImageBitmap(icon);
			}

			public void onReceivedTitle(WebView view, String title){
				//mPage_title.setVisibility(View.VISIBLE);
				mFavor_title.setText(title);
			}
		});

		// mWebViewMain.requestFocus();
	}


	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		processExtraData();
	}

	private void processExtraData() {
		Intent intent = getIntent();
		mURL = intent.getStringExtra("url");

		mScreen2.setVisibility(View.INVISIBLE);
		mScreen1.setVisibility(View.VISIBLE);

		if (mURL.length() != 0) {
			// mWebViewMain .setBackgroundResource(R.drawable.google);
			// mWebViewMain.setBackgroundColor(Color.argb(0, 0, 0, 0));
			mWebViewMain.requestFocus();
			mWebViewMain.loadUrl(mURL);
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			mPageCountIv.setImageLevel(arg0);

		}
	}

	public class ViewPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public ViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mListViews.get(position));
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch(keyCode)
        {
        case KeyEvent.KEYCODE_BACK:
            ((Homepage)mlistViews2.get(1)).stopShakeing();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
