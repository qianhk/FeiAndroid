/**
 * @(#)PersonIml.java		2013-1-24
 * Copyright (c) 2007-2013 Shanghai ShuiDuShi Co.Ltd. All right reserved.
 *
 */
package com.njnu.kai.aidl.service;

import android.os.Parcel;

public class PersonIml extends Person {
	private int mTestId;
	public PersonIml(int age, String name) {
		super(age, name);
		mTestId = 666;
	}
	public PersonIml(Parcel source) {
		super(source);
		mTestId = 666;
	}

}