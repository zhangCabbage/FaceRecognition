package com.zhang.util;

import java.io.Serializable;

/**
 * 我的班级类，包括班级的ID、名字、人数、是否被选择了！
 * @author zhang
 *
 */
public class MyClass implements Serializable{
	private int classID;
	private String className;
	private int classStuNumber;
	private boolean isSelected;//用来显示此班级是否被选择
	
	public int getClassID() {
		return classID;
	}
	public void setClassID(int classID) {
		this.classID = classID;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getClassStuNumber() {
		return classStuNumber;
	}
	public void setClassStuNumber(int classStuNumber) {
		this.classStuNumber = classStuNumber;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	@Override
	public String toString() {
		return "classID = "+this.classID +"，className = " +this.className + "，isSelected = " + this.isSelected;
	}
	
}
