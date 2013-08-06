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

import java.util.ArrayList;
import java.util.List;

public class OptimizeActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<ComponentName> adapter = new ArrayAdapter<ComponentName>(this, R.layout.simple_list_item_1, getData());
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                OptimizeActivity activity = OptimizeActivity.this;
                Intent intent = new Intent();
                intent.setComponent((ComponentName)adapterView.getItemAtPosition(position));
                activity.startActivity(intent);
            }
        });
    }

    private List<ComponentName> getData() {
        ArrayList<ComponentName> list = new ArrayList<ComponentName>();
        list.add(new ComponentName(FibonacciActivity.class.getPackage().getName(), FibonacciActivity.class.getName()));
        return list;
    }


}
