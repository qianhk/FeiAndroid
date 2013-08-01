package com.njnu.kai.feiphoneinfo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.UUID;

public class AboutActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.about);

//        testUUID();
//        testNormal();
	}

    public static void testUUID() {
        int hascode = 0;
        long curTime = System.currentTimeMillis();
        for (long idx = 0; idx < 10000; ++idx) {
            hascode = UUID.randomUUID().toString().hashCode();
        }
        System.out.println("testuuid uuid=" + (System.currentTimeMillis() - curTime));
    }

    private static String normalfunction(long idx) {
        String xx = String.format("xx%d%d%d, %s", idx, 20, 30, "hongkai.qian");
        return xx;
    }

    public static void testNormal() {
        int hascode = 0;
        long curTime = System.currentTimeMillis();
        for (long idx = 0; idx < 10000; ++idx) {
            hascode = normalfunction(idx).hashCode();
        }
        System.out.println("testuuid normal=" + (System.currentTimeMillis() - curTime));
    }

}
