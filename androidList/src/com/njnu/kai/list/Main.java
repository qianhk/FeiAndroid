/**
 * @author allin.dev
 * http://allin.cnblogs.com/
 */
package com.njnu.kai.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Main extends Activity {

	private Button myListViewBtn;
	private Button mysimpleCursorAdapterBtn;
	private Button simpleAdapterBtn;
	private Button myAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findViews();
		setLIsteners();
	}

	private void findViews() {
		myListViewBtn = (Button) findViewById(R.id.mylistview_btn);
		mysimpleCursorAdapterBtn = (Button) findViewById(R.id.simpleCursorAdapter_btn);
		simpleAdapterBtn = (Button) findViewById(R.id.simpleAdapter_btn);
		myAdapter = (Button) findViewById(R.id.myAdapter_btn);
	}

	private void setLIsteners() {

		myListViewBtn.setOnClickListener(myListViewBtnListener);
		mysimpleCursorAdapterBtn.setOnClickListener(simpleCursorAdapterBtnListener);
		simpleAdapterBtn.setOnClickListener(simpleAdapterBtnListener);
		myAdapter.setOnClickListener(myAdapterBtnListener);
	}

	private View.OnClickListener myAdapterBtnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.v("MyListView4", "myAdapter");
			Intent intent = new Intent(Main.this, MyListView4.class);
			startActivity(intent);
		}
	};

	private View.OnClickListener simpleAdapterBtnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.v("MyListView3", "simpleAdapter");
			Intent intent = new Intent(Main.this, MyListView3.class);
			startActivity(intent);
		}
	};
	private View.OnClickListener simpleCursorAdapterBtnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.v("MyListView2", "simpleCursorAdapter");
			Intent intent = new Intent(Main.this, MyListView2.class);
			startActivity(intent);
		}
	};

	private View.OnClickListener myListViewBtnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.v("MyListView", "myListViewBtnListener");

			// Intent intent = new Intent("android.intent.action.mylistview");
			Intent intent = new Intent(Main.this, MyListView.class);

			startActivity(intent);

		}

	};
}
