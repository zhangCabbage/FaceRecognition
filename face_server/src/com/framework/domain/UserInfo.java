package com.framework.domain;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 

 */
//@SuppressWarnings("serial")
//public class UserInfo   implements UserDetails {
  public class UserInfo {
	
	private Integer infoId;

	//private String oldPassword;
 
	private String email;
	
	private Timestamp registTime;
	
	private Integer sex;
	
	private Date birthday;
	
	private String hometown;
	
	private String head;
	
	private String sinature;
	/**
	 * 当前积分
	 */
	private Integer score;
	/**
	 * 用户权限
	 */
	private Integer permission;
	/**
	 * 用户登录状态
	 */
	private Integer status;
	
	public UserInfo() {
	}

	public Integer getInfoId() {
		return infoId;
	}

	public void setInfoId(Integer infoId) {
		this.infoId = infoId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getRegistTime() {
		return registTime;
	}

	public void setRegistTime(Timestamp registTime) {
		this.registTime = registTime;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getSinature() {
		return sinature;
	}

	public void setSinature(String sinature) {
		this.sinature = sinature;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getPermission() {
		return permission;
	}

	public void setPermission(Integer permission) {
		this.permission = permission;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public UserInfo(Integer infoId, String email, Timestamp registTime,
			Integer sex, Date birthday, String hometown, String head,
			String sinature, Integer score, Integer permission, Integer status) {
		super();
		this.infoId = infoId;
		this.email = email;
		this.registTime = registTime;
		this.sex = sex;
		this.birthday = birthday;
		this.hometown = hometown;
		this.head = head;
		this.sinature = sinature;
		this.score = score;
		this.permission = permission;
		this.status = status;
	}
	



}
