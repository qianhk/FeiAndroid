package me.longerian.demo.singlechoice;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CustomUICheckMarkDemo2 extends Activity {

//	public static final String[] items = new String[] {
//		"Android", "iPhone", "WindowPhone", "Blackberry", "Symbian", "WebOS", "MeeGo", "Bada"
//	};
//	public static final String[] items2 = new String[] {
//		"iPhone", "WindowPhone", "Blackberry", "Symbian", "WebOS", "MeeGo", "Bada"
//	};
	
private List<String> mListString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);
        mListString = new ArrayList<String>(5);
        mListString.add("Android");
        mListString.add("iPhone");
        mListString.add("WP7");
        mListString.add("Blackberry");
        mListString.add("Symbian");
        final ListView customUIListView = (ListView) findViewById(R.id.listview);
        customUIListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        customUIListView.setAdapter(new ArrayAdapter<String>(
        		getApplicationContext(),  R.layout.custom_ui_check_mark_item2, mListString));
        customUIListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				Toast.makeText(getApplicationContext(), items[customUIListView.getCheckedItemPosition()], Toast.LENGTH_SHORT).show();
			}
		});
    }

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		final ListView customUIListView = (ListView) findViewById(R.id.listview);
		ArrayAdapter ada = (ArrayAdapter)customUIListView.getAdapter();
		ada.remove("Android");
//		ada.add("XXXX");
//		ada.notifyDataSetChanged();
//		customUIListView.setAdapter(new ArrayAdapter<String>(
//        		getApplicationContext(),  R.layout.custom_ui_check_mark_item2, items2));
		return super.onMenuItemSelected(featureId, item);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Flush");
		return super.onCreateOptionsMenu(menu);
	}
    
}
