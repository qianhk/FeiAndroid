package com.njnu.kai.ExceptionTest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class KaiActivity extends Activity {
    private static final String LOG_TAG = "KaiActivity";
    public static final String ACTION_CRASH_BROADCAST = "kaicrash_broadcast";
    public static final String AUTHORITY = "content://com.njnu.kai.ExceptionTest";

    private KaiCrashReceiver mKaiCrashReceiver = new KaiCrashReceiver();

    private class KaiCrashReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean thread = intent.getBooleanExtra("thread", false);
            if (thread) {
                Runnable runnable = new Runnable() {
                    public void run() {
                        Log.d(LOG_TAG, "will crash In Receiver Thread:");
                        int i = 1 / 0;
                    }
                };
                new Thread(runnable).start();
            }
            else {
                Log.d(LOG_TAG, "will crash In Receiver:");
                int i = 1 / 0;
            }
        }
    }
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ViewGroup rootView = (ViewGroup) findViewById(R.id.layoutRoot);
        Log.i(LOG_TAG, "rootView childView=" + rootView.getChildCount());
        setClickListener(rootView);
        registerReceiver(mKaiCrashReceiver, new IntentFilter(ACTION_CRASH_BROADCAST));
    }

    private void setClickListener(ViewGroup rootView) {
        for(int idx = rootView.getChildCount() - 1; idx >= 0; --idx) {
            View childView = rootView.getChildAt(idx);
            if (childView instanceof ViewGroup) {
                setClickListener((ViewGroup)childView);
            } else if (childView instanceof Button) {
                childView.setOnClickListener(mClickListener);
                Log.i(LOG_TAG, "set btn click listener id=:" + childView.getId() + " name=" + ((Button) childView).getText());
            }
        }
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnActivity:
                    crashInActivity();
                    break;

                case R.id.btnActivityThread:
                    crashInActivityThread();
                    break;

                case R.id.btnService:
                    crashInService();
                    break;

                case R.id.btnServiceThread:
                    crashInServiceThread();
                    break;

                case R.id.btnReceiver:
                    crashInReceiver();
                    break;

                case R.id.btnReceiverThread:
                    crashInReceiverThread();
                    break;

                case R.id.btnProvider:
                    crashInProvider();
                    break;

                case R.id.btnProviderThread:
                    crashInProviderThread();
                    break;

                default:
                    break;
            }
        }
    };

    private void crashInActivity() {
        Log.d(LOG_TAG, "will crash In Activity:");
        int i = 1 / 0;
    }

    private void crashInActivityThread() {
        Runnable runnable = new Runnable() {
            public void run() {
                Log.d(LOG_TAG, "will crash In Activity Thread:");
                int i = 1 / 0;
            }
        };
        new Thread(runnable).start();
    }

    private void crashInService() {
        Intent intent = new Intent(this, KaiService.class);
        startService(intent);
    }

    private void crashInServiceThread() {
        Intent intent = new Intent(this, KaiService.class);
        intent.putExtra("thread", true);
        startService(intent);
    }

    private void crashInReceiver() {
        Intent intent = new Intent(ACTION_CRASH_BROADCAST);
        sendBroadcast(intent);
    }

    private void crashInReceiverThread() {
        Intent intent = new Intent(ACTION_CRASH_BROADCAST);
        intent.putExtra("thread", true);
        sendBroadcast(intent);
    }

    private void crashInProvider() {
        getContentResolver().query(Uri.parse(AUTHORITY + "/10"), null, null, null, null);
    }

    private void crashInProviderThread() {
        getContentResolver().query(Uri.parse(AUTHORITY + "/10"), null, "thread", null, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, KaiService.class);
        stopService(intent);
        unregisterReceiver(mKaiCrashReceiver);
    }

}
