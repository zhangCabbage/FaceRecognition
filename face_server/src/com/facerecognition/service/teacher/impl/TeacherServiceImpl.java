package com.facerecognition.service.teacher.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.facerecognition.service.teacher.TeacherServiceDAO;
import com.framework.dao.impl.GenericHBDao;
import com.framework.dao.impl.GenericJDBCDao;

public class TeacherServiceImpl implements TeacherServiceDAO{

	private GenericJDBCDao genericJDBCDao;//两个有关数据库的基本属性
	private GenericHBDao genericHBDao;
	
	public void setGenericJDBCDao(GenericJDBCDao genericJDBCDao) {
		this.genericJDBCDao = genericJDBCDao;
	}

	public void setGenericHBDao(GenericHBDao genericHBDao) {
		this.genericHBDao = genericHBDao;
	} 
	
	@Override
	public Map<String, Object> checkLogin(Map<String, Object> paraMap) {
		//具体实现验证登陆用户信息的功能
		Map<String, Object> userMap = null ; 
		String userTeacher = paraMap.get("teacher").toString();//用户邮箱
		String userPassword = paraMap.get("password").toString();//密码
		
		String sql = "select teacherID,teacherName,teacherPassword from teacher where teacherMail=? and teacherPassword=?" ; 
		List<Object> list = new ArrayList<Object>();
		list.add(userTeacher);
		list.add(userPassword);
		System.out.println("sql:"+sql+"list:"+list.toString());
		
		try {
			userMap = genericJDBCDao.findMapBySQL(sql, list.toArray());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return userMap;
		}
		return userMap;
	}

	@Override
	public String modifyPassword(Map<String, Object> paraMap) {
		//具体实现修改用户密码的功能
		String result =null;
		String userNumbr=(String)paraMap.get("usernumber");
		String password=(String) paraMap.get("password");
		String sql = "update teacher set password=? where usernumber=?";
		List<Object> list = new ArrayList<Object>();
		list.add(password);
		list.add(userNumbr);
		System.out.println("sql:"+sql+"args:"+list.toString());
		
		try{
			Integer row =genericJDBCDao.execute(sql, list.toArray());
			if(row!=0 && row!=null){
				result=password;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Map<String, Object> selfInfo(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer insertTeacher(Map<String, Object> paraMap) {
		//具体实现注册用户的功能
		Integer result = null;
		//插入所需用的三个参数
		String teacherName = paraMap.get("teacherName").toString();
		String teacherMail = paraMap.get("teacherMail").toString();
		String teacherPassword = paraMap.get("teacherPassword").toString();
		
		String sql = "insert into teacher(teacherName, teacherMail, teacherPassword) value(?, ?, ?)";
		
		List<Object> args = new ArrayList<Object>();
		args.add(teacherName);
		args.add(teacherMail);
		args.add(teacherPassword);
		
		System.out.println("sql:"+sql+"，args:"+args.toString());
		
		try {
			result = genericJDBCDao.Insert(sql, args.toArray());
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> findAllTeacher(Map<String, Object> paraMap) {
		List<Map<String, Object>> result = null;
		
		String sql = "select teacherMail FROM teacher";
//		String sql = "select classID,className from class";//这里我们只查找class班级的ID和班级的Name
		
		try {
			result = genericHBDao.findMapBySQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

}
