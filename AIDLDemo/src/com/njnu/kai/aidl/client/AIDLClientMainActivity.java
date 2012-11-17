package com.njnu.kai.aidl.client;

import android.app.Activity;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.njnu.kai.aidl.service.IStockQuoteService;
import com.njnu.kai.aidldemo.R;

public class AIDLClientMainActivity extends Activity {

    private EditText mEdt;
	private TextView mQuote;
	private boolean mBindService;
	private IStockQuoteService mStockService;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aidl_client_main);
        Button btn = (Button)findViewById(R.id.btn);
        mEdt = (EditText)findViewById(R.id.edt_stock);
        mQuote = (TextView)findViewById(R.id.tv_quote);
        
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!mBindService) {
					//
				}
				String str = mEdt.getText().toString();
				
			}
		});
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	
	private ServiceConnection
}
