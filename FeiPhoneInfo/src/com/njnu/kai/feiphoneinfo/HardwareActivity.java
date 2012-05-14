package com.njnu.kai.feiphoneinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class HardwareActivity extends Activity implements OnItemClickListener {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ListView lv = new ListView(this);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.list_item_number_two_text,
				new String[] { "title", "info", "no" }, new int[] { R.id.text1, R.id.text2, R.id.orderno });
		lv.setAdapter(adapter);
		setContentView(lv);
		lv.setOnItemClickListener(this);
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "G1");
		map.put("info", "google 1");
		map.put("no", 1);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "G2");
		map.put("info", "google 2");
		map.put("no", 2);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "G3");
		map.put("info", "google 3");
		map.put("no", 3);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "G4");
		map.put("info", "google 4");
		map.put("no", 4);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "G5");
		map.put("info", "google 5");
		map.put("no", 5);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "G6");
		map.put("info", "google 6");
		map.put("no", 6);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "G7");
		map.put("info", "google 7");
		map.put("no", 7);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "G8");
		map.put("info", "google 8");
		map.put("no", 8);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "G9");
		map.put("info", "google 9");
		map.put("no", 9);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "G10");
		map.put("info", "google 10");
		map.put("no", 10);
		list.add(map);

		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		Log.v("Hardware", "Clicked " + id + " " + ((TextView) view).getText() +" pos=" + position);
		Log.v("Hardware", "Clicked " + id + " " + view +" pos=" + position);

	}
}
