package com.framework.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * 功能权限实体<br>
 * @author    李选东
 * @version   V1.00 
 * @date      Nov 17, 2010
 */
public class FunctionAuthority extends PersistentBase  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 编码	FNumber
	 */
	private String code;
	/**
	 * 功能名称
	 */
	private String name;
	/**
	 * 功能类型 1：url  2：按钮 3：数据 4：方法
	 */
    private Integer type;
    /**
     * 权限资源
     */
    private String resource;
    /**
     * 描述
     */
    private String description;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 显示顺序
     */
    private Integer order;
    
 
    
    
    
    /**
     * 角色集合 
     */
	@SuppressWarnings("rawtypes")
	private Set roles =  new HashSet(0);

    public FunctionAuthority() {
    }
    
    public FunctionAuthority(String ID) {
       setId(ID);
    }
    
 
	@SuppressWarnings("rawtypes")
	public FunctionAuthority(Integer type, String resource,Set roles) {
       this.type = type;
       this.resource = resource;
       this.roles = roles;
    }
    
	/**
	 * @return 属性 code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置属性 code 值.
	 * @param code 设置属性 code 值
	 */
	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	@SuppressWarnings("rawtypes")
	public Set getRoles() {
		return roles;
	}

	@SuppressWarnings("rawtypes")
	public void setRoles(Set roles) {
		this.roles = roles;
	}


}


