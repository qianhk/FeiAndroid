package com.njnu.kai.feisms;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class FeiSMSDBHelper extends SQLiteOpenHelper {

	private static final String PREFIX = "[FeiSMSDBHelper]:";

	private static final int DATABASE_VERSION = 4;

	private static final String DATABASE_NAME = "feismsdata.db";

	public static final String TABLE_NAME_SMS_GROUP = "t_sms_group";
	public static final String COLUMN_NAME_GROUP_NAME = "group_name";
	public static final String COLUMN_NAME_GROUP_SMS = "group_sms";

	public static final String TABLE_NAME_SMS_CONTACTS = "t_sms_contacts";
	public static final String COLUMN_NAME_GROUP_ID = "group_id";
	public static final String COLUMN_NAME_CONTACTS_ID = "contacts_id";
	public static final String COLUMN_NAME_CONTACTS_NAME = "contacts_name";
	public static final String COLUMN_NAME_CONTACTS_PHONE_NUMBER = "contacts_phone_number";
	public static final String COLUMN_NAME_SEND_STATE = "send_state";
	
	public static final String V_TABLE_NAME_GROUP_INFO = "v_group_info";
	public static final String V_COLUMN_NAME_CONTACTS_AMOUNT = "contacts_amount";

	public FeiSMSDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(PREFIX, "onCreate");
		db.beginTransaction();
		String sqlCreateTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s integer PRIMARY KEY AutoIncrement, "
				+ "%s text not null,  %s text not null);", TABLE_NAME_SMS_GROUP, BaseColumns._ID, COLUMN_NAME_GROUP_NAME,
				COLUMN_NAME_GROUP_SMS);
		db.execSQL(sqlCreateTable);
		sqlCreateTable = String.format(
				"create table if not exists %s (%s integer PRIMARY KEY AUTOINCREMENT, %s integer not null, %s integer not null,"
						+ "%s text not null, %s text not null);", TABLE_NAME_SMS_CONTACTS, BaseColumns._ID, COLUMN_NAME_GROUP_ID,
				COLUMN_NAME_CONTACTS_ID, COLUMN_NAME_CONTACTS_NAME, COLUMN_NAME_CONTACTS_PHONE_NUMBER);
		db.execSQL(sqlCreateTable);
		String sqlCreateTrigger = String.format(
				"CREATE TRIGGER tr_sms_group_delete AFTER DELETE ON %s FOR EACH ROW BEGIN DELETE FROM %s WHERE %s=old._id; END;",
				TABLE_NAME_SMS_GROUP, TABLE_NAME_SMS_CONTACTS, COLUMN_NAME_GROUP_ID);
		db.execSQL(sqlCreateTrigger);
		db.setTransactionSuccessful();
		db.endTransaction();
		if (DATABASE_VERSION > 1) {
			onUpgrade(db, 1, DATABASE_VERSION);
		}
	}

	private void upgradeFromVersion1(SQLiteDatabase db) {
	}
	
	private void upgradeFromVersion2(SQLiteDatabase db) {
		String sqlCreateView = String.format("create view %s as select t1._id, t1.%s,(select count(*) from %s t2 where t2.%s=t1._id) as %s from %s t1;"
				, V_TABLE_NAME_GROUP_INFO, COLUMN_NAME_GROUP_NAME, TABLE_NAME_SMS_CONTACTS, COLUMN_NAME_GROUP_ID, V_COLUMN_NAME_CONTACTS_AMOUNT, TABLE_NAME_SMS_GROUP);
		db.execSQL(sqlCreateView);
	}
	
	private void upgradeFromVersion3(SQLiteDatabase db) {
		String sqlCreateView = String.format("alter table %1$s add column %2$s integer not null default 0;"
				, TABLE_NAME_SMS_CONTACTS, COLUMN_NAME_SEND_STATE);
		db.execSQL(sqlCreateView);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(PREFIX, "onUpgrade oldVersion=" + oldVersion + " newVersion=" + newVersion);
		db.beginTransaction();
		if (oldVersion == 1) {	//from 1 to 2
			upgradeFromVersion1(db);
			++oldVersion;
		}
		if (oldVersion == 2) { //from 2 to 3
			upgradeFromVersion2(db);
			++oldVersion;
		}
		if (oldVersion == 3) { //from 3 to 4
			upgradeFromVersion3(db);
			++oldVersion;
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}
}
