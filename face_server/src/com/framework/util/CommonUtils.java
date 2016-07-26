package com.framework.util;

import java.util.HashMap;
import java.util.Map;


/**
 * 各种工具方法<br>
 * <p>该类为一工具类</P>

 */
public class CommonUtils { 
	
	/**
 	 * 从map中删除key在strArr中的元素
 	 */
	public static Map<String, String> mapRemoveCollection( Map<String, String> map, String[] strArr){
 		Map<String, String> newmap = new HashMap<String, String>(map);
 		for (String s : strArr){
 			newmap.remove(s);
 		}
 		return newmap;
 	}
}
