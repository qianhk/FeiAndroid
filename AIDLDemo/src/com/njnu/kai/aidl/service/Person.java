/**
 * @(#)Person.java		2012-12-12
 *
 */
package com.njnu.kai.aidl.service;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {
	private static final String LOG_TAG = "Person";

	private int mAge;
	private String mName;

	public int getAge() {
		return mAge;
	}

	public void setAge(int age) {
		this.mAge = age;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {

		@Override
		public Person createFromParcel(Parcel source) {
			return new Person(source);
		}

		@Override
		public Person[] newArray(int size) {
			return new Person[size];
		}
	};

	private Person(Parcel source) {
		readFromParcel(source);
	}

	public Person(int age, String name) {
		mAge = age;
		mName = name;
	}

	private void readFromParcel(Parcel source) {
		mAge = source.readInt();
		mName = source.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mAge);
		dest.writeString(mName);
	}
}
