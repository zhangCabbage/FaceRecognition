package com.framework.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.beanutils.Converter;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

/**
 * 时间处理的管理类<br>
 * <p>
 * 该类为一工具类，实现对时间的格式化等操作
 * </P>

 */
public class DateUtils implements Converter {
	/**
	 * 时间格式 : yyyy-MM-dd
	 */
	public final static String US_DATE_STYLE = "yyyy-MM-dd";
	/**
	 * 时间格式 : yyyy-MM-dd HH:mm:ss
	 */
	public final static String LONG_DATE_STYLE = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 时间格式 : yyyy-MM-dd HH:mm
	 */
	public final static String MIDDLE_DATE_STYLE = "yyyy-MM-dd HH:mm";
	/**
	 * 时间格式 : yyyyMMdd
	 */
	public final static String DATE_NODIVISION = "yyyyMMdd";

	/**
	 * 比较两个字符串类型时间并且返回分钟差<br>
	 * @param s1 时间串1
	 * @param s2 时间串2
	 * @return int
	 */
	public static int compareDateAndReturnHM(String s1, String s2) throws Exception {
		return compareDateAndReturnHM(convertStringDateTimeT(s1), convertStringDateTimeT(s2));
	}

	/**
	 * 比较两个时间并且返回分钟差<br>
	 * @param t1 时间1
	 * @param t2 时间2
	 * @return int
	 */
	public static int compareDateAndReturnHM(Timestamp t1, Timestamp t2) {
		try {
			long daterange = t2.getTime() - t1.getTime();
			long time = 1000 * 60;
			if (daterange % time == 0)
				return new Float(daterange / time).intValue();
			return new Float(daterange / time + 1).intValue();
		} catch (Exception e) {}
		return -1;
	}

	/**
	 * 把String类型的时间转换成Timestamp类型<br>
	 * <P>
	 * 根据时间串的长度转换为对应的Timestamp 时间
	 * </P>
	 * @param s 时间串
	 * @return Timestamp
	 */
	public static Timestamp convertStringDateTimeT(String s) {
		s.trim();
		if (s != null && s.length() > 0) {
			switch (s.length()) {
			case 7:
				return Timestamp.valueOf(s + "-01 00:00:00");
			case 10:
				return Timestamp.valueOf(s + " 00:00:00");
			case 13:
				return Timestamp.valueOf(s + ":00:00");
			case 16:
				return Timestamp.valueOf(s + ":00");
			case 19:
				return Timestamp.valueOf(s);
			default:
				return Timestamp.valueOf(s);
			}
		}
		System.err.print("invalid date or date time fromat:" + s.trim() + ", return null!");
		return null;
	}

	/**
	 * 把时间转换成如 "yyyy-MM-dd"格式的字符串
	 * @param date 目标类
	 * @exception throws
	 * @return 如 "yyyy-MM-dd"格式的时间字符串
	 */
	public static String conventDateToString(Date date) throws Exception {
		SimpleDateFormat s = new SimpleDateFormat(US_DATE_STYLE, Locale.US);
		return s.format(date);
	}

	/**
	 * 把时间转换成如 "yyyy-MM-dd HH:mm"格式的字符串
	 * @param date 目标类
	 * @exception throws
	 * @return 如 "yyyy-MM-dd HH:mm"格式的时间字符串
	 */
	public static String conventMiddleDateToString(Date date) throws Exception {
		SimpleDateFormat s = new SimpleDateFormat(MIDDLE_DATE_STYLE, Locale.CHINESE);
		return s.format(date);
	}

	/**
	 * 把时间转换成如 "yyyy-MM-dd HH:mm:ss"格式的字符串
	 * @param date 目标类
	 * @exception throws
	 * @return 如 "yyyy-MM-dd HH:mm:ss"格式的时间字符串
	 */
	public static String conventLongDateToString(Date date) throws Exception {
		SimpleDateFormat s = new SimpleDateFormat(LONG_DATE_STYLE, Locale.CHINESE);
		return s.format(date);
	}

	/**
	 * 把时间转换成如 "XX月XX日XX时XX分" 格式的字符串
	 * @param date 目标类
	 * @exception throws
	 * @return 如 "XX月XX日XX时XX分"格式的时间字符串
	 */
	public static String conventDateToChinaString(String date) throws Exception {
		Timestamp dateTime = Timestamp.valueOf(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateTime.getTime());
		String mon = calendar.get(Calendar.MONTH) + 1 >= 10 ? (calendar.get(Calendar.MONTH) + 1 + "") : ("0" + (calendar.get(Calendar.MONTH) + 1));
		String day = calendar.get(Calendar.DAY_OF_MONTH) >= 10 ? (calendar.get(Calendar.DAY_OF_MONTH) + "") : ("0" + calendar.get(Calendar.DAY_OF_MONTH));
		String hour = calendar.get(Calendar.HOUR_OF_DAY) >= 10 ? (calendar.get(Calendar.HOUR_OF_DAY) + "") : ("0" + calendar.get(Calendar.HOUR_OF_DAY));
		String min = calendar.get(Calendar.MINUTE) >= 10 ? (calendar.get(Calendar.MINUTE) + "") : ("0" + calendar.get(Calendar.MINUTE));
		return mon + "月" + day + "日" + hour + "时" + min + "分";
	}

