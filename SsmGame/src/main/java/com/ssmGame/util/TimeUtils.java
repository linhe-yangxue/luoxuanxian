package com.ssmGame.util;

import java.util.Calendar;

import com.ssmData.config.constant.ConfigConstant;

public class TimeUtils {
	
	public static long TodayStart()
	{
		Calendar t = Calendar.getInstance();
		t.set(Calendar.HOUR_OF_DAY, 0);
		t.set(Calendar.MINUTE, 0);
		t.set(Calendar.SECOND, 0);
		t.set(Calendar.MILLISECOND, 0);
		return t.getTime().getTime();
	}
	
	public static final long SEVEN_DAY_TIME = 7 * 24 * 60 * 60 * 1000;
	public static final long ONE_DAY_TIME = 1 * 24 * 60 * 60 * 1000;
	public static final long ONE_HOUR_TIME = 1 * 60 * 60 * 1000;
	public static final long ONE_MIN_TIME = 1 * 60 * 1000;
	
	public static boolean isLeap(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}
	
	public static int natureDayDiff(Calendar a, Calendar b) {	
		int a_year = a.get(Calendar.YEAR);
		int b_year = b.get(Calendar.YEAR);
		int a_day = a.get(Calendar.DAY_OF_YEAR);
		int b_day = b.get(Calendar.DAY_OF_YEAR);
		int diff = 0;
		if (a_year == b_year) {
			diff = a_day - b_day;
		} else {
			diff = (365 + (TimeUtils.isLeap(b_year)?1:0) - b_day) + a_day;
		}
		return diff;
	}
	
	public static int GetConfigWeekTime(int i) {
		int week_time = ConfigConstant.tConf.getWeekTime()[i];
		int result = Calendar.SUNDAY;
		switch (week_time) {
		case 1:
			result = Calendar.MONDAY;
			break;
		case 2:
			result = Calendar.TUESDAY;
			break;
		case 3:
			result = Calendar.WEDNESDAY;
			break;
		case 4:
			result = Calendar.THURSDAY;
			break;
		case 5:
			result = Calendar.FRIDAY;
			break;
		case 6:
			result = Calendar.SATURDAY;
			break;
		case 7:
			result = Calendar.SUNDAY;
			break;
		}
		return result;
	}
}
