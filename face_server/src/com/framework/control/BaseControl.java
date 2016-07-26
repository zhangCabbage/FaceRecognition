package com.framework.control;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.view.AbstractView;

import com.framework.core.Page;

/**
 * 主要提供分页查询，以及客户端请求数据的绑定
 */
public class BaseControl {

	protected final static Logger logger = Logger.getLogger(BaseControl.class);

	protected Page page;

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public void initView(AbstractView view) {
		view.setApplicationContext(ContextLoader
				.getCurrentWebApplicationContext());
	}

	public String getParameter(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		if (value == null)
			return null;
		return value.trim();
	}

	public String getParameter(String paramName) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		return request.getParameter(paramName);
	}

	public Object getAttribute(String attr) {
		return getAttribute(attr, RequestAttributes.SCOPE_SESSION);
	}

	public Object getAttribute(String attr, int scope) {
		RequestAttributes requestAttributes = RequestContextHolder
				.currentRequestAttributes();
		return requestAttributes.getAttribute(attr, scope);
	}

	public String getString(HttpServletRequest request, String paramName) {
		String value = getParameter(request, paramName);
		if (value == null || value.length() == 0)
			return null;
		return value;
	}

	public Integer getInteger(HttpServletRequest request, String paramName) {
		try {
			return Integer.valueOf(getParameter(request, paramName));
		} catch (Exception e) {
		}
		return null;
	}

	public Long getLong(HttpServletRequest request, String paramName) {
		try {
			return Long.valueOf(getParameter(request, paramName));
		} catch (Exception e) {
		}
		return null;
	}

	public String getJson(Object value) {
		String s = null;
		try {
			s = new ObjectMapper().writeValueAsString(value);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return s;
	}

	public void ajaxOutput(HttpServletResponse response, String outputString)
			throws IOException {
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(outputString);
		response.getWriter().flush();
	}

	public void ajaxOutput(HttpServletResponse response,
			List<Map<String, Object>> listMap) throws IOException {
		// 跨域调用，调用此方法的控制层方法，新增参数 HttpServletResponse response
		// 包：json-lib-2.3-jdk15 json-20090211 json_simple-1.1 ezmorph-1.0.6
		JSONObject jo = new JSONObject();
		jo.element("rows", listMap);
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(jo.toString());
		response.getWriter().flush();
	}

	/**
	 * 处理图片文件上传请求
	 * 
	 * @param request
	 * @param response
	 * @param paraMap
	 * @throws Exception
	 */
	public String handleImgRequest(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> paraMap)
			throws Exception {
		String userId = paraMap.get("userId") == null ? "guest" : paraMap
				.get("userId").toString();
		MultipartHttpServletRequest multipartRequest = null;
		String route=null;
		// 获得第1张图片（根据前台的name名称得到上传的文件）
		MultipartFile img = null;
		try {
			//转型为MultipartHttpRequest
			multipartRequest = (MultipartHttpServletRequest) request;
			img = multipartRequest.getFile("imgFile");
			//获得文件名
			String fileName = img.getOriginalFilename();
			//获取当前时间
			String time=String.valueOf((new Date()).getTime());
			System.out.println("time:"+time);
			//获取一个随机数
			String random1=String.valueOf((Math.random()+1)*100);
			String random2=String.valueOf(random1.substring(1,3));
			String Fname=time+random2;
			//获取文件大小
			byte[] bytes = img.getBytes();
			String suf=fileName.substring(fileName.lastIndexOf("."), fileName.length());
			Fname=Fname+suf;
			System.out.println("2:Fname:"+Fname);
			// 从长远角度讲，每个用户都应该有一个以自己用户名命名的文件夹，位于目录/pic下。
			// 需要等用户账号功能完全实现后才可以做这一块。现在默认在 /pic/guest 目录下
			String uploadDir = request.getRealPath("/") + "pic/" + userId;
			System.out.println("3:"+uploadDir);
			File dirPath = new File(uploadDir);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}
			String sep = System.getProperty("file.separator");//获取文件分隔符‘/’
			System.out.println("4:sep="+sep);
			/*File uploadedFile = new File(uploadDir + sep+ img.getOriginalFilename());*/
			File uploadedFile = new File(uploadDir + sep+Fname);
			FileCopyUtils.copy(bytes, uploadedFile);
			route=userId+sep+Fname;
			
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		return route;
	}
	
}
