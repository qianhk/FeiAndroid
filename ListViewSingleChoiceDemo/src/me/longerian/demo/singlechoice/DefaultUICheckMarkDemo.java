package me.longerian.demo.singlechoice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DefaultUICheckMarkDemo extends Activity {
	
	public static final String[] items = new String[] {
		"Android", "iPhone", "WindowPhone", "Blackberry", "Symbian", "WebOS", "MeeGo", "Bada"
	};
	
    /** 
     * android.R.layout.simple_list_item_single_choice 源码
     <CheckedTextView xmlns:android="http://schemas.android.com/apk/res/android"
	    android:id="@android:id/text1"
	    android:layout_width="match_parent"
	    android:layout_height="?android:attr/listPreferredItemHeight"
	    android:textAppearance="?android:attr/textAppearanceLarge"
	    android:gravity="center_vertical"
	    android:checkMark="?android:attr/listChoiceIndicatorSingle"
	    android:paddingLeft="6dip"
	    android:paddingRight="6dip"
		/>
     **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);
        final ListView defaultUIListView = (ListView) findViewById(R.id.listview);
        defaultUIListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        defaultUIListView.setAdapter(new ArrayAdapter<String>(
        		getApplicationContext(),  android.R.layout.simple_list_item_single_choice, items));
        defaultUIListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(getApplicationContext(), items[defaultUIListView.getCheckedItemPosition()], Toast.LENGTH_SHORT).show();
			}
		});
    }
}