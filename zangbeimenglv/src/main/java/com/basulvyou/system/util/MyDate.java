package com.basulvyou.system.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具栏
 *
 */
public class MyDate {

	public final static String FORMAT_DATE = "yyyy-MM-dd";
	public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日 hh:mm";
	public final static String FORMAT_DATE_TIME = "yyyy-MM-dd hh:mm";
	private static SimpleDateFormat sdf = new SimpleDateFormat();
	
	public static String getDateEN() {  
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");  
        String date1 = format1.format(new Date(System.currentTimeMillis()));  
        return date1;// 2012-10-03 23:41:31  
    }
	
	/**
	 * 根据时间戳获取指定格式的时间，如2011-11-30 08:40
	 * 
	 * @param timestamp
	 *            时间戳 单位为毫秒
	 * @param format
	 *            指定格式 如果为null或空串则使用默认格式"yyyy-MM-dd HH:MM"
	 * @return
	 */
	public static String getFormatTimeFromTimestamp(long timestamp,
			String format)
	{
		if (format == null || format.trim().equals(""))
		{
			sdf.applyPattern(FORMAT_DATE);
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			int year = Integer.valueOf(sdf.format(new Date(timestamp))
					.substring(0, 4));
			if (currentYear == year)
			{// 如果为今年则不显示年份
				sdf.applyPattern(FORMAT_MONTH_DAY_TIME);
			}
			else
			{
				sdf.applyPattern(FORMAT_DATE_TIME);
			}
		}
		else
		{
			sdf.applyPattern(format);
		}
		Date date = new Date(timestamp);
		return sdf.format(date);
	}
	
	/**
	 * 把秒转换成 日:时:分:秒
	 * @param ms 秒
	 * @return
	 */
	public static int[] formatDuring(long ms) {
		int endTime[] = new int[4];
		long mss = ms * 1000;
		int days = (int) (mss / (1000 * 60 * 60 * 24));
		int hours = (int) ((mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
		int minutes = (int) ((mss % (1000 * 60 * 60)) / (1000 * 60));
		int seconds = (int) ((mss % (1000 * 60)) / 1000);
		endTime[0] = days;
		endTime[1] = hours;
		endTime[2] = minutes;
		endTime[3] = seconds;
		return endTime;
	}
}