	/**
	 * 把时间转换成传入参数格式的字符串
	 * @param date 目标类
	 * @param dateTyle 时间格式
	 * @exception throws
	 * @return 时间字符串
	 */
	public static String coventDateToString(Date date, String dateTyle) throws Exception {
		SimpleDateFormat s = new SimpleDateFormat(dateTyle);
		return s.format(date);
	}

	/**
	 * 把时间转换成如"yyyy-MM-dd HH:mm:ss"格式的字符串
	 * @param timeStamp 目标类
	 * @exception throws
	 * @return 如"yyyy-MM-dd HH:mm:ss"格式的时间字符串
	 */
	public static String coventTimestampToString(Timestamp timeStamp) throws Exception {
		SimpleDateFormat s = new SimpleDateFormat(LONG_DATE_STYLE);
		return s.format(timeStamp);
	}

	/**
	 * 把时间转换成传入参数格式的字符串
	 * @param timeStamp 目标类
	 * @param dateStyle 时间格式
	 * @exception throws
	 * @return 时间字符串
	 */
	public static String coventDateToString(Timestamp timeStamp, String dateStyle) throws Exception {
		SimpleDateFormat s = new SimpleDateFormat(dateStyle);
		return s.format(timeStamp);
	}

	/**
	 * 时间各部分数量增加
	 * @param type 时间field
	 * @param amount 增加的数量
	 * @param date 目标时间
	 * @return Date 增加运算后的时间
	 */
	public static Date add(int type, int amount, Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(type, amount);
		return c.getTime();
	}

