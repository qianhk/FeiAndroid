/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.hellojni;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

class TestClass {
	private String s;
	public static int a;
	
	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}
	
	private void priMethod() {
		s = "kaikai好";
	}

	public native void accessField();
}

public class HelloJni extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * Create a TextView and set its content. the text is retrieved by calling a native function.
		 */
		TextView tv = new TextView(this);
		tv.setText(stringFromJNIWithParam("Java"));
		setContentView(tv);
//        unimplementedStringFromJNI(); //java.lang.UnsatisfiedLinkError: unimplementedStringFromJNI
		
		int[] oriArr = new int[] {1, 2, 3, 4, 5};
		int sumA = sumArray(oriArr, oriArr.length);
		System.out.println("sumA=" + sumA); //expect 15
		
		int[][] j2arr = initInt2DArray(3);
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				System.out.println("(" + i + "," + j + ")=" + j2arr[i][j]);
			}
		}
		
		TestClass tc = new TestClass();
		tc.setS("qhk凯");
		tc.a = 1224;
		System.out.println("in java : before tc.s=" + tc.getS() + " a=" + tc.a);
		tc.accessField();
		System.out.println("in java : after tc.s=" + tc.getS() + " a=" + tc.a);
	}
	
	public native int sumArray(int arr[], int len);

	public native int[][] initInt2DArray(int size);	
	
	/*
	 * A native method that is implemented by the 'hello-jni' native library, which is packaged with this application.
	 */
	public native String stringFromJNI();

	public native String stringFromJNIWithParam(String a);

	/*
	 * This is another native method declaration that is *not* implemented by 'hello-jni'. This is simply to show that you can declare as
	 * many native methods in your Java code as you want, their implementation is searched in the currently loaded native libraries only the
	 * first time you call them.
	 * 
	 * Trying to call this function will result in a java.lang.UnsatisfiedLinkError exception !
	 */
	public native String unimplementedStringFromJNI();

	/*
	 * this is used to load the 'hello-jni' library on application startup. The library has already been unpacked into
	 * /data/data/com.example.hellojni/lib/libhello-jni.so at installation time by the package manager.
	 */
	static {
		System.loadLibrary("hello-jni");
	}
}
