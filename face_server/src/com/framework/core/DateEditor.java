/**
 * 
 */
package com.framework.core;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class DateEditor extends PropertyEditorSupport {

	private Class<?> targetType;

	public DateEditor(Class<?> targetType) {
		this.targetType = targetType;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			if (text != null && text.trim().length() != 0) {
				setValue(DatePatterns.changeType(DatePatterns.parse(text.trim()), targetType));
			} else {
				setValue(null);
			}
		} catch (Exception e) {
		}
	}

	static final class DatePatterns {
		private final static String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
		private final static String dateTimePattern2 = "yyyy/MM/dd HH:mm:ss";
		private final static String dateTimePattern3 = "yyyyMMddHHmmss";
		private final static String dateTimePattern4 = "yyyy-MM-dd HH:mm";
		private final static String dateTimePattern5 = "yyyy/MM/dd HH:mm";
		private final static String dateTimePattern6 = "yyyyMMddHHmm";
		private final static String datePattern = "yyyy-MM-dd";
		private final static String datePattern2 = "yyyy/MM/dd";
		private final static String datePattern3 = "yyyy-MM";
		private final static String timePattern = "HH:mm:ss";
		private final static String stimePattern = "HH:mm";

		private static Date parse(String text) throws ParseException {
			if (text.length() == dateTimePattern.length()) {
				if (text.charAt(4) == '-' && text.charAt(7) == '-')
					return new SimpleDateFormat(dateTimePattern).parse(text);
				if (text.charAt(4) == '/' && text.charAt(7) == '/')
					if (text.charAt(13) == ':' && text.charAt(16) == ':')
						return new SimpleDateFormat(dateTimePattern2).parse(text);
			} else if (text.length() == dateTimePattern3.length()) {
				return new SimpleDateFormat(dateTimePattern3).parse(text);
			} else if (text.length() == dateTimePattern4.length()) {
				if (text.charAt(4) == '-' && text.charAt(7) == '-')
					return new SimpleDateFormat(dateTimePattern4).parse(text);
				if (text.charAt(4) == '/' && text.charAt(7) == '/')
					if (text.charAt(13) == ':')
						return new SimpleDateFormat(dateTimePattern5).parse(text);
			} else if (text.length() == dateTimePattern6.length()) {
				return new SimpleDateFormat(dateTimePattern6).parse(text);
			} else if (text.length() == datePattern3.length()) {
				return new SimpleDateFormat(datePattern3).parse(text);
			} else if (text.length() == datePattern.length()) {
				if (text.charAt(4) == '-' && text.charAt(7) == '-')
					return new SimpleDateFormat(datePattern).parse(text);
				if (text.charAt(4) == '/' && text.charAt(7) == '/')
					return new SimpleDateFormat(datePattern2).parse(text);
			} else if (text.length() == timePattern.length()) {
				if (text.charAt(2) == ':' && text.charAt(5) == ':')
					return new SimpleDateFormat(timePattern).parse(text);
			} else if (text.length() == stimePattern.length()) {
				if (text.charAt(2) == ':')
					return new SimpleDateFormat(stimePattern).parse(text);
			}
			return new Date(Long.parseLong(text));
		}

		private static Date changeType(Date date, Class<?> targetType) {
			if (date == null)
				return date;
			if (java.sql.Date.class == targetType) {
				date = new java.sql.Date(date.getTime());
			} else if (java.sql.Time.class == targetType) {
				date = new java.sql.Time(date.getTime());
			} else if (java.sql.Timestamp.class == targetType) {
				date = new java.sql.Timestamp(date.getTime());
			}
			return date;
		}
	}
}
