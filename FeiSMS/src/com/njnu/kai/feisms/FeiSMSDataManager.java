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
		String[] columns = new String[] {BaseColumns._ID, FeiSMSDBHelper.COLUMN_NAME_GROUP_NAME,
				FeiSMSDBHelper.V_COLUMN_NAME_CONTACTS_AMOUNT};
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

//	public List<String> getKeyWords(String prefix) {
//		List<String> listKeyword = null;
//		if (!TextUtils.isEmpty(prefix)) {
//			String[] columns = new String[] {FeiSMSDBHelper.COLUMN_NAME_KEYWORD};
//			String selection = FeiSMSDBHelper.COLUMN_NAME_KEYWORD + " like '" + prefix + "%'";
//			String orderBy = "_id desc";
//			SQLiteDatabase database = mDbHelper.getReadableDatabase();
//			Cursor cursor = database.query(FeiSMSDBHelper.TABLE_NAME_KEYWORD_SEARCHED, columns, selection, null, null, null,
//					orderBy, MAX_RETURN_ROW);
//			int resultCount = cursor.getCount();
//			if (resultCount > 0) {
//				listKeyword = new ArrayList<String>(resultCount);
//				cursor.moveToFirst();
//				do {
//					String keyword = cursor.getString(0);
//					listKeyword.add(keyword);
//				} while (cursor.moveToNext());
//			}
//			cursor.close();
//			database.close();
//		}
//		return listKeyword;
//	}
//

}
