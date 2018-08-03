/**
 * @Title: TimeUtil.java
 * @Package com.cpsdna.libs.util
 * @Description: TODO
 */
package com.camelbell.jobrecord.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description: 时间处理Util类
 * @author wangwenbin
 * @date 2013-3-8 下午12:00:38
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

	public static final String FORMAT_DATA = "yyyy-MM-dd";
	public static final String FORMAT_TIME = "HH:mm:ss";
	public static final String FORMAT_DATA_TIME = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_DATA_TIME_1 = "MM-dd HH:mm";
	public static final String FORMAT_DATA_TIME_2 = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_DATA_TIME_3 = "HH:mm";
	public static final String FORMAT_DATA_TIME_4 = "MM-dd HH:mm";

	/**
	 * @Description: 获取当前时间
	 * @param pattern
	 *            {@link #FORMAT_DATA} {@link #FORMAT_TIME}
	 *            {@link #FORMAT_DATA_TIME}
	 * @return String 格式化时间String
	 */
	public static String getNowTime(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	/**
	 * @Description: 格式化时间
	 * @param time
	 * @param pattern
	 * @return Date
	 */
	public static Date formatTimeToDate(String time, String pattern) {
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat(pattern);

		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	/**
	 * @param time
	 * @param pattern
	 * @return String
	 */
	public static String formatTime(String time, String pattern) {
		if (TextUtils.isEmpty(time)) {
			return time;
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);

		try {
			Date date = format.parse(time);
			return format.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return time;
		}
	}

	/**
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param min
	 * @return String
	 */
	public static String formatTime(int year, int month, int day, int hour,
			int min) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, hour, min, 0);
		Date date = calendar.getTime();
		return dateFormat.format(date);
	}

	/**
	 * @Description: 比较时间前后
	 * @param time1
	 * @param time2
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean compareTime(String time1, String time2, int i) {
		if (i == 0) {
			return compareTime(time1, time2, "yyyy-MM-dd");
		} else {
			return compareTime(time1, time2, "yyyy-MM");
		}
	}

	public static boolean compareDate(String time1, String time2) {
		return compareTime(time1, time2, "yyyy-MM-dd HH:mm");
	}

	public static boolean compareTime(String time1, String time2, String pattern) {
		if (time1 == null || time2 == null) {
			return false;
		}
		time1 = time1.trim();
		time2 = time2.trim();
		SimpleDateFormat formater = new SimpleDateFormat(pattern);
		try {
			if (formater.parse(time1).getTime() > formater.parse(time2)
					.getTime()) {
				return true;

			} else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 格式化月份
	public static String formatMonth(int year, int month, int day) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		Date date = calendar.getTime();
		return dateFormat.format(date);
	}

	public static int getNowYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	// -1的month
	public static int getNowMonth() {
		return Calendar.getInstance().get(Calendar.MONTH);
	}

	public static int getNowDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	public static String getToday() {
		return formatDate(getNowYear(), getNowMonth(), getNowDay());
	}

	public static String formatDate(int year, int month, int day) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		Date date = calendar.getTime();
		return dateFormat.format(date);
	}

	public static String formatTime1(int year, int month, int day, int hour,
			int min) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, hour, min, 0);
		Date date = calendar.getTime();
		return dateFormat.format(date);
	}

	public static String formatDate(String time) {
		if ("".equals(time)) {
			return time;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(time);
			String s = sdf.format(date);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return time;
		}

	}
	

	public static String formatDetailDate(long time) {
		String backTime = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = new Date(time);
			backTime = sdf.format(date);

			return backTime;
		} catch (Exception e) {
			e.printStackTrace();
			return backTime;
		}

	}

	// 获取几天后日期
	public static String afterFormatDate(int num) {
		Date d = new Date();
		SimpleDateFormat df = new SimpleDateFormat(FORMAT_DATA);
		return df.format(new Date(d.getTime() + num * 24 * 60 * 60 * 1000));
	}

	public static String[] TIMEFORMARTS = new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm"};


}