	/**
	 * 获得当前时间的下一天（北京时间）格式：“yyyy-MM-dd”
	 * @return 当前时间的下一天（北京时间）
	 */
	public static Timestamp getNextDateTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new java.util.Date());
		c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);
		String date = df.format(c.getTime());
		return convertStringDateTimeT(date);
	}






	/**
	 * 获取系统当前时间 格式：“yyyy-MM-dd HH:mm:ss”
	 * @return Current DateTime Timestamp
	 */
	public static Timestamp getCurrentDateTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(new java.util.Date());
		String date = df.format(c.getTime());
		return convertStringDateTimeT(date);
	}
	
	/**
	 * 获取当前系统时间   update by dranson on 2012-02-06
	 * @return
	 */
	public static Timestamp getCurrent() {
		Calendar c = Calendar.getInstance();
		c.setTime(new java.util.Date());
		c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) - 8);
		return new Timestamp(c.getTimeInMillis());
	}

	/**
	 * 根据指定时间获取 "yyyy-MM-dd HH:mm:ss" 格式的Timestamp 时间
	 * @param time long类型的时间
	 * @return Timestamp
	 */
	public static Timestamp getDateTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = df.format(new java.util.Date(time));
		return convertStringDateTimeT(date);
	}

	/**
	 * 获取当前时间的前一天的时间
	 * @return Timestamp
	 */
	public static Timestamp getBeforeDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new java.util.Date());
		c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - 1);
		String date = df.format(c.getTime());
		return convertStringDateTimeT(date);
	}

	/**
	 * 给定毫秒数，按照既定的格式格式化
	 * @param milliSecond
	 * @param dateStyle 时间格式
	 * @return 格式化字符串
	 */
	public static String getFormatDateStringByDate(long milliSecond, String dateStyle) {
		Date date = new Date(milliSecond);
		SimpleDateFormat s = new SimpleDateFormat(dateStyle);
		String formatDate = null;

		try {
			formatDate = s.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formatDate;
	}

	/**
	 * 按照既定的格式取得当前日期的格式化字符串
	 * @param dateStyle 时间格式
	 * @return 格式化字符串
	 */
	public static String getFormatDateStringByCurDate(String dateStyle) {
		Calendar c = Calendar.getInstance();

		return DateUtils.getFormatDateStringByDate(c.getTimeInMillis(), dateStyle);
	}

	/**
	 * 给定日期或时间，按照既定的格式格式化日期
	 * @param date
	 * @param dateStyle 时间格式
	 * @return 格式化字符串
	 */
	public static String getFormatDateStringByDate(Date date, String dateStyle) {
		SimpleDateFormat s = new SimpleDateFormat(dateStyle);
		String formatDate = null;

		try {
			formatDate = s.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formatDate;
	}

	/**
	 * 给定日期，按照既定的格式格式化日期
	 * @param year 年份
	 * @param month 月份 0-11
	 * @param day 日期 1-31
	 * @param dateStyle 时间格式
	 * @return 格式化字符串
	 */
	public static String getFormatDateStringByIntegerData(int year, int month, int day, String dateStyle) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);

		return DateUtils.getFormatDateStringByDate(c.getTimeInMillis(), dateStyle);
	}



	/**
	 * @param 使用给定的时间表达式格式化当前时间
	 * @return Timestamp
	 * @exception IllegalArgumentException 给定的表达式无效
	 * @exception NullPointerException 给定模式或表达式为空
	 */
	public static Timestamp getCurrentDateTimeByPattern(String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		Calendar c = Calendar.getInstance();
		c.setTime(new java.util.Date());
		String date = df.format(c.getTime());

		Timestamp time = null;
		try {
			time = new Timestamp(df.parse(date).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 将yyyy-MM-ddTHH:mm:ss 转成 yyyy-MM-dd HH:mm:ss<br>
	 * @param s
	 * @return String
	 */
	public static String formatTimeA(String s) {
		return s.substring(0, 10) + " " + s.substring(11, 19);
	}

	/**
	 * 将yyyy-MM-dd HH:mm:ss 转成 yyyy-MM-ddTHH:mm:ss<br>
	 * @param s
	 * @return String
	 */
	public static String formatTimeB(String s) {
		return s.substring(0, 10) + "T" + s.substring(11, 19);
	}

	@SuppressWarnings("rawtypes")
	public Object convert(Class arg0, Object arg1) {
		if (arg1 instanceof XMLGregorianCalendar) {
			return this.convert(arg0, (XMLGregorianCalendar) arg1);
		} else {
			return this.convert(arg0, (Calendar) arg1);
		}
	}
 
	public Calendar convert(Class<?> calendarType, XMLGregorianCalendar xmlCalendar) {
		return xmlCalendar.toGregorianCalendar();
	}

	public XMLGregorianCalendar convert(Class<?> xmlCalendarType, Calendar calendar) {
		XMLGregorianCalendar cal = new XMLGregorianCalendarImpl();
		cal.setYear(calendar.get(Calendar.YEAR));
		cal.setMonth(calendar.get(Calendar.MONTH) + 1);
		cal.setDay(calendar.get(Calendar.DAY_OF_MONTH));
		cal.setHour(calendar.get(Calendar.HOUR_OF_DAY));
		cal.setMinute(calendar.get(Calendar.MINUTE));
		cal.setSecond(calendar.get(Calendar.SECOND));
		cal.setMillisecond(calendar.get(Calendar.MILLISECOND));
		cal.setTimezone(calendar.get(Calendar.ZONE_OFFSET) / 60000);
		return cal;
	}

	/**
	 * 将字符串转换为时间
	 * @param s
	 * @return Date
	 */
	public static Date parseDate(String s) {
		return DatatypeConverter.parseDate(s).getTime();
	}

	/**
	 * 将时间转换为 yyyy-MM-ddTHH:mm:ss 格式
	 * @param dt
	 * @return String
	 */
	public static String parseStr(Date dt) {
		SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");
		String a1 = dateformat1.format(dt);

		SimpleDateFormat dateformat2 = new SimpleDateFormat("HH:mm:ss");
		String a2 = dateformat2.format(dt);
		return a1 + "T" + a2;
	}

	/**
	 * 将时间转换为 yyyy-MM-dd HH:mm:ss 格式
	 * @param dt
	 * @return String
	 */
	public static String parseStr2(Date dt) {
		SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String a1 = dateformat1.format(dt);
		return a1;
	}

	/**
	 * 将时间转换为 yyyy-MM-ddTHH:mm:ss 格式
	 * @param dt
	 * @return String
	 */
	public static String printDate(Date dt) {
		SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");
		String a1 = dateformat1.format(dt);

		SimpleDateFormat dateformat2 = new SimpleDateFormat("HH:mm:ss");
		String a2 = dateformat2.format(dt);
		return a1 + "T" + a2;
	}



	public static boolean compareDateTimeStr(String str1,String str2){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		boolean result = false;
		try {
			Date date1 = df.parse(str1);
			Date date2 = df.parse(str2);
			if((date1.getTime()	- date2.getTime()) > 0){
				result = true;
			}else{
				result = false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static String getCurrentTimeArrayStr(){
		Calendar c = Calendar.getInstance();
		//c.setTime(new java.util.Date());
		//c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) - 1);
		StringBuffer sb  = new StringBuffer();
		//int hour = c.get(Calendar.HOUR_OF_DAY);
		for(int i = 0; i<7; i++){
			Date tempDate =  new Date(new Date().getTime()+(i-9)*3600000);
			c.setTime(tempDate);
			int _hour = c.get(Calendar.HOUR_OF_DAY);		
			int _day = c.get(Calendar.DAY_OF_MONTH);
			int _Month = c.get(Calendar.MONTH)+1;
			String tempHour = "";
			String tempDay = "";
			String tempMonth = "";
			if(_hour < 10){
				tempHour = "0"+_hour;
			}else{
				tempHour = _hour + "";
			}
			if(_day < 10){
				tempDay = "0"+_day;
			}else{
				tempDay = _day + "";
			}	
			if(_Month < 10){
				tempMonth = "0"+_Month;
			}else{
				tempMonth = _Month + "";
			}			
			if(i<6){		
				sb.append(tempMonth+tempDay+tempHour+",");
			}else{
				sb.append(tempMonth+tempDay+tempHour);
			}
			
		}
		return sb.toString();
	}	
}