/**
 * 
 */
package com.framework.control;

import java.net.URLDecoder;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.framework.core.ContextUtil;
import com.framework.service.ILoginService;


/**
 */
@Controller
public class LoginControl extends BaseControl {
	
	@Autowired
	private ILoginService loginService;


	@RequestMapping(value = "login.do")
	public ModelAndView login() {
		ModelAndView view = new ModelAndView("/modules/portlets/index.jsp");

		
		return view;
	}
	
	@RequestMapping(value = "logout.do")
	public ModelAndView logout() {
		return new ModelAndView(new RedirectView("j_spring_security_logout"));
	}
	
	@RequestMapping(value = "noAccess.json")
	public void noAccess(String name) {
		String s = null;
		try {
			if (name != null)
				s = URLDecoder.decode(name, "UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		throw new RuntimeException((s == null ? "" : s) + ContextUtil.getMessage("lt.check.access"));
	}
	
	@RequestMapping(value = "initData.json")
	public void initData() {
		loginService.initData();
	}
	
	@RequestMapping(value = "failure.json")
	public void failure() {
		Exception e = (Exception) ContextUtil.getAttributeBySession(WebAttributes.AUTHENTICATION_EXCEPTION);
		if (e == null)
			e = (Exception) ContextUtil.getAttribute(WebAttributes.ACCESS_DENIED_403);
		throw new RuntimeException(e.getMessage());
	}
	
	/***
	 * 页面用户密码修改，如果只是单纯的页面跳转用*。do，如果是数据转换则用*。json
	 * 该方法是用于用户密码修改的跳转页面
	 * @return passwordView
	 */
	@RequestMapping(value = "updatePassword.do")
	public ModelAndView updatePassword(){
		ModelAndView passwordView = new ModelAndView("/modules/system/updatePasswd.jsp");
		return passwordView;
	}
	

	/**
	 * 该方法用于检测用户密码是否正确
	 */
	@RequestMapping(value = "checkPwd.json")
	public void checkPwd(@RequestParam Map<String, String> mapParams){
		loginService.checkPwd(mapParams);
	}
	
	/**
	 * 该方法用于用户密码修改
	 * @param user
	 */
	@RequestMapping(value = "updatePwd.json")
	public void updatePwd(@RequestParam Map<String, String> mapParams){
		loginService.updatePwd(mapParams);
	}
}