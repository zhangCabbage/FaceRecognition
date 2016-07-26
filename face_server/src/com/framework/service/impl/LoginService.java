/**
 * 
 */
package com.framework.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;

import com.framework.core.ContextUtil;
import com.framework.dao.IGenericHBDao;
 
import com.framework.domain.User;
import com.framework.service.ILoginService;

/**


 */
public class LoginService implements ILoginService {
	
	private IGenericHBDao genericHBDao;

	@Override
	public void initAuthorities() {
		
		
		User user = ContextUtil.getUser();
		/*
		String sql = "select r.fcode from t_um_user u join t_um_member ur on u.fid=ur.userid join t_pm_role r on r.fid=ur.roleid where u.fname=?";
		List<Map<String, Object>> list = genericHBDao.findMapBySQL(sql, user.getName());
		Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
		for (Map<String, Object> map : list) {
			Role role = new Role();
			role.setCode((String) map.get("fcode"));
			roles.add(role);
		}
		
		*/
	//	user.setRoles(roles);
	}



	@Override
	public void initData() {
		int abc=1;
	}

	public void setGenericHBDao(IGenericHBDao genericHBDao) {
		this.genericHBDao = genericHBDao;
	}
	

	@Override
	public boolean checkPwd(Map<String,String> mapParams) {
		//获得登入用户名
		User operUser = ContextUtil.getUser();
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		//检测新旧密码是否一致
		if(operUser.getPassword().equals(md5.encodePassword(mapParams.get("oldPasswd"),""))){
			return true;
		}
		return false;
	}

	@Override
	public void updatePwd(Map<String,String> mapParams) {
		User operUser = ContextUtil.getUser();
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		String hql = "update User u set u.passwd=? where  u.id=?";
		//传入用户的ID和修改后的秘密
		genericHBDao.executeHQL(hql, md5.encodePassword(mapParams.get("passwd"), ""), operUser.getId());
	}

}