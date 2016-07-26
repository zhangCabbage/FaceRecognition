package com.framework.security;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;

public class AccessExceptionHandler implements AccessDeniedHandler {

	protected static final Log logger = LogFactory.getLog(AccessExceptionHandler.class);

	// ~ Instance fields ================================================================================================

	private String errorPage;
	
	private String ajaxError;

	// ~ Methods ========================================================================================================

	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		if (!response.isCommitted()) {
			if (errorPage != null && ajaxError != null) {
				request.setAttribute(WebAttributes.ACCESS_DENIED_403, accessDeniedException);
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);

				RequestDispatcher dispatcher;
				if (request.getHeader("x-requested-with") != null) {//判断请求是否为AJAX异步请求XMLHttpRequest
					dispatcher = request.getRequestDispatcher(ajaxError);
				} else {
					dispatcher = request.getRequestDispatcher(errorPage);
				}
				dispatcher.forward(request, response);
			} else {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
			}
		}
	}

	/**
	 * The error page to use. Must begin with a "/" and is interpreted relative to the current context root.
	 * 
	 * @param errorPage the dispatcher path to display
	 * 
	 * @throws IllegalArgumentException if the argument doesn't comply with the above limitations
	 */
	public void setErrorPage(String errorPage) {
		if ((errorPage != null) && !errorPage.startsWith("/"))
			throw new IllegalArgumentException("errorPage must begin with '/'");
		this.errorPage = errorPage;//403.jsp  对应   accessDeniedHandler 的配置部分。
	}

	public void setAjaxError(String ajaxError) {
		if ((errorPage != null) && !errorPage.startsWith("/"))
			throw new IllegalArgumentException("errorPage must begin with '/'");
		this.ajaxError = ajaxError;//failure.json
	}
}
