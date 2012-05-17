package com.monkeybrowser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class URLListActivity extends Activity {
	private String[] mURLName = { "百度", "网易", "新浪", "搜狐", "腾讯", "淘宝", "谷歌",
			"人人网", "优酷", "拉手网" };

	private String[] mURL = { "http://www.baidu.com", "http://www.163.com",
			"http://www.sina.com.cn", "http://www.sohu.com",
			"http://www.qq.com", "http://www.taobao.com",
			"http://www.google.com", "http://www.renren.com",
			"http://www.youku.com", "http://www.lashou.com" };

	private SimpleAdapter mAdapter;
	private List<HashMap<String, Object>> mHashMaps;
	private HashMap<String, Object> map;
	private int mURLCount;

	private ListView mListView;
	private ImageView mGo;
	private EditText mURLEditView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.url_list_layout);

		mURLEditView = (EditText)findViewById(R.id.URLET);
		mGo = (ImageView) findViewById(R.id.go);
		mGo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("url", mURLEditView.getText().toString());
				intent.setClass(URLListActivity.this,
						MonkeyBrowserActivity.class);
				startActivity(intent);
			}
		});
		

		mURLCount = mURL.length;
		mHashMaps = new ArrayList<HashMap<String, Object>>();

		mListView = (ListView) findViewById(R.id.URL_list);
		mAdapter = new SimpleAdapter(this, getData(), R.layout.url_list_item,
				new String[] { "url_name", "url" }, new int[]{R.id.url_name,R.id.url});
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener(){ 
            @Override 
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, 
                    long arg3) { 
                ////////////Debug//////////////////
                @SuppressWarnings("unchecked")
				HashMap<String, Object> map=(HashMap<String, Object>)mListView.getItemAtPosition(arg2); 
                String urlName=(String) map.get("url_name"); 
                String url=(String) map.get("url"); 
                /////////////////////////////////////
                mURLEditView.setText(url);
                mURLEditView.setSelection(url.length());
            } 
             
        }); 
	}

	private List<HashMap<String, Object>> getData() {
		for (int i = 0; i < mURLCount; i++) {
			map = new HashMap<String, Object>();
			map.put("url_name", mURLName[i]);
			map.put("url", mURL[i]);
			mHashMaps.add(map);
		}
		return mHashMaps;
	}
}