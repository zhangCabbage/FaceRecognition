package com.zhang.util;

public class MyAttend {
	private long time;
	
	private boolean flag;
	
	private int studentID;
	
	private int classID;
	
	private int totalNum;//此处特别用在classAttend中
	
	public int getTotalNum() {
		return totalNum;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public int getStudentID() {
		return studentID;
	}
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	public int getClassID() {
		return classID;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public void setClassID(int classID) {
		this.classID = classID;
	}
}
