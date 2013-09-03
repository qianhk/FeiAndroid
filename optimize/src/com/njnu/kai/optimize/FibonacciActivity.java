package com.njnu.kai.optimize;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


public class FibonacciActivity extends Activity {

    private TextView mTvResult;
    private TextView mTvResult2;
    private TextView mTvResult3;
    private EditText mEdtNumber;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fibonacci);

        mTvResult = (TextView)findViewById(R.id.tv_result);
        mTvResult2 = (TextView)findViewById(R.id.tv_result2);
        mTvResult3 = (TextView)findViewById(R.id.tv_result3);
        mEdtNumber = (EditText)findViewById(R.id.edt_number);
        findViewById(R.id.btn_calc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcFibonacci(Integer.parseInt(mEdtNumber.getText().toString()));
            }
        });
        findViewById(R.id.btn_calc2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcFibonacciWithLoop(Integer.parseInt(mEdtNumber.getText().toString()));
            }
        });
        findViewById(R.id.btn_calc3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcIteratively(Integer.parseInt(mEdtNumber.getText().toString()));
            }
        });
    }

    public void calcFibonacci(int n) {
        long nanoBegin = System.nanoTime();
        long value = computeRecursively(n);
        long nanoEnd = System.nanoTime();
        mTvResult.setText("value is " + value + ".     time is " + TimeUnit.NANOSECONDS.toMillis(nanoEnd - nanoBegin));
    }

    public long computeRecursively(int n) {
        if (n > 1) {
            return computeRecursively(n - 2) + computeRecursively(n - 1);
        } else {
            return n;
        }
    }

    public void calcFibonacciWithLoop(int n) {
        long nanoBegin = System.nanoTime();
        long value = computeRecursivelyWithLoop(n);
        long nanoEnd = System.nanoTime();
        mTvResult2.setText("value is " + value + ".     time is " + TimeUnit.NANOSECONDS.toMillis(nanoEnd - nanoBegin));
    }

    public long computeRecursivelyWithLoop(int n) {
        if (n > 1) {
            long result = 1;
            do {
                result += computeRecursivelyWithLoop(n - 2);
                --n;
            } while (n > 1);
            return result;
        } else {
            return n;
        }
    }


    public void calcIteratively(int n) {
        long nanoBegin = System.nanoTime();
        long value = computeiteratively(n);
        long nanoEnd = System.nanoTime();
        mTvResult3.setText("value is " + value + ".     time is " + TimeUnit.NANOSECONDS.toMillis(nanoEnd - nanoBegin));
    }

    public long computeiteratively(int n) {
        if (n > 1) {
            long a = 0, b = 1;
            do {
                long tmp = b;
                b += a;
                a = tmp;
            } while (--n > 1);
            return b;
        }
        return n;
    }
}