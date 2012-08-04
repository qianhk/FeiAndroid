package com.njnu.kai.feisms;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;

class SMSGroupInfo {
	private int mGroupId;
	private int mPersonAmount;
	private String mGroupName;

	public SMSGroupInfo(int groupId, String groupName, int personAmount) {
		mGroupId = groupId;
		mGroupName = groupName;
		mPersonAmount = personAmount;
	}

	public int getGroupId() {
		return mGroupId;
	}

	public int getPersonAmount() {
		return mPersonAmount;
	}

	public String getGroupName() {
		return mGroupName;
	}
}

class SMSGroupEntrySMS {
	private int mGroupId;
	private String mGroupName;
	private String mSMSContent;

	public SMSGroupEntrySMS(int groupId, String groupName, String sMSContent) {
		mGroupId = groupId;
		mGroupName = groupName;
		mSMSContent = sMSContent;
	}

	public int getGroupId() {
		return mGroupId;
	}

	public String getGroupName() {
		return mGroupName;
	}

	public String getSMSContent() {
		return mSMSContent;
	}

	public void setGroupId(int groupId) {
		mGroupId = groupId;
	}

	public void setGroupName(String groupName) {
		mGroupName = groupName;
	}

	public void setSMSContent(String sMSContent) {
		mSMSContent = sMSContent;
	}
}

class SMSGroupEntryContactsItem {

	public SMSGroupEntryContactsItem(int dbId, int contactsId, String contactsName, String phoneNumber) {
		mDbId = dbId;
		mContactsId = contactsId;
		mContactsName = contactsName;
		mPhoneNumber = phoneNumber;
	}

	private int mDbId;
	private int mContactsId;
	private String mContactsName;
	private String mPhoneNumber;
}

class SMSGroupEntryContacts {
	private int mGroupId;
	private List<SMSGroupEntryContactsItem> mListContacts;

	public SMSGroupEntryContacts(int groupId, int amountOfContacts) {
		mGroupId = groupId;
		mListContacts = new ArrayList<SMSGroupEntryContactsItem>(amountOfContacts);
	}

	public void appendContactsItem(SMSGroupEntryContactsItem item) {
		mListContacts.add(item);
	}

	public void appendContactsItem(int dbId, int contactsId, String contactsName, String phoneNumber) {
		appendContactsItem(new SMSGroupEntryContactsItem(dbId, contactsId, contactsName, phoneNumber));
	}
}

public final class FeiSMSDataManager {
	private static final String PREFIX = "[FeiSMSDataManager]:";
	private FeiSMSDBHelper mDbHelper;
	private static FeiSMSDataManager mInstance;

	private FeiSMSDataManager(Context context) {
		mDbHelper = new FeiSMSDBHelper(context);
	}

	public static synchronized FeiSMSDataManager getDefaultInstance(Context context) {
		if (mInstance == null) {
			mInstance = new FeiSMSDataManager(context);
		}
		return mInstance;
	}

	public boolean AppendSMSGroup(String groupName, String groupSMS) {
		boolean operateSucess = true;
		if (TextUtils.isEmpty(groupName) || TextUtils.isEmpty(groupSMS)) {
			operateSucess = false;
		} else {
			SQLiteDatabase database = mDbHelper.getWritableDatabase();
			ContentValues contentValues = new ContentValues(2);
			contentValues.put(FeiSMSDBHelper.COLUMN_NAME_GROUP_NAME, groupName);
			contentValues.put(FeiSMSDBHelper.COLUMN_NAME_GROUP_SMS, groupSMS);
			operateSucess = database.insert(FeiSMSDBHelper.TABLE_NAME_SMS_GROUP, null, contentValues) != -1;
			database.close();
		}
		return operateSucess;

	}

	public boolean UpdateSMSGroup(SMSGroupEntrySMS groupEntrySMS) {
		boolean operateSucess = true;
		{
			SQLiteDatabase database = mDbHelper.getWritableDatabase();
			ContentValues contentValues = new ContentValues(2);
			contentValues.put(FeiSMSDBHelper.COLUMN_NAME_GROUP_NAME, groupEntrySMS.getGroupName());
			contentValues.put(FeiSMSDBHelper.COLUMN_NAME_GROUP_SMS, groupEntrySMS.getSMSContent());
			operateSucess = database.update(FeiSMSDBHelper.TABLE_NAME_SMS_GROUP, contentValues, "_id=" + groupEntrySMS.getGroupId(), null) != -1;
			database.close();
		}
		return operateSucess;
	}

	public boolean AppendContactsToGroup(int groupId, int contactsId, String contactsName, String contactsPhoneNumber) {
		boolean operateSucess = true;
		if (TextUtils.isEmpty(contactsName) || TextUtils.isEmpty(contactsPhoneNumber)) {
			operateSucess = false;
		} else {
			SQLiteDatabase database = mDbHelper.getWritableDatabase();
			ContentValues contentValues = new ContentValues(4);
			contentValues.put(FeiSMSDBHelper.COLUMN_NAME_GROUP_ID, groupId);
			contentValues.put(FeiSMSDBHelper.COLUMN_NAME_CONTACTS_ID, contactsId);
			contentValues.put(FeiSMSDBHelper.COLUMN_NAME_CONTACTS_NAME, contactsName);
			contentValues.put(FeiSMSDBHelper.COLUMN_NAME_CONTACTS_PHONE_NUMBER, contactsPhoneNumber);
			operateSucess = database.insert(FeiSMSDBHelper.TABLE_NAME_SMS_CONTACTS, null, contentValues) != -1;
			database.close();
		}
		return operateSucess;
	}

	public List<SMSGroupInfo> getAllSMSGroupInfo() {
		List<SMSGroupInfo> listSmsGroupInfo = null;
		String[] columns = new String[] { BaseColumns._ID, FeiSMSDBHelper.COLUMN_NAME_GROUP_NAME, FeiSMSDBHelper.V_COLUMN_NAME_CONTACTS_AMOUNT };
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.query(FeiSMSDBHelper.V_TABLE_NAME_GROUP_INFO, columns, null, null, null, null, null);

		int resultCount = cursor.getCount();
		if (resultCount > 0) {
			listSmsGroupInfo = new ArrayList<SMSGroupInfo>(resultCount);
			cursor.moveToFirst();
			do {
				listSmsGroupInfo.add(new SMSGroupInfo(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		return listSmsGroupInfo;
	}

	public SMSGroupEntrySMS getSMSGroupEntrySMS(int groupId) {
		SMSGroupEntrySMS gSms = null;
		String[] columns = { BaseColumns._ID, FeiSMSDBHelper.COLUMN_NAME_GROUP_NAME, FeiSMSDBHelper.COLUMN_NAME_GROUP_SMS };
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		String querySql = String.format("select %s, %s, %s from %s where %s=%d;", BaseColumns._ID, FeiSMSDBHelper.COLUMN_NAME_GROUP_NAME,
				FeiSMSDBHelper.COLUMN_NAME_GROUP_SMS, FeiSMSDBHelper.TABLE_NAME_SMS_GROUP, BaseColumns._ID, groupId);
		Cursor cursor = db.rawQuery(querySql, null);
		int resultCount = cursor.getCount();
		if (resultCount == 1) {
			cursor.moveToFirst();
			gSms = new SMSGroupEntrySMS(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
		}
		cursor.close();
		db.close();
		return gSms;
	}

}
