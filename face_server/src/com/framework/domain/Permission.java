package com.framework.domain;

public class Permission {
	/**
	 * 权限名称
	 */
	private String permissionName;

	/**
	 * 权限编号
	 */
	private Integer permissionId;

	/**
	 * 父权限编号
	 */
	private Integer pre_permissionId;

	/**
	 * 默认构造函数
	 */
	public Permission() {
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public Integer getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	public Integer getPre_permissionId() {
		return pre_permissionId;
	}

	public void setPre_permissionId(Integer pre_permissionId) {
		this.pre_permissionId = pre_permissionId;
	}


	
	

}
