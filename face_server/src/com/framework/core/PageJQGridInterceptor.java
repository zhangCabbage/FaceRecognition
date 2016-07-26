/**
 * 
 */
package com.framework.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.util.ParameterMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.framework.control.BaseControl;

/**
 */
public class PageJQGridInterceptor extends HandlerInterceptorAdapter {
	
	private final static String CUR_PAGE = "page";
	private final static String PAGE_SIZE = "rows";
	private final static String ORDER_COL = "sidx";
	private final static String ORDER_ASC = "sord";
	private final static String TOTAL_PAGE = "totals";
	private final static String TOTAL_ROWS = "records";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof BaseControl) {
			BaseControl control = (BaseControl) handler;
			Integer currentPage = control.getInteger(request, CUR_PAGE);
			if (currentPage != null) {
				ParameterMap map = (ParameterMap) request.getParameterMap();
				map.setLocked(false);
				Page page = new Page();
				page.setCurrentPage(currentPage);
				map.remove(CUR_PAGE);
				page.setPageSize(control.getInteger(request, PAGE_SIZE));
				map.remove(PAGE_SIZE);
				page.setOrderColumn(control.getString(request, ORDER_COL));
				map.remove(ORDER_COL);
				page.setOrderASC("asc".equalsIgnoreCase(control.getString(request, ORDER_ASC)));
				map.remove(ORDER_ASC);
				map.setLocked(true);
				control.setPage(page);
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (handler instanceof BaseControl) {
			BaseControl control = (BaseControl) handler;
			Page page = control.getPage();
			if (page != null) {
				modelAndView.addObject(CUR_PAGE, page.getCurrentPage());
				modelAndView.addObject(PAGE_SIZE, page.getPageSize());
				modelAndView.addObject(ORDER_COL, page.getOrderColumn());
				modelAndView.addObject(ORDER_ASC, page.isOrderASC() ? "asc" : "desc");
				modelAndView.addObject(TOTAL_ROWS, page.getTotalRows());
				modelAndView.addObject(TOTAL_PAGE, (int) Math.ceil(Double.valueOf(page.getTotalRows()) / Double.valueOf(page.getPageSize())));				
			}
			page = null;
		}
	}
}
