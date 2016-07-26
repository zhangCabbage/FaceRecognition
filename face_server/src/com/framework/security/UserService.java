/**
 * 
 */
package com.framework.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.framework.dao.IGenericHBDao;
import com.framework.dao.IGenericJDBCDao;
 
import com.framework.domain.User;

/**
 
 */
public class UserService implements UserDetailsService {

	private IGenericHBDao genericHBDao;

	private IGenericJDBCDao genericJDBCDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		StringBuilder sb = new StringBuilder();
		sb.append("from User where username=?");
		User user = genericHBDao.findTByHQL(sb.toString(), username);
		 
	 
		    PasswordEncoder passwordencoder=new Md5PasswordEncoder();
	 
		    
		
		return user;
	}

	public void setGenericHBDao(IGenericHBDao genericHBDao) {
		this.genericHBDao = genericHBDao;
	}

	public void setGenericJDBCDao(IGenericJDBCDao genericJDBCDao) {
		this.genericJDBCDao = genericJDBCDao;
	}
}
