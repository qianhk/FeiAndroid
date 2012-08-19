/**
 * @(#)FeiSMSConst.java		2012-7-30
 * Copyright (c) 2007-2012 Shanghai ShuiDuShi Co.Ltd. All right reserved.
 * 
 */
package com.njnu.kai.feisms;

/**
 * class 
 * @author hongkai.qian
 * @version 1.0.0
 * @since 2012-7-30
 */
public final class FeiSMSConst {
	public static final String KEY_GROUP_ID = "groupId";
	public static final String KEY_CONTACTS_ID = "contactsId";
	public static final String KEY_CONTACTS_NAME = "contactsName";
	public static final String KEY_CONTACTS_PHONE = "contactsPhone";
	public static final String ACTION_GROUP_ID_UPDATED = "com.njnu.kai.feisms.group_id_updated";
	public static final String ACTION_SMS_SENT_STATUS = "com.njnu.kai.feisms.sms_sent_status";
	
	public static final int REQUEST_CODE_CHOOSE_CONTACTS = 1;
	public static final int GROUP_ID_EXCLUDE = -1;
	public static final int GROUP_ID_CREATE = -2;

}
