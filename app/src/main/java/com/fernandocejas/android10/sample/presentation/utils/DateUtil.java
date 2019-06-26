package com.fernandocejas.android10.sample.presentation.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期处理工具类.
 */
public class DateUtil {

	/**
	 * 缺省的日期格式
	 */
	public static final String DAFAULT_DATE_FORMAT = "yyyy-M-d";

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * 默认日期类型格试.
	 *
	 */
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(DAFAULT_DATE_FORMAT);

	/**
	 * 缺省的日期时间格式
	 */
	private static final String DAFAULT_DATETIME_FORMAT = "yyyy-M-d HH:mm:ss";

	/**
	 * 时间格式
	 */
	private static String DATETIME_FORMAT = DAFAULT_DATETIME_FORMAT;

	private static SimpleDateFormat datetimeFormat = new SimpleDateFormat(DATETIME_FORMAT);

	/**
	 * 缺省的时间格式
	 */
	private static final String DAFAULT_TIME_FORMAT = "HH:mm:ss";

	/**
	 * 时间格式
	 */
	private static String TIME_FORMAT = DAFAULT_TIME_FORMAT;

	private static SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);

	private DateUtil() {
		// 私用构造主法.因为此类是工具类.
	}

	/**
	 * 获取格式化实例.
	 *
	 * @param pattern
	 *            如果为空使用DAFAULT_DATE_FORMAT
	 * @return
	 */
	public static SimpleDateFormat getFormatInstance(String pattern) {
		if (pattern == null || pattern.length() == 0) {
			pattern = DAFAULT_DATE_FORMAT;
		}
		return new SimpleDateFormat(pattern);
	}

	/**
	 * 格式化Calendar
	 *
	 * @param calendar
	 * @return
	 */
	public static String formatCalendar(Calendar calendar) {
		if (calendar == null) {
			return "";
		}
		return dateFormat.format(calendar.getTime());
	}

	public static String formatDateTime(Date d) {
		if (d == null) {
			return "";
		}
		return datetimeFormat.format(d);
	}

	public static String formatDate(Date d) {
		if (d == null) {
			return "";
		}
		return dateFormat.format(d);
	}

	/**
	 * 格式化时间
	 *
	 * @return
	 */
	public static String formatTime(Date d) {
		if (d == null) {
			return "";
		}
		return timeFormat.format(d);
	}

	/**
	 * 格式化整数型日期
	 *
	 * @param intDate
	 * @return
	 */
	public static String formatIntDate(Integer intDate) {
		if (intDate == null) {
			return "";
		}
		Calendar c = newCalendar(intDate);
		return formatCalendar(c);
	}

	/**
	 * 根据指定格式化来格式日期.
	 *
	 * @param date
	 *            待格式化的日期.
	 * @param pattern
	 *            格式化样式或分格,如yyMMddHHmmss
	 * @return 字符串型日期.
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		if (TextUtils.isEmpty(pattern)) {
			return formatDate(date);
		}
		SimpleDateFormat simpleDateFormat = null;
		try {
			simpleDateFormat = new SimpleDateFormat(pattern);
		} catch (Exception e) {
			e.printStackTrace();
			return formatDate(date);
		}
		return simpleDateFormat.format(date);
	}

	/**
	 * 取得Integer型的当前日期
	 *
	 * @return
	 */
	public static Integer getIntNow() {
		return getIntDate(getNow());
	}

	/**
	 * 取得Integer型的当前日期
	 *
	 * @return
	 */
	public static Integer getIntToday() {
		return getIntDate(getNow());
	}

	/**
	 * 取得Integer型的当前年份
	 *
	 * @return
	 */
	public static Integer getIntYearNow() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 取得Integer型的当前月份
	 *
	 * @return
	 */
	public static Integer getIntMonthNow() {
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH) + 1;
		return month;
	}

	public static String getStringToday() {
		return getIntDate(getNow()) + "";
	}

	/**
	 * 根据年月日获取整型日期
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Integer getIntDate(int year, int month, int day) {
		return getIntDate(newCalendar(year, month, day));
	}

	/**
	 * 某年月的第一天
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	public static Integer getFirstDayOfMonth(int year, int month) {
		return getIntDate(newCalendar(year, month, 1));
	}

	/**
	 * 某年月的第一天
	 *
	 * @return
	 */
	public static Integer getFirstDayOfThisMonth() {
		Integer year = DateUtil.getIntYearNow();
		Integer month = DateUtil.getIntMonthNow();
		return getIntDate(newCalendar(year, month, 1));
	}

	/**
	 * 某年月的第一天
	 *
	 * @param date
	 * @return
	 * @time:2008-7-4 上午09:58:55
	 */
	public static Integer getFistDayOfMonth(Date date) {
		Integer intDate = getIntDate(date);
		int year = intDate / 10000;
		int month = intDate % 10000 / 100;
		return getIntDate(newCalendar(year, month, 1));
	}

	/**
	 * 某年月的最后一天
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	public static Integer getLastDayOfMonth(int year, int month) {
		return intDateSub(getIntDate(newCalendar(year, month + 1, 1)), 1);
	}

	/**
	 * 根据Calendar获取整型年份
	 *
	 * @param c
	 * @return
	 */
	public static Integer getIntYear(Calendar c) {
		int year = c.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 根据Calendar获取整型日期
	 *
	 * @param c
	 * @return
	 */
	public static Integer getIntDate(Calendar c) {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		return year * 10000 + month * 100 + day;
	}

	/**
	 * 根据Date获取整型年份
	 *
	 * @param d
	 * @return
	 */
	public static Integer getIntYear(Date d) {
		if (d == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return getIntYear(c);
	}

	/**
	 * 根据Date获取整型日期
	 *
	 * @param d
	 * @return
	 */
	public static Integer getIntDate(Date d) {
		if (d == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return getIntDate(c);
	}

	/**
	 * 根据Integer获取Date日期
	 *
	 * @param n
	 * @return
	 */
	public static Date getDate(Integer n) {
		if (n == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.set(n / 10000, n / 100 % 100 - 1, n % 100);
		return c.getTime();
	}

	public static Date getDate(String date) {
		if (date == null || date.length() == 0) {
			return null;
		}

		try {
			if (date.contains("/")) {
				date = date.replaceAll("/", "-");
			}
			return getFormatInstance(DATE_FORMAT).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据年份Integer获取Date日期
	 *
	 * @param year
	 * @return
	 */
	public static Date getFirstDayOfYear(Integer year) {
		if (year == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.set(year, 1, 1);
		return c.getTime();
	}

	/**
	 * 根据年月日生成Calendar
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Calendar newCalendar(int year, int month, int day) {
		Calendar ret = Calendar.getInstance();
		if (year < 100) {
			year = 2000 + year;
		}
		ret.set(year, month - 1, day);
		return ret;
	}

	/**
	 * 根据整型日期生成Calendar
	 *
	 * @param date
	 * @return
	 */
	public static Calendar newCalendar(int date) {
		int year = date / 10000;
		int month = (date % 10000) / 100;
		int day = date % 100;

		Calendar ret = Calendar.getInstance();
		ret.set(year, month - 1, day);
		return ret;
	}

	/**
	 * 取得Date型的当前日期
	 *
	 * @return
	 */
	public static Date getNow() {
		return new Date();
	}

	/**
	 * 取得Date型的当前日期
	 *
	 * @return
	 */
	public static Date getToday() {
		return DateUtil.getDate(DateUtil.getIntToday());
	}

	/**
	 * 整数型日期的加法
	 *
	 * @param date
	 * @param days
	 * @return
	 */
	public static Integer intDateAdd(int date, int days) {
		int year = date / 10000;
		int month = (date % 10000) / 100;
		int day = date % 100;

		day += days;

		return getIntDate(year, month, day);
	}

	/**
	 * 整数型日期的减法
	 *
	 * @param date
	 * @param days
	 * @return
	 */
	public static Integer intDateSub(int date, int days) {
		return intDateAdd(date, -days);
	}

	/**
	 * 计算两个整型日期之间的天数
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Integer daysBetweenDate(Integer startDate, Integer endDate) {
		if (startDate == null || endDate == null) {
			return null;
		}
		Calendar c1 = newCalendar(startDate);
		Calendar c2 = newCalendar(endDate);

		Long lg = (c2.getTimeInMillis() - c1.getTimeInMillis()) / 1000 / 60 / 60 / 24;
		return lg.intValue();
	}

	/**
	 * 计算两个整型日期之间的天数
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Integer daysBetweenDate(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return null;
		}
		Long interval = endDate.getTime() - startDate.getTime();
		interval = interval / (24 * 60 * 60 * 1000);
		return interval.intValue();
	}

	/**
	 * 取得当前日期.
	 *
	 * @return 当前日期,字符串类型.
	 */
	public static String getStringDate() {
		return getStringDate(DateUtil.getNow());
	}

	/**
	 * 根据calendar产生字符串型日期
	 *
	 * @param d
	 * @return eg:20080707
	 */
	public static String getStringDate(Date d) {
		if (d == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(d);
	}

	public static String getStringMonth(Date d) {
		if (d == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(d);
	}

	public static String getStringDateyyyyMM(Date d) {
		if (d == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		return sdf.format(d);
	}

	public static String getFormatStringDate(Date d) {
		if (d == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		return sdf.format(d);
	}


	public static String getLastMonthStringDate() {
		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.DATE, -1);    //得到前一天
		calendar.add(Calendar.MONTH, -1);    //得到前一个月
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH)+1;

		return getStringDate(calendar.getTime());
	}

	/**
	 * 获取过去三个月份日期值
	 * @return
	 */
	public static List<String> getLastThreeMonth() {
		List<String> result = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		for (int i = 1; i < 4; i++) {
			calendar.add(Calendar.MONTH, -i);
			result.add(getStringMonth(calendar.getTime()));
			calendar.add(Calendar.MONTH, i);
		}
		return result;
	}

	/**
	 * 获取过去三个月份日期值
	 * @return
	 */
	public static List<String> getLastThreeMonthyyyyMM() {
		List<String> result = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		for (int i = 1; i < 4; i++) {
			calendar.add(Calendar.MONTH, -i);
			result.add(getStringDateyyyyMM(calendar.getTime()));
			calendar.add(Calendar.MONTH, i);
		}
		return result;
	}

	/**
	 * yyyyMM to yyyy年MM月
	 * @param date 201402
	 * @return 2014年02月
	 */
	public static String parseStringDateYearMonth(String date) {
		if(TextUtils.isEmpty(date))return "";
		if(date.length() != 6)return date;
		String year = date.substring(0, 4) + "年";
		String month = date.substring(4) + "月";
		return year + month;
	}

  /**
  * 将传入时间与当前时间进行对比，是否今天\昨天\前天\同一年
  *
  * @param
  * @return
  */
  public static String convertDateToDiaplayTime(Date date) {
		boolean sameYear = false;
		String todySDF = "HH:mm";
		String todaySDF = "今天";
		String yesterDaySDF = "昨天";
		String beforYesterDaySDF = "前天";
		String otherSDF = "HH:mm";
		String otherYearSDF = "yyyy-MM-dd";
		SimpleDateFormat sfd = null;
		String time = "";
		Calendar dateCalendar = Calendar.getInstance();
		dateCalendar.setTime(date);
		Date now = new Date();
		Calendar todayCalendar = Calendar.getInstance();
		todayCalendar.setTime(now);
		todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
		todayCalendar.set(Calendar.MINUTE, 0);

		if (dateCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
			sameYear = true;
		} else {
			sameYear = false;
		}

		if (dateCalendar.after(todayCalendar)) {// 判断是不是今天
			/*sfd = new SimpleDateFormat(todySDF);
			time = sfd.format(date);
			return time;*/
			sfd = new SimpleDateFormat(otherSDF);
			time = todaySDF+" "+sfd.format(date);
			return time;
		} else {
			todayCalendar.add(Calendar.DATE, -1);
			if (dateCalendar.after(todayCalendar)) {// 判断是不是昨天
				// sfd = new SimpleDateFormat(yesterDaySDF);
				// time = sfd.format(date);
				sfd = new SimpleDateFormat(otherSDF);
				time = yesterDaySDF+" "+sfd.format(date);
				return time;
			}
			/*todayCalendar.add(Calendar.DATE, -2);
			if (dateCalendar.after(todayCalendar)) {// 判断是不是前天
				// sfd = new SimpleDateFormat(beforYesterDaySDF);
				// time = sfd.format(date);
				time = beforYesterDaySDF;
				return time;
			}*/
		}

		if (sameYear) {
			/*sfd = new SimpleDateFormat(otherSDF);*/
			sfd = new SimpleDateFormat(otherYearSDF);
			time = sfd.format(date);
		} else {
			sfd = new SimpleDateFormat(otherYearSDF);
			time = sfd.format(date);
		}

		return time;
  }

  /**
  * 将传入时间与当前时间进行对比，是否今天 11:30\昨天 11:30 \前天 11:30 \同一年
  *
  * @param
  * @return
  */
  public static String convertDateToDiaplayTimeForOnLineTime(Date date) {
		boolean sameYear = false;
		String todySDF = "HH:mm";
		String todaySDF = "今天";
		String yesterDaySDF = "昨天";
		String beforYesterDaySDF = "前天";
		String otherSDF = "HH:mm";
		String otherYearSDF = "yyyy-MM-dd";
		SimpleDateFormat sfd = null;
		String time = "";
		Calendar dateCalendar = Calendar.getInstance();
		dateCalendar.setTime(date);
		Date now = new Date();
		Calendar todayCalendar = Calendar.getInstance();
		todayCalendar.setTime(now);
		todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
		todayCalendar.set(Calendar.MINUTE, 0);

		if (dateCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
			sameYear = true;
		} else {
			sameYear = false;
		}

		if (dateCalendar.after(todayCalendar)) {// 判断是不是今天
			/*sfd = new SimpleDateFormat(todySDF);
			time = sfd.format(date);
			return time;*/

			sfd = new SimpleDateFormat(otherSDF);
			time = todaySDF+" "+sfd.format(date);
			return time;
		} else {
			todayCalendar.add(Calendar.DATE, -1);
			if (dateCalendar.after(todayCalendar)) {// 判断是不是昨天
				// sfd = new SimpleDateFormat(yesterDaySDF);
				// time = sfd.format(date);

				sfd = new SimpleDateFormat(otherSDF);
				time = yesterDaySDF+" "+sfd.format(date);
				return time;
			}
		/*	todayCalendar.add(Calendar.DATE, -2);
			if (dateCalendar.after(todayCalendar)) {// 判断是不是前天
				// sfd = new SimpleDateFormat(beforYesterDaySDF);
				// time = sfd.format(date);

				sfd = new SimpleDateFormat(otherSDF);
				time = beforYesterDaySDF+" "+sfd.format(date);

				return time;
			}*/
		}

		if (sameYear) {
			sfd = new SimpleDateFormat(otherYearSDF);
			time = sfd.format(date);
		} else {
			sfd = new SimpleDateFormat(otherYearSDF);
			time = sfd.format(date);
		}

		return time;
  }

}
