package com.njnu.kai.aidl.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.njnu.kai.aidl.service.IStockQuoteService;
import com.njnu.kai.aidl.service.PersonIml;
import com.njnu.kai.aidldemo.R;

public class AIDLClientMainActivity extends Activity {

	private static final String LOG_TAG = "AIDLClientMainActivity";

	private static final int BIND_STATE_IDLE = 0;
	private static final int BIND_STATE_BINDING= 1;
	private static final int BIND_STATE_BINDED = 2;

    private EditText mEdt;
	private TextView mQuote;
	private int mBindState;
	private IStockQuoteService mStockService;

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn:
                    if (mBindState == BIND_STATE_IDLE) {
                        Intent intent = new Intent(IStockQuoteService.class.getName());
                        bindService(intent, serConn, Context.BIND_AUTO_CREATE);
                    } else if (mBindState == BIND_STATE_BINDED) {
                        String str = mEdt.getText().toString();
                        try {
                            String quote = mStockService.getQuote(str, new PersonIml(29, "QHK"));
                            mQuote.setText("Stock Quote is: " + quote);
                        } catch (Exception e) {
                            e.printStackTrace();
                            mQuote.setText("Exception: " + e.toString());
                        }
                    }
                    break;

                case R.id.btnBind:
                    if (mBindState == BIND_STATE_IDLE) {
                        Intent intent = new Intent(IStockQuoteService.class.getName());
                        bindService(intent, serConn, Context.BIND_AUTO_CREATE);
                    }
                    break;

                case R.id.btnUnbind:
                    if (mBindState == BIND_STATE_BINDED) {
                        unbindService(serConn);
                        mBindState = BIND_STATE_IDLE;
                    }
                    break;

                case R.id.btnStartService:
                    startService(new Intent(IStockQuoteService.class.getName()));
                    break;

                case R.id.btnStopService:
//                    mBindState = BIND_STATE_IDLE;
                    stopService(new Intent(IStockQuoteService.class.getName()));
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aidl_client_main);
        Button btn = (Button)findViewById(R.id.btn);
        mEdt = (EditText)findViewById(R.id.edt_stock);
        mQuote = (TextView)findViewById(R.id.tv_quote);
        btn.setOnClickListener(mOnClickListener);
        btn = (Button)findViewById(R.id.btnBind);
        btn.setOnClickListener(mOnClickListener);
        btn = (Button)findViewById(R.id.btnUnbind);
        btn.setOnClickListener(mOnClickListener);
        btn = (Button)findViewById(R.id.btnStartService);
        btn.setOnClickListener(mOnClickListener);
        btn = (Button)findViewById(R.id.btnStopService);
        btn.setOnClickListener(mOnClickListener);
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBindState != BIND_STATE_IDLE) {
			unbindService(serConn);
		}
	}

	private ServiceConnection serConn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d(LOG_TAG, "onServiceDisconnected " + name);
			Toast.makeText(AIDLClientMainActivity.this, "onServiceDisconnected", Toast.LENGTH_SHORT).show();
			mBindState = BIND_STATE_IDLE;
			mStockService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(LOG_TAG, "onServiceConnected " + name + " " + service);
			mBindState = BIND_STATE_BINDED;
			Toast.makeText(AIDLClientMainActivity.this, "onServiceConnected", Toast.LENGTH_SHORT).show();
			mStockService = IStockQuoteService.Stub.asInterface(service);
		}
	};
}
