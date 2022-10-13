package com.ssmCore.tool.colligate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	/**
	 * 缺省日期时间格式 yyyy-MM-dd
	 */
	public final static String DEFAULT_DATA_PATTERN = "yyyy-MM-dd";
	/**
	 * 缺省日期时间格式 yyyy-MM-dd HH:mm:ss
	 */
	public final static String DEFAULT_DATATIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 缺省日期时间格式 yyyyMMddHHmmss
	 */
	public final static String DEFAULTPATTERN = "yyyyMMddHHmmss";
	/**
	 * 缺省时间格式化工具
	 */
	private final static SimpleDateFormat DEFAULT_DATETIME_FORMAT = new SimpleDateFormat(DEFAULT_DATATIME_PATTERN);

	private final static SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(DEFAULT_DATA_PATTERN);

	private final static SimpleDateFormat DEFAULTDATEFORMAT = new SimpleDateFormat(DEFAULTPATTERN);

	/**
	 * 获取当前时间（格式："2012-05-10 11:50:50" ）
	 * 
	 * @return
	 */
	public static String getCurrentDateTime() {
		return DEFAULTDATEFORMAT.format(System.currentTimeMillis());
	}

	public static String getCurrentDate_Time() {
		return DEFAULT_DATETIME_FORMAT.format(System.currentTimeMillis());
	}

	public static String getCurrentDate_patten() {
		return DEFAULT_DATETIME_FORMAT.format(System.currentTimeMillis());
	}

	/**
	 * 时间相加计算
	 * 
	 * @param start
	 * @param mills
	 *            秒
	 * @return
	 */
	public static long GetAddDate(long start, long mills) {
		long diff = start + (mills * 1000);
		return diff;
	}

	public static long GetSubDate(Date end, Date begin) {
		long diff = end.getTime() - begin.getTime();
		return diff / 1000;
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		return getDateTime("hh:mm:ss", System.currentTimeMillis());
	}

	/**
	 * 获取当天的日期（年月日）
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		return getDateTime("yyyy-MM-dd hh:mm:ss", System.currentTimeMillis());
	}

	/**
	 * 获取当前日期
	 * 
	 * @param format
	 *            "yyyy-MM-dd hh:mm:ss"
	 * @return
	 */
	public static String getCurrentDate(String format) {
		return getDateTime(format, System.currentTimeMillis());
	}

	public static String getCurrDate(String sign) {
		String tfromat = null;
		if (sign != null)
			tfromat = "yyyy" + sign + "MM" + sign + "dd";
		else
			tfromat = "yyyyMMdd";

		return getDateTime(tfromat, System.currentTimeMillis());
	}

	/**
	 * 字符窜转时间
	 * 
	 * @param dateString
	 * @param patter
	 * @return
	 * @throws ParseException
	 */
	public static Date getStringDate(String dateString, String patter) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(patter);
		Date date = sdf.parse(dateString);
		return date;
	}

	/**
	 * 获取指定时间的默认格式化字符串(格式："2012-05-10 11:50:50" )
	 * 
	 * @param _calendar
	 * @return
	 */
	public static String getDefaultTime(Calendar _calendar) {
		return getDateTime(DEFAULT_DATATIME_PATTERN, _calendar.getTimeInMillis());
	}

	/**
	 * 获取指定时间的默认格式化字符串(格式："2012-05-10 11:50:50" )
	 * 
	 * @param _calendar
	 * @return
	 */
	public static String getDefaultTime(Date _date) {
		return getDateTime(DEFAULT_DATATIME_PATTERN, _date.getTime());
	}

	/**
	 * 转换时间
	 * 
	 * @param _date
	 * @param format
	 *            格式化
	 * @return
	 */
	public static String getDefaultTime(Date _date, String format) {
		return getDateTime(format, _date.getTime());
	}

	public static Date getDate(String _time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			long time = Long.parseLong(_time) * 1000;
			_time = getDefaultTime(time);
			date = format.parse(_time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取时间
	 * 
	 * @param time
	 *            时间戳 毫秒
	 * @param formatstr
	 * @return
	 */
	public static Date getDate(Long time, String formatstr) {
		SimpleDateFormat format = new SimpleDateFormat(formatstr);
		Date date = null;
		try {
			String _time = getDefaultTime(time);
			date = format.parse(_time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date strParesDate(String _time) {
		SimpleDateFormat format = null;
		Date date = null;
		try {
			if (_time.indexOf(":") < 0)
				format = new SimpleDateFormat("yyyy-MM-dd");
			else if (_time.indexOf(":") > 0 && _time.indexOf(":") <= 13) {
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			} else {
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
			date = format.parse(_time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取指定时间的默认格式化字符串(格式："2012-05-10 11:50:50" )
	 * 
	 * @param _calendar
	 * @return
	 */
	public static String getDefaultTime(long time) {
		return getDateTime(DEFAULT_DATATIME_PATTERN, time);
	}

	/**
	 * 获取指定时间的默认格式化字符串(格式："20120510115050" )
	 * 
	 * @param _calendar
	 * @return
	 */
	public static String get_DefaultTime(long time) {
		return getDateTime(DEFAULTPATTERN, time);
	}

	/**
	 * @param _pattern
	 * @param _milliseconds
	 * @return
	 */
	public static String getDateTime(String _pattern, long _milliseconds) {
		return new SimpleDateFormat(_pattern).format(_milliseconds);
	}

	/**
	 * 按照标准时间格式获取毫秒（格式："2012-05-10 11:50:50" ）
	 * 
	 * @param _dateTimeString
	 * @return
	 */
	public static long parseToLong(String _dateTimeString) throws Exception {
		return new SimpleDateFormat(DEFAULT_DATATIME_PATTERN).parse(_dateTimeString).getTime();
	}

	public static Date parseToDateTime(String _dateTimeString) throws Exception {
		return DEFAULT_DATETIME_FORMAT.parse(_dateTimeString);
	}

	public static Date parseToDate(String _dateTimeString) throws Exception {
		return DEFAULT_DATE_FORMAT.parse(_dateTimeString);
	}

	/**
	 * 按照指定时间格式获取毫秒
	 * 
	 * @param _pattern
	 *            时间表达式
	 * @param _dateTimeString
	 *            时间字符串
	 * @return
	 */
	public static long parseToLong(String _pattern, String _dateTimeString) throws Exception {
		return new SimpleDateFormat(_pattern).parse(_dateTimeString).getTime();
	}

	/**
	 * 毫秒时间转换为时间格式字符（格式："2012-05-10 11:50:50" ）
	 * 
	 * @param _milliseconds
	 * @return
	 */
	public static String getYear2Second(long _milliseconds) {
		return new SimpleDateFormat(DEFAULT_DATATIME_PATTERN).format(new Date(_milliseconds));
	}

	/**
	 * 毫秒时间转换为日期格式字符（格式："2012-05-10"）
	 * 
	 * @param _milliseconds
	 * @return
	 */
	public static String getYear2Day(long _milliseconds) {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(_milliseconds));
	}

	/**
	 * 毫秒时间转换为日期格式字符（格式："2012-05-10"）
	 * 
	 * @param _milliseconds
	 * @return
	 * @throws ParseException
	 */
	public static long getYear2Day(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(date).getTime();
	}

	/**
	 * 毫秒时间转换为时间格式字符（格式："05-10 11:50"）
	 * 
	 * @param _milliseconds
	 * @return
	 */
	public static String getMonth2Minute(long _milliseconds) {
		return new SimpleDateFormat("MM-dd HH:mm").format(new Date(_milliseconds));
	}

	/**
	 * 毫秒时间转换为时间格式字符（格式："11:50"）
	 * 
	 * @param _milliseconds
	 * @return
	 */
	public static String getHour2Minute(long _milliseconds) {
		return new SimpleDateFormat("HH:mm").format(new Date(_milliseconds));
	}

	/**
	 * 得到两个日期的间隔天数，若twoDate>oneDate，则返回正数，否则返回负数。
	 * 
	 * @param oneDate
	 *            第一个日期
	 * @param twoDate
	 *            第二个日期
	 * @return 两个日期的间隔天数
	 */
	public static long getGapDays(Date oneDate, Date twoDate) {
		if (oneDate == null || twoDate == null)
			return 0;
		// LogServer.info(DateFormat.class,twoDate.getTime()/1000/60/60/24 + "-"
		// + oneDate.getTime()/1000/60/60/24 + "=" +
		// (twoDate.getTime()/1000/60/60/24 - oneDate.getTime()/1000/60/60/24));
		return (twoDate.getTime() / 1000 / 60 / 60 / 24 - oneDate.getTime() / 1000 / 60 / 60 / 24);
	}

	public static boolean getBeforDay(Long one) {
		Calendar mCalendar = Calendar.getInstance();
		long currTime = System.currentTimeMillis();
		mCalendar.setTime(new Date(currTime));
		int year = mCalendar.get(Calendar.YEAR);
		int month = mCalendar.get(Calendar.MONTH);
		int day = mCalendar.get(Calendar.DAY_OF_MONTH);
		mCalendar.set(year, month, day, 0, 0, 0);
		if (one < mCalendar.getTimeInMillis())
			return true;
		return false;
	}

	public static boolean getBeforTime(Long one) {
		Calendar mCalendar = Calendar.getInstance();
		long currTime = System.currentTimeMillis();
		mCalendar.setTime(new Date(currTime));
		int year = mCalendar.get(Calendar.YEAR);
		int month = mCalendar.get(Calendar.MONTH);
		int day = mCalendar.get(Calendar.DAY_OF_MONTH);
		mCalendar.set(year, month, day, 22, 00, 00);
		if (one < mCalendar.getTimeInMillis() && currTime >= mCalendar.getTimeInMillis())
			return true;
		else {
			mCalendar.set(year, month, day, 0, 0, 0);
			if (one < mCalendar.getTimeInMillis())
				return true;
		}
		return false;
	}

	/**
	 * 判断当前日期是星期几
	 * 
	 * @param date
	 *            日期
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */
	public static int dayForWeek(Date date) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	/**
	 * 获取日期的年月日数字。
	 * 
	 * @param date
	 * @return 19700101
	 */
	public static int getNumTime(Date date) {
		String format = new SimpleDateFormat("yyyyMMdd").format(date);
		return Integer.parseInt(format);
	}

	/**
	 * 实例化时间对象
	 * 
	 * @param date
	 * @return
	 * @throws NullPointerException
	 */
	public static Calendar toCalendar(Date date) throws NullPointerException {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}

	public static long dateCalculation(long data, int sub) throws Exception {
		Calendar cal = Calendar.getInstance();
		Date date = new Date(data);
		cal.clear();
		cal.setTime(date);
		cal.add(Calendar.MONTH, sub);
		return cal.getTime().getTime();
	}

	public static long dayCalculation(long data, int sub) throws Exception {
		Calendar cal = Calendar.getInstance();
		Date date = new Date(data);
		cal.clear();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, sub);
		return cal.getTime().getTime();
	}

	public static final long ONE_DAY_TIME = 24 * 60 * 60 * 1000;

	// 某一天的开始时间戳
	public static long somedayStart(long time) {
		Calendar t = Calendar.getInstance();
		t.setTimeInMillis(time);
		t.set(Calendar.HOUR_OF_DAY, 0);
		t.set(Calendar.MINUTE, 0);
		t.set(Calendar.SECOND, 0);
		t.set(Calendar.MILLISECOND, 0);
		return t.getTime().getTime();
	}

	// 从begin到end，是否经过了某天的某些小时点
	public static boolean checkPassTime(long begin, long end, int[] day_time) {
		long dif = end - begin;
		if (dif <= 0)
			return false;

		Calendar begin_c = Calendar.getInstance();
		begin_c.setTimeInMillis(begin);
		Calendar end_c = Calendar.getInstance();
		end_c.setTimeInMillis(end);

		int begin_hour = begin_c.get(Calendar.HOUR_OF_DAY);
		int end_hour = end_c.get(Calendar.HOUR_OF_DAY);
		int begin_day = begin_c.get(Calendar.DAY_OF_YEAR);
		int end_day = end_c.get(Calendar.DAY_OF_YEAR);

		for (int i = 0; i < day_time.length; ++i) {
			int test = day_time[i];
			if (begin_day == end_day) {
				if (end_hour >= test && begin_hour < test) {
					return true;
				}
			} else {
				if (begin_hour <= end_hour) {
					return true;
				} else if (test > begin_hour || test <= end_hour) {
					return true;
				}
			}
		}
		return false;

		/*
		 * long check_day = DateUtil.getGapDays(begin_c.getTime(),
		 * end_c.getTime());
		 * 
		 * long test_begin = begin; for (long i = 0; i <= check_day; ++i) { long
		 * day_start = somedayStart(test_begin); for (int j = 0; j <
		 * day_time.length; ++j) { long check = day_start + day_time[j] * 60 *
		 * 60 * 1000; if (check > begin && end > check) { return true; } }
		 * test_begin += ONE_DAY_TIME; }
		 * 
		 * return false;
		 */
	}
}
