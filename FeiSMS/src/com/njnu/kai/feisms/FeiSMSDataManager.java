package com.njnu.kai.feisms;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import com.njnu.kai.feisms.ContactsData.ContactsInfo;

class SMSGroupInfo {
	private int mGroupId;
	private int mPersonAmount;
	private String mGroupName;

	@Override
	public int hashCode() {
		return mGroupId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SMSGroupInfo other = (SMSGroupInfo) obj;
		return mGroupId == other.mGroupId;
	}

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

	public void setGroupId(int groupId) {
		mGroupId = groupId;
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

	public int getDbId() {
		return mDbId;
	}

	public int getContactsId() {
		return mContactsId;
	}

	public String getContactsName() {
		return mContactsName;
	}

	public String getPhoneNumber() {
		return mPhoneNumber;
	}

	@Override
	public String toString() {
		StringBuilder build = new StringBuilder(32);
		build.append(mContactsName);
		while (build.length() < 3) {
			build.append('\u3000'); // 全角空格
		}
		build.append(": ");
		build.append(mPhoneNumber);
		return build.toString();
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

	public int getGroupId() {
		return mGroupId;
	}

	public List<SMSGroupEntryContactsItem> getListContacts() {
		return mListContacts;
	}

	public int getCount() {
		return mListContacts.size();
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

	public boolean appendSMSGroup(String groupName, String groupSMS) {
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

	public boolean updateSMSGroup(SMSGroupEntrySMS groupEntrySMS) {
		boolean operateSucess = true;

		SQLiteDatabase database = mDbHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues(2);
		contentValues.put(FeiSMSDBHelper.COLUMN_NAME_GROUP_NAME, groupEntrySMS.getGroupName());
		contentValues.put(FeiSMSDBHelper.COLUMN_NAME_GROUP_SMS, groupEntrySMS.getSMSContent());

		if (groupEntrySMS.getGroupId() >= 0) {
			operateSucess = database.update(FeiSMSDBHelper.TABLE_NAME_SMS_GROUP, contentValues, "_id=" + groupEntrySMS.getGroupId(), null) != -1;

		} else {
			int newGroupId = (int) database.insert(FeiSMSDBHelper.TABLE_NAME_SMS_GROUP, null, contentValues);
			if (newGroupId >= 0) {
				groupEntrySMS.setGroupId(newGroupId);
			} else {
				operateSucess = false;
			}
		}
		database.close();

		return operateSucess;
	}

	public void deleteSMSGroup(int[] groupIds) {
		StringBuffer deleteSql = new StringBuffer("delete from " + FeiSMSDBHelper.TABLE_NAME_SMS_GROUP + " where _id in(-100");
		for (int idx = groupIds.length - 1; idx >= 0; --idx) {
			deleteSql.append("," + groupIds[idx]);
		}
		deleteSql.append(");");
		executeSql(deleteSql.toString());
	}

	public void deleteSMSContacts(int[] contactsIds) {
		StringBuffer deleteSql = new StringBuffer("delete from " + FeiSMSDBHelper.TABLE_NAME_SMS_CONTACTS + " where _id in(-100");
		for (int idx = contactsIds.length - 1; idx >= 0; --idx) {
			deleteSql.append("," + contactsIds[idx]);
		}
		deleteSql.append(");");
		executeSql(deleteSql.toString());
	}

	private void executeSql(String sql) {
		Log.i(PREFIX, "sql is: " + sql);
		SQLiteDatabase database = mDbHelper.getWritableDatabase();
		database.execSQL(sql.toString());
		database.close();
	}

	public boolean appendContactsToGroup(int groupId, int contactsId, String contactsName, String contactsPhoneNumber) {
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

	public boolean appendContactsToGroup(int groupId, ArrayList<SMSGroupEntryContactsItem> listContacts) {
		boolean operateSucess = true;
		SQLiteDatabase database = mDbHelper.getWritableDatabase();
		database.beginTransaction();
		int size = listContacts.size();
		ContentValues contentValues = new ContentValues(4);
		for (int idx = 0; idx < size && operateSucess; ++idx) {
			SMSGroupEntryContactsItem item = listContacts.get(idx);
			contentValues.put(FeiSMSDBHelper.COLUMN_NAME_GROUP_ID, groupId);
			contentValues.put(FeiSMSDBHelper.COLUMN_NAME_CONTACTS_ID, item.getContactsId());
			contentValues.put(FeiSMSDBHelper.COLUMN_NAME_CONTACTS_NAME, item.getContactsName());
			contentValues.put(FeiSMSDBHelper.COLUMN_NAME_CONTACTS_PHONE_NUMBER, item.getPhoneNumber());
			operateSucess = database.insert(FeiSMSDBHelper.TABLE_NAME_SMS_CONTACTS, null, contentValues) != -1;
		}
		if (operateSucess) {
			database.setTransactionSuccessful();
		}
		database.endTransaction();
		database.close();
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

	public SMSGroupEntryContacts getSMSGroupEntryContacts(int groupId) {
		SMSGroupEntryContacts gContacts = null;
		String[] columns = { BaseColumns._ID, FeiSMSDBHelper.COLUMN_NAME_CONTACTS_ID, FeiSMSDBHelper.COLUMN_NAME_CONTACTS_NAME,
				FeiSMSDBHelper.COLUMN_NAME_CONTACTS_PHONE_NUMBER };
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor c = db.query(FeiSMSDBHelper.TABLE_NAME_SMS_CONTACTS, columns, FeiSMSDBHelper.COLUMN_NAME_GROUP_ID + "=?",
				new String[] { String.valueOf(groupId) }, null, null, null);
		int resultCount = c.getCount();
		if (resultCount > 0) {
			gContacts = new SMSGroupEntryContacts(groupId, resultCount);
			c.moveToFirst();
			do {
				int dbId = c.getInt(0);
				int contactsId = c.getInt(1);
				String contactsName = c.getString(2);
				String phoneNumber = c.getString(3);
				gContacts.appendContactsItem(dbId, contactsId, contactsName, phoneNumber);
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return gContacts;
	}

	public long[] getUsedContactsId() {
		String querySql = String.format("select %1$s from %2$s order by %1$s asc;", FeiSMSDBHelper.COLUMN_NAME_CONTACTS_ID,
				FeiSMSDBHelper.TABLE_NAME_SMS_CONTACTS);
		Log.i(PREFIX, "getUsedContactsId = " + querySql);
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor c = db.rawQuery(querySql, null);
		int resultCount = c.getCount();
		long[] arrayIds = new long[resultCount];
		if (resultCount > 0) {
			int idx = -1;
			c.moveToFirst();
			do {
				arrayIds[++idx] = c.getLong(0);
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return arrayIds;
	}

	public List<String> getContactsNoUsedPhoneNumber(final ContactsInfo contactsInfo) {
		List<String> noUsedPhone = null;
		int phoneNumberCount = contactsInfo.getPhoneNumberCount();
		if (phoneNumberCount > 0) {
			StringBuilder querySqlBuilder = new StringBuilder("select ttkai from (select '" + contactsInfo.getPhoneNumber(0) + "' ttkai ");
			for (int idx = 1; idx < phoneNumberCount; ++idx) {
				querySqlBuilder.append("union select '" + contactsInfo.getPhoneNumber(idx) + "'");
			}
			querySqlBuilder.append(") t where ttkai not in(");
			String existPhoneSql = String.format("select %1$s from %2$s where %3$s=%4$d);",
					FeiSMSDBHelper.COLUMN_NAME_CONTACTS_PHONE_NUMBER, FeiSMSDBHelper.TABLE_NAME_SMS_CONTACTS, FeiSMSDBHelper.COLUMN_NAME_CONTACTS_ID,
					contactsInfo.getId());
			querySqlBuilder.append(existPhoneSql);
			Log.i(PREFIX, "getContactsNoUsedPhoneNumber " + querySqlBuilder.toString());
			SQLiteDatabase db = mDbHelper.getReadableDatabase();
			Cursor c = db.rawQuery(querySqlBuilder.toString(), null);
			int resultCount = c.getCount();
			if (resultCount > 0) {
				noUsedPhone = new ArrayList<String>(resultCount);
				c.moveToFirst();
				do {
					noUsedPhone.add(c.getString(0));
				} while (c.moveToNext());
			}
			c.close();
			db.close();
		}
//		contactsInfo.setId(100);
//		contactsInfo = null;
		return noUsedPhone;
	}
}
