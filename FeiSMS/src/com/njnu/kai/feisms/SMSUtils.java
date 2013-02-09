package com.njnu.kai.feisms;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

public final class SMSUtils {
	private final static String MARK_FENGEHAO = "-";
	private final static String MARK_BLANK_SPACE = " ";
	private final static String MARK_EMPTY_STRING = "";
	private final static String MARK_PLUS = "+";
	private final static String MARK_PREFIX_17951 = "17951";
	private final static String MARK_PREFIX_12530 = "12530";
	private final static String MARK_CHINESE_AREA_CODE = "86";
	private final static String PREFIX = "SMSUtils";
	private static int mCurrentDetailGroupId;

	public static int getCurrentDetailGroupId() {
		return mCurrentDetailGroupId;
	}

	public static void setCurrentDetailGroupId(int currentDetailGroupId) {
		mCurrentDetailGroupId = currentDetailGroupId;
	}

	public static String convertToChinesePhoneNumber(String phoneNumber) {
//		String testNumber = phoneNumber.toLowerCase();
		boolean isMobile = !TextUtils.isEmpty(phoneNumber);
		if (isMobile) {
			phoneNumber = phoneNumber.replace(MARK_FENGEHAO, MARK_EMPTY_STRING).replace(MARK_BLANK_SPACE, MARK_EMPTY_STRING)
					.replace(MARK_PLUS, MARK_EMPTY_STRING);
			if (phoneNumber.startsWith(MARK_PREFIX_17951)) {
				phoneNumber = phoneNumber.substring(MARK_PREFIX_17951.length());
			} else if (phoneNumber.startsWith(MARK_PREFIX_12530)) {
				phoneNumber = phoneNumber.substring(MARK_PREFIX_12530.length());
			}
			if (phoneNumber.startsWith(MARK_CHINESE_AREA_CODE)) {
				phoneNumber = phoneNumber.substring(MARK_CHINESE_AREA_CODE.length());
			}
			isMobile = ((phoneNumber.length() == 11 && phoneNumber.charAt(0) == '1') || (phoneNumber.length() == 4 && phoneNumber.startsWith("555")));
		}
//		if (!isMobile) {
//			Log.i(PREFIX, "not mobile=" + phoneNumber);
//		}
//		if (isMobile && !testNumber.equals(phoneNumber)) {
//			Log.i(PREFIX, "is mobile=" + testNumber + " after zl=" + phoneNumber);
//		}
		
		return isMobile ? phoneNumber : null;
	}

	private static ContactsData mContactsData = null;
	
	public static synchronized void clearContactsData() {
		mContactsData = null;
		//TODO 考虑到内存占用问题，软件到后台后最好清掉这里的数据。
	}
	
	public static ContactsData getContactsData() {
		return mContactsData;
	}

	public static synchronized ContactsData getContactsData(Context context) {

		if (mContactsData == null) {
			String[] personProjection = new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME,
					ContactsContract.Contacts.HAS_PHONE_NUMBER };
			
			Cursor cur = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, personProjection, null, null,
					 Build.VERSION.SDK_INT < 8 ? null : ContactsContract.Contacts.SORT_KEY_PRIMARY + " asc"
					);
			mContactsData = new ContactsData(cur.getCount());
			int phoneNo = 0;
			if (cur.moveToFirst()) {
				do {

					long contactId = cur.getLong(0);
					String disPlayName = cur.getString(1);
					int phoneCount = cur.getInt(2);
					if (phoneCount > 0) {
						Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
						ContactsData.ContactsInfo cInfo = null;
						if (phones.moveToFirst()) {
							do {
								String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								phoneNumber = convertToChinesePhoneNumber(phoneNumber);
								if (phoneNumber != null) {
									if (cInfo == null) {
										cInfo = new ContactsData.ContactsInfo(contactId, disPlayName, phones.getCount());
									}
									cInfo.appendPhoneNumber(phoneNumber);
									++phoneNo;
								}
							} while (phones.moveToNext());
						}
						phones.close();
						if (cInfo != null) {
							mContactsData.appendContactsInfo(cInfo);
						}
					}

				} while (cur.moveToNext());

			}
			cur.close();
			mContactsData.setPhoneCount(phoneNo);
			FeiSMSApplication.getInstance().setContactsData(mContactsData);
		}
//		Log.i(PREFIX, "total phoneNumber is: " + phoneNo + " total person is:" + cData.getCount());
		return mContactsData;
	}
}
