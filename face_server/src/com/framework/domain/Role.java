package com.framework.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

/**

 */
public class Role  implements GrantedAuthority {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 编码 FCode 以"ROLE_"开头
	 */
	private String code;
	/**
	 * 名称 
	 */
	private String name ;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 状态
	 */
	private Integer state;
	private String id;
	/**
	 * 权限集合
	 */
	private Set<FunctionAuthority> items = new HashSet<FunctionAuthority>(0);
	/**
	 * 用户集合
	 */
	private Set<User> users = new HashSet<User>(0);
	
	public Role() {
	}

	public Role(String ID) {
		setId(ID);
	}

	
	public void setId(String id){
		this.id=id;
	}
	
	public String getId(){
		return this.id;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<FunctionAuthority> getItems() {
		return items;
	}

	public void setItems(Set<FunctionAuthority> items) {
		this.items = items;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public String getAuthority() {
		return this.code;
	}
}
