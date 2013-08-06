package com.njnu.kai.optimize;

import android.*;
import android.R;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptimizeActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ArrayAdapter<ComponentName> adapter = new ArrayAdapter<ComponentName>(this, R.layout.simple_list_item_1, getData());
        SimpleAdapter adapter = new SimpleAdapter(this, getData(),
                android.R.layout.simple_list_item_1, new String[] { "title" },
                new int[] { android.R.id.text1 });
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                OptimizeActivity activity = OptimizeActivity.this;
                Map<String, Object> map = (Map<String, Object>)adapterView.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.setComponent((ComponentName)map.get("component"));
                activity.startActivity(intent);
            }
        });
    }

//    private List<ComponentName> getData() {
//        ArrayList<ComponentName> list = new ArrayList<ComponentName>();
//        list.add(new ComponentName(FibonacciActivity.class.getPackage().getName(), FibonacciActivity.class.getName()));
//        return list;
//    }

    private List<Map<String, Object>> getData() {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        addActivityToData(list, FibonacciActivity.class);

        return list;
    }

    private void addActivityToData(ArrayList<Map<String, Object>> data, Class<?> clazz) {
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("title", clazz.getSimpleName());
        temp.put("component", new ComponentName(clazz.getPackage().getName(), clazz.getName()));
        data.add(temp);
    }

}
