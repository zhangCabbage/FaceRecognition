 

package com.framework.data;

import java.util.HashMap;
import java.util.Map;

/**
 
 */
public enum EnumState {
	//公共
	
	/**
	 * ABS(0x2000) : 手工设置取消 ABS
	 */
	ABC(0x2000);
    
    private int hexValue;
    
    /**
     * 构造函数
     * @param hexValue 
     */
    EnumState(int hexValue){
    	this.hexValue = hexValue;
    }
    
    /**
     * 获取对应枚举的16进制字符值
     * @return 枚举的16进制字符值
     */
    public String getHexStringValue(){
    	return Integer.toHexString(this.hexValue);
    }
  
    /**
     * 获取对应枚举的10进制值
     * @return 枚举的10进制值
     */
    public int getValue(){
    	return this.hexValue;
    }
    
    /**
     * 获取对应枚举的16进制字符值
     * @return 枚举的10进制字符值
     */
    public String getValueString(){
    	return Integer.toString(getValue());
    }
    
    /**
     * 获取对应枚举的2进制字符值
     * @return
     */
    public String getBinaryValue(){
    	return Integer.toBinaryString(hexValue);
    }
    
    /**
     * 将EnumFlightState转换为Map
     * 	key：枚举名的字符串
     *  value：枚举的int值
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map getMap()
    {
    	EnumState[] arr = EnumState.values();
    	Map map = new HashMap();
    	for (EnumState state : arr) {
    		map.put(state.toString(), state.getValue());
		}
    	return map;
    }
    
    /**
     * 获取字符对应得枚举项
     * @param  String value
     * @return EnumFlightState 
     */
    public static EnumState getEnum(String value){
    	return getEnum(Integer.valueOf(value));
    }
    
    /**
     * 获取数字对应得枚举项
     * @param  int value
     * @return EnumFlightState 
     */
    public static EnumState getEnum(int value){
    	for(EnumState state : values()){
    		if(state.getValue() == value){
    			return state;
    		}
    	}
    	return null;
    }
}
