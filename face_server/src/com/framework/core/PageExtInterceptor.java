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
public class PageExtInterceptor extends HandlerInterceptorAdapter {
	
	private final static String CUR_PAGE = "page";
	private final static String PAGE_SIZE = "limit";
	private final static String ORDER = "sort";
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
				String order = control.getString(request, ORDER);
				map.remove(ORDER);
				if (order != null) {//[{"property":"id","direction":"DESC"}]
					String[] ary = order.split(",");
					page.setOrderColumn(ary[0].substring(ary[0].indexOf(":") + 2, ary[0].length() - 1));
					page.setOrderASC("ASC".equalsIgnoreCase(ary[1].substring(ary[1].indexOf(":") + 2, ary[1].length() - 3)));
				}
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
				if (page != null) {
					StringBuffer buf = new StringBuffer();
					buf.append("[{\"property\":\"").append(page.getOrderColumn()).append("\",\"direction\":\"");
					buf.append(page.isOrderASC() ? "ASC" : "DESC").append("\"}]");
					modelAndView.addObject(ORDER, buf.toString());
				}
				modelAndView.addObject(TOTAL_ROWS, page.getTotalRows());
				modelAndView.addObject(TOTAL_PAGE, (int) Math.ceil(Double.valueOf(page.getTotalRows()) / Double.valueOf(page.getPageSize())));				
			}
			control.setPage(null);
		}
	}
}
