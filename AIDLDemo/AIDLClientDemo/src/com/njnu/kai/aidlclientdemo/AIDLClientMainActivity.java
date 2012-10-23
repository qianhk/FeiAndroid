package com.njnu.kai.aidlclientdemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AIDLClientMainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aidl_client_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.aidl_client_main, menu);
        return true;
    }
}
