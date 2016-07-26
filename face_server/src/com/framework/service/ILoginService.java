/**
 * 
 */
package com.framework.service;

import java.util.List;
import java.util.Map;



/**
 */
public interface ILoginService {
	

	
	void initAuthorities();
	
	void initData();
	
	/**
	 * 检测用户输入密码是否正确
	 * @param params
	 * @return boolean 
	 */
	boolean checkPwd(Map<String,String> params);
	
	/**
	 * 修改当前用户密码
	 * @param params 修改参数 
	 */
	void updatePwd(Map<String,String> mapParams);
}