package com.njnu.kai.feisms;

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

	public static boolean isChinesePhoneNumber(String phoneNumber) {
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
			isMobile = (phoneNumber.length() == 11 && phoneNumber.charAt(0) == '1');
		}
//		if (!isMobile) {
//			Log.i(PREFIX, "not mobile=" + phoneNumber);
//		}
//		if (isMobile && !testNumber.equals(phoneNumber)) {
//			Log.i(PREFIX, "is mobile=" + testNumber + " after zl=" + phoneNumber);
//		}
		return isMobile;
	}
}
