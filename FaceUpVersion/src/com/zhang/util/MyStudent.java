package com.zhang.util;

import java.io.Serializable;

/**
 * 学生类，包括学生的姓名，性别，ID等信息
 * @author zhang
 *
 */
public class MyStudent implements Serializable{
	private int studentID;
	private String studentName;
	private String studentGender;
	private String studentPhone;
	public int getStudentID() {
		return studentID;
	}
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentGender() {
		return studentGender;
	}
	public void setStudentGender(String studentGender) {
		this.studentGender = studentGender;
	}
	public String getStudentPhone() {
		return studentPhone;
	}
	public void setStudentPhone(String studentPhone) {
		this.studentPhone = studentPhone;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "student ID --> "+this.studentID+", Name --> "+this.studentName;
	}
}
