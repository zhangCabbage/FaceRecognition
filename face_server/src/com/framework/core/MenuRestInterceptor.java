/*
 *@项目名称: CDM 深圳机场系统决策系统
 *@文件名称: MenuRestInterceptor.java
 *@Copyright: 2009-2010 SZATCTEE All Rights Reserved.
 *注意：本内容仅限于 深圳市空管技术装备工程有限公司 内部传阅，
 *	   禁止外泄以及用于其他的商业目的
 */

package com.framework.core;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.framework.control.LoginControl;
 

/**
 * 菜单点击拦截器<br>
 * <p>
 * 用户初始化一级菜单及二级菜单，根据用户点击情况更新操作菜单
 * </P>
 * 
 * @author 李选东
 * @version V1.00
 * @date Jan 13, 2011
 */
public class MenuRestInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 一级菜单
	 */
	private static String rootMenus = "";

	/**
	 * 二级菜单
	 */
	private static String secondaryMenus = "";
	/**
	 * 菜单配置文件
	 */
	private String filePath = "/WEB-INF/menuConfig.xml";
	/**
	 * 菜单配置文档
	 */
	public static Document menuDoc = null;
	/**
	 * 一级菜单格式
	 */
	public static final String ROOT_FORMAT = "<li id=\"%s\" style=\"width:%spx\"><a href=\"#\" onclick=\"%s\">%s</a></li>";
	/**
	 * 一级菜单默认宽度
	 */
	public static final String ROOT_WIDTH = "70";
	/**
	 * 一级菜单分割符
	 */
	public static final String ROOT_MENU_SEPARATOR = "<li class=\"menu_separator\">&nbsp;</li>";
	/**
	 * 二级菜单格式
	 */
	public static final String SECONDARY_FORMAT = "<a id=\"%s\" href='#' onclick=\"%s\">%s</a>&nbsp;";
	/**
	 * 当前二级菜单格式
	 */
	public static final String CUR_SECONDARY_FORMAT = "<a id=\"%s\" href='#' onclick=\"%s\"><b>%s</b></a>&nbsp;";
	/**
	 * 脚本函数格式
	 */
	public static final String FUNCTION_FORMAT = "javascript:visitFun(\'%s?menuId=%s\',\'%s\',\'%s\')";

	/**
	 * 拦截操作
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String menuId = "";
		// 获取请求的aciton类名及其方法名
		// String className = invocation.getProxy().getConfig().getClassName();
		// String actionName = className.substring(className.lastIndexOf(".") + 1);
		// 加载配置文件
		if (menuDoc == null) {
			SAXReader saxReader = new SAXReader();
			WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
			menuDoc = saxReader.read(ctx.getResource(filePath).getInputStream());
		}

		// 读取配置文件
		Element root = menuDoc.getRootElement();
		// 登录初始化
		if (handler instanceof LoginControl && request.getRequestURI().endsWith("login.do")) {
			defualtInvoke();
		} else { // 点击菜单请求
			HttpSession session = request.getSession();
			menuId = request.getParameter("menuId");
			if (StringUtils.isNotEmpty(menuId)) {
				Element currentRootNode = root.element(menuId);

				if (currentRootNode == null) { // 点击为二级菜单
					// FIX 强制赋值，框架BUG，临时措施 modify by dranson on 2012-01-05
					String curId = (String) session.getAttribute("menuId");
					if (curId == null) {
//						curId = "f_0";
						currentRootNode = setRootMenuBySecond(session, menuId);
					} else {
						currentRootNode = root.element(curId);
					}
					setSecondaryMenu(session, currentRootNode, menuId);
				} else {// 点击为一级菜单
					setRootMenu(session, menuId);
					setSecondaryMenu(session, currentRootNode, null);
				}
			}
		//	IMessageService messageService = ContextUtil.getBeanByType(IMessageService.class);
			try {
		//		request.setAttribute("_message", messageService.findByNameMessages(ContextUtil.getUser().getName()));
			} catch (Exception e) {}
		}
		return true;
	}

	/**
	 * 一级菜单设置<br>
	 * 
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	private void setRootMenu(HttpSession session, String rootMenuId) {
		if (StringUtils.isNotEmpty(rootMenus)) {
			// 设置当前一级菜单的id
			session.setAttribute("menuId", rootMenuId);
		} else {
			Element root = menuDoc.getRootElement();
			List<Element> rootMenuList = root.elements();
			StringBuffer rootBf = new StringBuffer();
			rootBf.append("<ul>");

			for (int i = 0; i < rootMenuList.size(); i++) {
				Element rootMenuNode = (Element) rootMenuList.get(i);
				// 节点id
				String rootId = rootMenuNode.getName();
				// 菜单名称
				String rootName = rootMenuNode.attributeValue("menuName");
				// handleType ： 操作类型 （1：url打开； 2:系统默认脚本函数执行； 3：自定义脚本函数执行）
				Integer rootHandleType = Integer.parseInt(rootMenuNode.attributeValue("handleType"));
				// visitType :浏览方式 （1：在当前窗口打开； 2：在新窗口中打开）
				String rootVisitType = rootMenuNode.attributeValue("visitType") == null ? "" : rootMenuNode.attributeValue("visitType");
				// resource :访问资源（url 或 脚本）
				String rootResource = rootMenuNode.attributeValue("resource");
				// width :显示宽度
				String rootWidth = rootMenuNode.attributeValue("width") == null ? ROOT_WIDTH : rootMenuNode.attributeValue("width");
				// resourceType:资源类型（1：内部 2：外部系统资源）
				String rootResourceType = rootMenuNode.attributeValue("resourceType") == null ? "" : rootMenuNode.attributeValue("resourceType");
				// 格式化菜单点击事件函数
				String handleStr = formatHandle(rootId, rootHandleType, rootResource, rootResourceType, rootVisitType);
				// 一级菜单格式:<li id=\"%s\" style=\"width:%s\"><a href=\"#\" onclick=\"%s\">%s</a></li>
				rootBf.append(String.format(ROOT_FORMAT, new Object[] { rootId, rootWidth, handleStr, rootName }));
				if (i != (rootMenuList.size() - 1))
					rootBf.append(ROOT_MENU_SEPARATOR);
			}
			Element firstRootMenu = (Element) rootMenuList.get(0);
			session.setAttribute("menuId", firstRootMenu.getName());
			rootMenus = rootBf.append("</ul>").toString();
		}
		session.setAttribute("rootMenus", rootMenus);
	}

	/**
	 * 一级菜单设置<br>
	 * 
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	private Element setRootMenuBySecond(HttpSession session, String secondId) {
		Element root = menuDoc.getRootElement();
		List<Element> rootMenuList = root.elements();
		StringBuffer rootBf = new StringBuffer();
		rootBf.append("<ul>");
		Element curRoot = null;
		for (int i = 0; i < rootMenuList.size(); i++) {
			Element rootMenuNode = (Element) rootMenuList.get(i);
			// 节点id
			String rootId = rootMenuNode.getName();
			// 菜单名称
			String rootName = rootMenuNode.attributeValue("menuName");
			// handleType ： 操作类型 （1：url打开； 2:系统默认脚本函数执行； 3：自定义脚本函数执行）
			Integer rootHandleType = Integer.parseInt(rootMenuNode.attributeValue("handleType"));
			// visitType :浏览方式 （1：在当前窗口打开； 2：在新窗口中打开）
			String rootVisitType = rootMenuNode.attributeValue("visitType") == null ? "" : rootMenuNode.attributeValue("visitType");
			// resource :访问资源（url 或 脚本）
			String rootResource = rootMenuNode.attributeValue("resource");
			// width :显示宽度
			String rootWidth = rootMenuNode.attributeValue("width") == null ? ROOT_WIDTH : rootMenuNode.attributeValue("width");
			// resourceType:资源类型（1：内部 2：外部系统资源）
			String rootResourceType = rootMenuNode.attributeValue("resourceType") == null ? "" : rootMenuNode.attributeValue("resourceType");
			// 格式化菜单点击事件函数
			String handleStr = formatHandle(rootId, rootHandleType, rootResource, rootResourceType, rootVisitType);
			// 一级菜单格式:<li id=\"%s\" style=\"width:%s\"><a href=\"#\" onclick=\"%s\">%s</a></li>
			rootBf.append(String.format(ROOT_FORMAT, new Object[] { rootId, rootWidth, handleStr, rootName }));
			if (i != (rootMenuList.size() - 1))
				rootBf.append(ROOT_MENU_SEPARATOR);
			if (curRoot == null && rootMenuNode.element(secondId) != null) {
				session.setAttribute("menuId", rootId);
				curRoot = rootMenuNode;
			}
		}
//		Element firstRootMenu = (Element) rootMenuList.get(0);
//		session.setAttribute("menuId", firstRootMenu.getName());
		if (rootMenus == null)
			rootMenus = rootBf.append("</ul>").toString();
		if (session.getAttribute("rootMenus") == null)
			session.setAttribute("rootMenus", rootMenus);
		return curRoot;
	}

	/**
	 * 二级菜单设置<br>
	 * <br>
	 * 
	 * @param session
	 * @param currentRootNode
	 *            当前一级菜单节点
	 * @param curSecondaryId
	 *            当前二级菜单节点名称
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	private void setSecondaryMenu(HttpSession session, Element currentRootNode, String curSecondaryId) {
		// 二级菜单设置
		List<Element> secondaryMenuNodes = currentRootNode.elements();
		StringBuffer secondaryBf = new StringBuffer();
		// 点击二级菜单情况
		if (StringUtils.isNotEmpty(curSecondaryId)) {
			for (int j = 0; j < secondaryMenuNodes.size(); j++) {
				Element secondaryMenu = secondaryMenuNodes.get(j);
				// 节点id
				String secondaryId = secondaryMenu.getName();
				// 菜单名称
				String secondaryName = secondaryMenu.attributeValue("menuName");
				// handleType ： 操作类型 （1：url打开； 2:系统默认脚本函数执行； 3：自定义脚本函数执行）
				Integer secondaryHandleType = Integer.parseInt(secondaryMenu.attributeValue("handleType"));
				// visitType :浏览方式 （1：在当前窗口打开； 2：在新窗口中打开）
				String secondaryVisitType = secondaryMenu.attributeValue("visitType") == null ? "" : secondaryMenu.attributeValue("visitType");
				// resource :访问资源（url 或 脚本）
				String secondaryResource = secondaryMenu.attributeValue("resource");
				// resourceType:资源类型（1：内部 2：外部系统资源）
				String secondaryResourceType = secondaryMenu.attributeValue("resourceType") == null ? "" : secondaryMenu.attributeValue("resourceType");

				// 格式化菜单点击事件函数
				String handleStr = formatHandle(secondaryId, secondaryHandleType, secondaryResource, secondaryResourceType, secondaryVisitType);
				// 格式化菜单Html代码
				String secondaryMenuStr = "";
				if (curSecondaryId.equalsIgnoreCase(secondaryId)) {
					// <a id=\"%s\" href='#' onclick=\"%s\"><b>%s</b></a>&nbsp;
					secondaryMenuStr = String.format(CUR_SECONDARY_FORMAT, new Object[] { secondaryId, handleStr, secondaryName });
				} else {
					secondaryMenuStr = String.format(SECONDARY_FORMAT, new Object[] { secondaryId, handleStr, secondaryName });
				}
				secondaryBf.append(secondaryMenuStr);
			}
			secondaryMenus = secondaryBf.toString();
		} else {// 点击一级菜单的情况
				// 如果存在子菜单
			if (secondaryMenuNodes.size() > 0) {
				for (int j = 0; j < secondaryMenuNodes.size(); j++) {
					Element secondaryMenu = secondaryMenuNodes.get(j);
					// 节点id
					String secondaryId = secondaryMenu.getName();
					// 菜单名称
					String secondaryName = secondaryMenu.attributeValue("menuName");
					// handleType ： 操作类型 （1：url打开； 2:系统默认脚本函数执行； 3：自定义脚本函数执行）
					Integer secondaryHandleType = Integer.parseInt(secondaryMenu.attributeValue("handleType"));
					// visitType :浏览方式 （1：在当前窗口打开； 2：在新窗口中打开）
					String secondaryVisitType = secondaryMenu.attributeValue("visitType");
					// resource :访问资源（url 或 脚本）
					String secondaryResource = secondaryMenu.attributeValue("resource");
					// resourceType:资源类型（1：内部 2：外部系统资源）
					String secondaryResourceType = secondaryMenu.attributeValue("resourceType");

					// 格式化菜单点击事件函数
					String handleStr = formatHandle(secondaryId, secondaryHandleType, secondaryResource, secondaryResourceType, secondaryVisitType);
					// 格式化菜单Html代码
					String secondaryMenuStr = "";
					if (j == 0) {
						// <a id=\"%s\" href='#' onclick=\"%s\"><b>%s</b></a>&nbsp;
						secondaryMenuStr = String.format(CUR_SECONDARY_FORMAT, new Object[] { secondaryId, handleStr, secondaryName });
					} else {
						secondaryMenuStr = String.format(SECONDARY_FORMAT, new Object[] { secondaryId, handleStr, secondaryName });
					}
					secondaryBf.append(secondaryMenuStr);
				}
				secondaryMenus = secondaryBf.toString();
			} else { // 不存在子菜单
				secondaryMenus = "<a href='#' onclick=\"javascript:void(0);\"><b>" + currentRootNode.attributeValue("menuName") + "</b></a>";
			}
		}
		session.setAttribute("secondaryMenus", secondaryMenus);
	}

	/**
	 * 登录默认处理<br>
	 * <P>
	 * 登录时，默认显示第一个一级菜单的第一个二级菜单内容
	 * </P>
	 * 
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	private void defualtInvoke() {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = servletRequestAttributes.getRequest();
		HttpSession session = request.getSession();
		// ======================= 一级菜单设置 ==============================
		Element root = menuDoc.getRootElement();
		List<Element> rootMenuList = root.elements();
		Element firstRootMenu = (Element) rootMenuList.get(0);

		// 设置一级菜单
		setRootMenu(session, null);
		// 二级菜单设置
		setSecondaryMenu(session, firstRootMenu, null);
	}

	/**
	 * 格式化菜单点击事件函数
	 * 
	 * @param id
	 * @param handleType
	 * @param resource
	 * @param resourceType
	 * @param visitType
	 * @return String
	 */
	private String formatHandle(String id, Integer handleType, String resource, String resourceType, String visitType) {
		String handleStr = "";
		// 资源不为空
		if (StringUtils.isNotEmpty(resource)) {
			switch (handleType) { // 操作类型 （1:系统默认脚本函数执行； 2：自定义脚本函数执行）
			case 1:
				handleStr = String.format(FUNCTION_FORMAT, resource, id, resourceType, visitType);
				break;
			case 2:// 自定义函数时，必须在自定函数的请求url中加入 参数：menuId=f_xx
				handleStr = resource;
				break;
			}
		} else {
			// 资源为空
			handleStr = "javascript:void(0);";
		}
		return handleStr;
	}
}
