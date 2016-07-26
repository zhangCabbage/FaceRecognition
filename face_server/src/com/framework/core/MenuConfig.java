/**
 * 
 */
package com.framework.core;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author dranson on 2012-2-3
 * 
 */
public class MenuConfig {

	private static String rootMenus;
	/**
	 * 二级菜单
	 */
	//private static String secondaryMenus;
	/**
	 * 菜单配置文件
	 */
	private static String filePath = "/WEB-INF/menuConfig.xml";
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

	@SuppressWarnings("unchecked")
	public static String getRootMenus() {
		try {
			if (rootMenus == null) {
				WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
				if (menuDoc == null) {
					SAXReader saxReader = new SAXReader();
					menuDoc = saxReader.read(ctx.getResource(filePath).getInputStream());
				}
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
					if (i != (rootMenuList.size() - 1)) {
						rootBf.append(ROOT_MENU_SEPARATOR);
					}
				}
				rootMenus = rootBf.append("</ul>").toString();
			}
			return rootMenus;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 格式化菜单点击事件函数
	 * @param id
	 * @param handleType
	 * @param resource
	 * @param resourceType
	 * @param visitType
	 * @return String
	 */
	private static String formatHandle(String id, Integer handleType, String resource, String resourceType, String visitType) {
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
