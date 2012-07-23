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

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "feismsdata.db";

	public static final String TABLE_NAME_SMS_GROUP = "t_sms_group";
	public static final String COLUMN_NAME_GROUP_NAME = "group_name";
	public static final String COLUMN_NAME_GROUP_SMS = "group_sms";

	public static final String TABLE_NAME_SMS_CONTACTS = "t_sms_contacts";
	public static final String COLUMN_NAME_GROUP_ID = "group_id";
	public static final String COLUMN_NAME_CONTACTS_ID = "contacts_id";
	public static final String COLUMN_NAME_CONTACTS_NAME = "contacts_name";
	public static final String COLUMN_NAME_CONTACTS_PHONE_NUMBER = "contacts_phone_number";

	public FeiSMSDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		String sqlCreateTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s integer PRIMARY KEY AutoIncrement, "
				+ "%s text not null,  %s text not null);", TABLE_NAME_SMS_GROUP, BaseColumns._ID, COLUMN_NAME_GROUP_NAME, COLUMN_NAME_GROUP_SMS);
		db.execSQL(sqlCreateTable);
		sqlCreateTable = String.format("create table if not exists %s (%s integer PRIMARY KEY AUTOINCREMENT, %s integer not null, %s integer not null,"
				+ "%s text not null, %s text not null);", TABLE_NAME_SMS_CONTACTS, BaseColumns._ID, COLUMN_NAME_GROUP_ID, COLUMN_NAME_CONTACTS_ID,
				COLUMN_NAME_CONTACTS_NAME, COLUMN_NAME_CONTACTS_PHONE_NUMBER);
		db.execSQL(sqlCreateTable);
		String sqlCreateTrigger = String.format("CREATE TRIGGER tr_sms_group_delete AFTER DELETE ON %s FOR EACH ROW BEGIN DELETE FROM %s WHERE %s=old.id; END;"
				, TABLE_NAME_SMS_GROUP, TABLE_NAME_SMS_CONTACTS, COLUMN_NAME_GROUP_ID);
		db.execSQL(sqlCreateTrigger);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sqlDrop = "DROP TABLE IF EXISTS " + TABLE_NAME_SMS_CONTACTS;
		db.execSQL(sqlDrop);
		sqlDrop = "drop table if exists " + TABLE_NAME_SMS_GROUP;
		db.execSQL(sqlDrop);
		onCreate(db);
	}

}
