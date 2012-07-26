package me.longerian.demo.singlechoice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CustomUICheckMarkDemo extends Activity {

	public static final String[] items = new String[] {
		"Android", "iPhone", "WindowPhone", "Blackberry", "Symbian", "WebOS", "MeeGo", "Bada"
	};
	
    /**
     * <CheckedTextView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@android:id/text1"
    android:layout_width="match_parent"
    android:layout_height="?android:attr/listPreferredItemHeight"
    android:textAppearance="?android:attr/textAppearanceLarge"
    android:gravity="center_vertical"
    android:checkMark="@drawable/check_selector"
    android:paddingLeft="6dip"
    android:paddingRight="6dip"
/>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);
        final ListView customUIListView = (ListView) findViewById(R.id.listview);
        customUIListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        customUIListView.setAdapter(new ArrayAdapter<String>(
        		getApplicationContext(),  R.layout.custom_ui_check_mark_item, items));
        customUIListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(getApplicationContext(), items[customUIListView.getCheckedItemPosition()], Toast.LENGTH_SHORT).show();
			}
		});
    }
    
}
