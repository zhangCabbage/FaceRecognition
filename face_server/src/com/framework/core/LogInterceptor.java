package com.framework.core;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.framework.dao.IGenericHBDao;
 
import com.framework.domain.User;
import com.framework.util.DateUtils;

public class LogInterceptor extends HandlerInterceptorAdapter {

	// private static Document operLogdoc = null;

	// private String filePath = "/WEB-INF/logConfig.xml";

	@Autowired
	private IGenericHBDao genericHBDao;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		try {
			// 取得URL信息
			String url = request.getRequestURI();
			if (url.indexOf("flightGraphical/findAirportWeathers.txt") >= 0) 
				return;
			// 取得登录用户信息
			User user = ContextUtil.getUser();
			if (user == null)
				return;

			// 请求访问模块名
			String modelName = null;
			// 请求操作实例
			String instanceName = null;

			if (url.endsWith("login.do")) {
				modelName = "登录";
				instanceName = "登录";
			} else if (url.endsWith("logout.do")) {
				modelName = "登出";
				instanceName = "登出";
			} else {
				int pos = url.lastIndexOf("/");
				String path = url.substring(0, pos);
				String action = url.substring(pos + 1);
				if (action.startsWith("find"))
					return;

				// 遍历取得相关信息
				Collection<GrantedAuthority> authorities = user.getAuthorities();
				if (authorities != null && authorities.size() > 0) {
					Long parentId = null;
					// 取得instanceName
					for (GrantedAuthority authority : authorities) {
		   
					}
				}
			}
			// 保存Log信息
			Object e = request.getAttribute(SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE);
			if (user != null && e == null && instanceName != null && modelName != null) {

				// 登录IP
				WebAuthenticationDetails details = (WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
				String loginIp = details.getRemoteAddress();

				if (url.endsWith("login.do") || url.endsWith("logout.do")) {
					 
				} else {
				 
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(request.getRequestURI() + "----" + e.getMessage());
		}
	}

	/*
	 * public void afterCompletion(HttpServletRequest request,
	 * HttpServletResponse response, Object handler, Exception ex) throws
	 * Exception { try { if (operLogdoc == null) { SAXReader saxReader = new
	 * SAXReader(); WebApplicationContext ctx =
	 * ContextLoader.getCurrentWebApplicationContext(); operLogdoc =
	 * saxReader.read(ctx.getResource(filePath).getInputStream()); } // 读取配置文件.
	 * Element root = operLogdoc.getRootElement(); // 获取请求的aciton类名及其方法名 String
	 * className = handler.getClass().getSimpleName(); int start =
	 * request.getServletPath().lastIndexOf("/") + 1; int end =
	 * request.getServletPath().indexOf("."); // 方法名 String methodName =
	 * request.getServletPath().substring(start, end); // 根据action名读取配置文件中的节点
	 * Element actionNode = root.element(className);
	 * 
	 * // 如果节点不为空则表明需要日志记录，否则不需要. if (null != actionNode) { // 根据方法名读取配置文件中的方法节点
	 * Element methodNode = actionNode.element(methodName);
	 * 
	 * // 特殊处理 // if (null != invocation.getStack().findString("editId") &&
	 * invocation.getStack().findString("editId") != "''" &&
	 * invocation.getStack().findString("editId").length() > 2) { // methodNode
	 * = actionNode.element("edit"); // } else { // methodNode =
	 * actionNode.element(invocation.getProxy().getMethod()); // }
	 * 
	 * // 请求方法名节点是否存在，不存在则不记录日志 if (methodNode != null) { // 请求访问模块名 String
	 * modelName = actionNode.attributeValue("modelName"); //
	 * 如果action节点中无模块名，则查看method节点中模块名 if (StringUtils.isEmpty(modelName))
	 * modelName = methodNode.attributeValue("modelName"); // 请求操作实例 String
	 * instanceName = methodNode.attributeValue("instanceName"); // 操作时间
	 * Timestamp operateTime = DateUtils.getCurrentDateTime();
	 * 
	 * User user = ContextUtil.getUser(); WebAuthenticationDetails details =
	 * (WebAuthenticationDetails)
	 * SecurityContextHolder.getContext().getAuthentication().getDetails();
	 * String loginIp = details.getRemoteAddress();
	 * 
	 * Object e = request.getAttribute(SimpleMappingExceptionResolver.
	 * DEFAULT_EXCEPTION_ATTRIBUTE); if (user != null && e == null) { Log log =
	 * new Log(); log.setLoginIp(loginIp); log.setOrg(user.getOrg());
	 * log.setOperateType(1); log.setRealName(user.getName());
	 * log.setOperateUser(user.getId().toString());
	 * log.setOperateTime(operateTime); log.setInstance(instanceName);
	 * log.setModel(modelName); genericHBDao.save(log); } } } } catch (Exception
	 * e) { throw new RuntimeException(e.getMessage()); } }
	 */
}
