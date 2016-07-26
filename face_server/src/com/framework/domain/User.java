package com.framework.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 

 */
@SuppressWarnings("serial")
public class User   implements UserDetails {
	
	//select  uid,email,username,password,status,emailstatus,adminid,groupid  from pre_common_member where username='admin'
	/**
	 * 登陆用户名FName
	 */
	private String name;
	/**
	 * 密码FPasswd
	 */
	private String password;
	
	
	private String userName;
	/**
	 * 旧密码oldPasswd
	 */
	private String oldPassword;
 
	/**
	 * 地址 email
	 */
	private String email;
	
	private Integer status;
 
	
	private Integer id;
	/**
	 * 
	 * 构造函数<br>
	 */
	public User() {
	}

	public Integer getId()
	{
		return id;
	}
	
	
	public void setId(Integer id){
		this.id=id;
	}
	/**
	 * 构造函数
	 * @param name 名称	 
	 */
	public User(String name) {
		setName(name);
		 
	} 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName()
	{
		return this.userName;
	}
	
	public void setUserName(String userName){
		this.userName=userName;
	}
	 
	@Override
	public String getPassword() {
		return this.password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail(){
		return email;
	}
   
	public void setEmail(String email){
		this.email=email;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
 
	public Integer getStatus()
	{
		return this.status;
	}
	
	public void setStatus(Integer status){
	
		this.status=status;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return null;
	}


	@Override
	public String getUsername() {
		return this.name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
