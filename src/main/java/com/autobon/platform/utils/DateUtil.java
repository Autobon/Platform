package com.autobon.platform.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DateUtil extends WjafDateUtil {

	private static SimpleDateFormat year_format = new SimpleDateFormat("yyyy");

	private static SimpleDateFormat default_format = new SimpleDateFormat(
			"yyyy-MM-dd");

	private static SimpleDateFormat time_format = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	protected static Log log = LogFactory.getLog(DateUtil.class);
	/**
	 * 得到当前时间,格式为yyyyMMddhhmmss
	 * 
	 * @return String
	 */
	public static String generateTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		return format.format(new Date());
	}
	/**
	 * 根据格式生成当前日期时间
	 * @param formatStr
	 * @return
	 */
	public static String generateDateTime(String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(new Date());
	}

	/**
	 * 根据yyyy-MM-dd得到月份
	 * 
	 * @param dateString
	 *            String
	 * @return int
	 */
	public static int getMonthFromYear(String dateString) {
		Date date = string2Date(dateString);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 得到指定年的所有天数
	 * 
	 * @param year
	 *            String
	 * @return day int
	 */
	public static int getDayFromYear(String year) {
		Date date;
		int day = 0;
		try {
			date = year_format.parse(year);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			day = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
		} catch (ParseException e) {
			log.error("",e);
		}
		return day;
	}

	/**
	 * 返回下一年_格式yyyy
	 * 
	 * @return int
	 */
	public static int getNextYear() {
		return new GregorianCalendar().get(Calendar.YEAR) + 1;
	}

	/**
	 * 获取某年第一天日期_格式yyyy-MM-dd
	 * 
	 * @param year
	 *            年份
	 * @return String
	 */
	public static String getCurrYearFirstDay(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return default_format.format(currYearFirst);
	}

	/**
	 * 根据一个日期，返回是星期几的数字_星期天:1,星期一:2....星期六:7
	 * 
	 * @param dateString
	 *            String
	 * @return int
	 */
	public static int getWeek(String dateString) {
		Date date = string2Date(dateString);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 根据一个日期,传入指定天数,想要返回长度,得到String
	 * 
	 * @param inDate
	 *            日期
	 * @param days
	 *            天数
	 * @param _iType
	 *            inDate长度
	 * @return String
	 */
	public static String getDateByAddDays(String inDate, int days, int _iType) {
		Date dateStr = string2Date(inDate);
		Date tempDate = getDateByAddDays(dateStr, days);
		return date2String(tempDate, _iType);
	}

	/**
	 * 根据Date日期,传入返回长度,得到String
	 * 
	 * @param date
	 *            Date
	 * @param _iType
	 *            返回String长度
	 * @return String
	 */
	public static String date2String(Date date, int _iType) {
		String strTemp = time_format.format(date);
		SimpleDateFormat formatter = null;
		switch (_iType) {
		case 0: // yyyymmdd
			strTemp = strTemp.substring(0, 8);
			break;
		case 4://yyyy
			strTemp = strTemp.substring(0, 4);
			break;
		case 6: // yymmdd
			strTemp = strTemp.substring(2, 8);
			break;
		case 8: // yyyymmdd
			strTemp = strTemp.substring(0, 8);
			break;
		case 10: // yyyy-mm-dd
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			strTemp = formatter.format(date);
			break;
		case -6: // HHmmss
			strTemp = strTemp.substring(8);
			break;
		case -8: // HH:mm:ss
			formatter = new SimpleDateFormat("HH:mm:ss");
			strTemp = formatter.format(date);
			break;
		default:
			break;
		}
		return strTemp;
	}

	/**
	 * 获得指定日期前后的日期，返回日期型值
	 * 
	 * @param inDate
	 *            指定的日期
	 * @param days
	 *            加减天数
	 * @return Date
	 * 
	 */
	public static Date getDateByAddDays(Date inDate, int days) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(inDate);
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}

	/**
	 * 将日期字符串转换成日期型，日期格式为"yyyy-MM-dd"
	 * 
	 * @param dateString
	 *            指定的日期字符串，格式为yyyyMMdd 或者yyyy-MM-dd
	 * @return Date
	 * @author lijunchen
	 */
	public final static Date string2Date(String dateString) {
		if (dateString == null || dateString.trim().length() == 0) {
			return new Date(0);
		}
		try {
			String strFormat = "";
			switch (dateString.length()) {
			case 4:
				strFormat = "yyyy";
				break;
			case 6: // yymmdd
				strFormat = "yyMMdd";
				break;
			case 8: // yyyymmdd
				strFormat = "yyyyMMdd";
				break;
			case 10: // yyyy-mm-dd
				strFormat = "yyyy-MM-dd";
				break;
			case 14:
				strFormat = "yyyyMMddHHmmss";
				break;
			default:
				strFormat = "yyyy-MM-dd HH:mm:ss";
			}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormat);
			// 这里没有做非法日期的判断，比如：＂2007-05-33＂
			Date timeDate = simpleDateFormat.parse(dateString);
			return timeDate;
		} catch (Exception e) {
			return new Date(0);
		}
	}
	/**
	 * 得到两个时间的差值，单位是小时
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static double getHourBetweenDates(Date beginDate,Date endDate){
		
		long l1 = endDate.getTime();
		long l2 = beginDate.getTime();
		
		double cc =l1 - l2;
		return cc/(60*60*1000);
	}
	/**
	 * 得到两个时间的差值，单位是分钟
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static double getMinuteBetweenDates(Date beginDate,Date endDate){
		
		double cc =endDate.getTime() - beginDate.getTime();
		return cc/(60*1000);
	}

}
