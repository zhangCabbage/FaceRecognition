package com.framework.domain;

import java.sql.Timestamp;

public class Log {
	/**
	 * 日志内容
	 */
	private String logContent;
	/**
	 * 日志生成时间
	 */
	private Timestamp log_time;
	/**
	 * 日志ID
	 */
	private Integer loggingId;
	
	public Log() {
	}

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	public Timestamp getLog_time() {
		return log_time;
	}

	public void setLog_time(Timestamp log_time) {
		this.log_time = log_time;
	}

	public Integer getLoggingId() {
		return loggingId;
	}

	public void setLoggingId(Integer loggingId) {
		this.loggingId = loggingId;
	}


}
