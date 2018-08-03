package com.camelbell.jobrecord.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public static String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        return mDateFormat.format(new Date(time));
    }
}
