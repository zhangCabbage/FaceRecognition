/**
 * 
 */
package com.framework.core;

import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.framework.domain.User;

/**
 
 */
public class ContextUtil {
	
	public static String getPassword(String password) {
		PasswordEncoder passwordEncoder = getBeanByType(PasswordEncoder.class);
		return passwordEncoder.encodePassword(password, null);
	}
	
	public static String getPassword(String password, Object salt) {
		PasswordEncoder passwordEncoder = getBeanByType(PasswordEncoder.class);
		return passwordEncoder.encodePassword(password, salt);
	}
	
	public static int setRights(int... rights) {
		int rs = 0;
		for (int right : rights)
			rs = rs | (int) Math.pow(2, right);
		return rs;
	}
	/**
	 * 权限校验
	 * @param role	具备的权限
	 * @param access	需要校验的操作权限
	 * @return
	 */
	public static boolean checkAccess(int role, int access) {
		if (role != 0) {
			int value = (int) Math.pow(2, access);
			return (role & value) == value;
		}
		return false;
	}
	
	public static User getUser() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return obj instanceof User ? (User) obj : null;
	}
	
	/**
	 * 获取request参数值
	 * @param name
	 * @return
	 */
	public static String getParameter(String name) {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return servletRequestAttributes.getRequest().getParameter(name);
	}
	
	/**
	 * 获取request属性值
	 * @param name
	 * @return
	 */
	public static Object getAttribute(String name) {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return servletRequestAttributes.getRequest().getAttribute(name);
	}
	
	/**
	 * 获取session属性值
	 * @param name
	 * @return
	 */
	public static Object getAttributeBySession(String name) {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return servletRequestAttributes.getAttribute(name, RequestAttributes.SCOPE_SESSION);
	}
	
	/**
	 * 通过bean名称获取对象
	 * @param <T>
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static <T> T getBeanByName(Class<T> clazz, String name) {
		ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		return ctx.getBean(name, clazz);
	}
	
	/**
	 * 通过bean类型获取对象
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T> T getBeanByType(Class<T> clazz) {
		ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		return ctx.getBean(clazz);
	}
	
	/**
	 * 读取国际化信息
	 * @param code	资源文件键值
	 * @return
	 */
	public static String getMessage(String code) {
		ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return ctx.getMessage(code, null, servletRequestAttributes.getRequest().getLocale());
	}
	
	/**
	 * 密码加密
	 * @param password	密码
	 * @return
	 */
	public static String getMd5Pwd(String password) {
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		return md5.encodePassword(password, null);
	}
}
