package com.framework.dao;

import java.util.UUID;

/**
 * 主键生成器<br>
 * <p>生成数据库持久化主键</P>
 */
public class IDGenerator {
	
	/**
	 * 创建主键ID<br>
	 * @return String UUID串
	 */
	public static String createID(){
		return UUID.randomUUID().toString();
	}
}
