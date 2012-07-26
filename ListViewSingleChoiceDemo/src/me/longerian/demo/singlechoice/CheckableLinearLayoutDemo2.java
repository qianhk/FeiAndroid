package me.longerian.demo.singlechoice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CheckableLinearLayoutDemo2 extends Activity {

	public static final String[] items = new String[] {
		"S", "M", "L", "XL", "EXL"
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);
        final ListView defaultUIListView = (ListView) findViewById(R.id.listview);
        defaultUIListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        defaultUIListView.setAdapter(new ArrayAdapter<String>(
        		getApplicationContext(),  R.layout.checkable_linear_layout_item2, R.id.size, items));
        defaultUIListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(getApplicationContext(), items[defaultUIListView.getCheckedItemPosition()], Toast.LENGTH_SHORT).show();
			}
		});
    }
	
	
}
