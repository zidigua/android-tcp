package com.threepapa.vmtcp.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 类描述：转换时间的工具类
 * 
 * @author huangchao
 * @since 1.0
 */
public class DateUtil {

	/**
	 * 获取当前时间戳
	 * 
	 * @author huangchao
	 * @return
	 */
	public static long getCurrentDate() {
		Date d = new Date();
		return d.getTime();
	}

	/**
	 * 把格式是2012-12-02的日期串转为Date类型
	 * 
	 * @author huangchao
	 * @param strDate
	 * @return Date
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 把时间戳时间转换成yyyy年MM月dd日形式
	 * 
	 * @author huangchao
	 * @param gtm
	 * @return strGtm
	 */
	public static String dateToStr(long gtm) {
		Date date = new Date(gtm);
		SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
		String strGtm = formatter.format(date);
		return strGtm;
	}
}
