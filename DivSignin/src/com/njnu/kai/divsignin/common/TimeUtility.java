package com.njnu.kai.divsignin.common;

import java.util.Calendar;

public final class TimeUtility {
	private static final String PREFIX = "[TimeUtility]:";
//	public static final long ONE_DAY_IN_MILLISECOND = 10 * 1000; //24 * 3600 * 1000;
	private static int READY_QL_HOUR = 7;
	private static int READY_QL_MINUTE = 54;

	public static boolean isTimeToQiangLou() {
		boolean isTime = false;
		Calendar cal = Calendar.getInstance();
		int curHour = cal.get(Calendar.HOUR_OF_DAY);
		int curMinute = cal.get(Calendar.MINUTE);
		if ((curHour > READY_QL_HOUR) || (curHour == READY_QL_HOUR && curMinute >= READY_QL_MINUTE)) {
			isTime = true;
		}
		return isTime;
	}

	public static Calendar getNextStartTime(boolean isTimeToQiangLou) {
		Calendar cal = Calendar.getInstance();
//		Log.i(PREFIX, cal.getTime().toLocaleString() + " h=" + cal.get(Calendar.HOUR_OF_DAY));
		if (isTimeToQiangLou) {
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
		}
		cal.set(Calendar.HOUR_OF_DAY, READY_QL_HOUR);
		cal.set(Calendar.MINUTE, READY_QL_MINUTE);
		cal.set(Calendar.SECOND, 0);
		return cal;
	}

}
