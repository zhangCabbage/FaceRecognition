package com.framework.domain;

public class Permission {
	/**
	 * Ȩ������
	 */
	private String permissionName;

	/**
	 * Ȩ�ޱ��
	 */
	private Integer permissionId;

	/**
	 * ��Ȩ�ޱ��
	 */
	private Integer pre_permissionId;

	/**
	 * Ĭ�Ϲ��캯��
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
