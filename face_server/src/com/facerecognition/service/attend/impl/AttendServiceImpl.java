package com.facerecognition.service.attend.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.facerecognition.service.attend.AttendServiceDAO;
import com.framework.dao.impl.GenericHBDao;
import com.framework.dao.impl.GenericJDBCDao;

public class AttendServiceImpl implements AttendServiceDAO  {

	private GenericJDBCDao genericJDBCDao;//偏向于使用修改数据库中的数据使用
	private GenericHBDao genericHBDao;//偏向于查找数据库中的数据
	
	public void setGenericJDBCDao(GenericJDBCDao genericJDBCDao) {
		this.genericJDBCDao = genericJDBCDao;
	}

	public void setGenericHBDao(GenericHBDao genericHBDao) {
		this.genericHBDao = genericHBDao;
	}

	@Override
	public List<Map<String, Object>> findStuAttend(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> result = null ; 
		int studentID = Integer.parseInt( paraMap.get("studentID").toString() );
		
		String sql = "select time, flag from attend where studentID = ?" ; 
		List<Object> args = new ArrayList<Object>();
		args.add(studentID);
		System.out.println("sql:"+sql+"，args:"+args.toString());
		
		try {
			result = genericHBDao.findMapBySQL(sql, args.toArray());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		return result;
		//
	}
	
	@Override
	public List<Map<String, Object>> findAllStuOfClassAttend(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> result = null ; 
		int classID = Integer.parseInt( paraMap.get("classID").toString() );
		long time = Long.parseLong( paraMap.get("time").toString() );
		
		String sql = "select stuName, flag from attend, student where attend.classID=? AND attend.time = ? AND attend.studentID = student.stuID" ; 
		List<Object> args = new ArrayList<Object>();
		args.add(classID);
		args.add(time);
		System.out.println("sql:"+sql+"，args:"+args.toString());
		
		try {
			result = genericHBDao.findMapBySQL(sql, args.toArray());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> findClassAttend(Map<String, Object> paraMap) {
		List<Map<String, Object>> result = null ; 
		
		int classID = Integer.parseInt( paraMap.get("classID").toString() );
		long startTime = Long.parseLong( paraMap.get("startDate").toString() );
		long endTime = Long.parseLong( paraMap.get("endDate").toString() );
		
		String sql = "select time, count(flag) total from attend WHERE flag=1 GROUP BY time, classID HAVING classID = ? and time > ? and time < ?" ; 
		List<Object> args = new ArrayList<Object>();
		args.add(classID);
		args.add(startTime);
		args.add(endTime);
		System.out.println("sql:"+sql+"，args:"+args.toString());
		
		try {
			result = genericHBDao.findMapBySQL(sql, args.toArray());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		return result;
	}

	@Override
	public Integer insertAttend(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		Integer result = null;
		//插入所需用的四个参数
		String time = paraMap.get("time").toString();
		int flag = Integer.parseInt( paraMap.get("flag").toString() );
		int studentID = Integer.parseInt( paraMap.get("studentID").toString() );
		int classID = Integer.parseInt( paraMap.get("classID").toString() );
		
		String sql = "insert into attend(time, flag, studentID, classID) value(?, ?, ?, ?)";
		
		List<Object> args = new ArrayList<Object>();
		args.add(time);
		args.add(flag);
		args.add(studentID);
		args.add(classID);
		
		System.out.println("sql:"+sql+"，args:"+args.toString());
		
		try {
			result = genericJDBCDao.Insert(sql, args.toArray());
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return result;
	} 
	
}
