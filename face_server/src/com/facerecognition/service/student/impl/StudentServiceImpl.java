package com.facerecognition.service.student.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.facerecognition.service.student.StudentServiceDAO;
import com.framework.dao.impl.GenericHBDao;
import com.framework.dao.impl.GenericJDBCDao;

public class StudentServiceImpl implements StudentServiceDAO {

	private GenericJDBCDao genericJDBCDao;//偏向于使用修改数据库中的数据使用
	private GenericHBDao genericHBDao;//偏向于查找数据库中的数据
	
	public void setGenericJDBCDao(GenericJDBCDao genericJDBCDao) {
		this.genericJDBCDao = genericJDBCDao;
	}

	public void setGenericHBDao(GenericHBDao genericHBDao) {
		this.genericHBDao = genericHBDao;
	}
	
	/**
	 * 查找对应classID下的所有学生
	 */
	@Override
	public List<Map<String, Object>> findAllStudent(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> result = null ; 
		int classID = Integer.parseInt( paraMap.get("classID").toString() );
		
		String sql = "select stuID, stuName, stuGender, stuPhone from student where classID = ?" ; 
		List<Object> args = new ArrayList<Object>();
		args.add(classID);
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
	
	/**
	 * 给班级中添加学生，这里要分两个步骤来完成
	 */
	@Override
	public Integer insertStudent(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		Integer result = null;
		//插入所需用的四个参数
		String stuName = paraMap.get("studentName").toString();
		String stuGender = paraMap.get("studentGender").toString();
		String stuPhone = paraMap.get("studentPhone").toString();
		int classID = Integer.parseInt( paraMap.get("classID").toString() );
		
		String sql = "insert into student(stuName, stuGender, stuPhone, classID) value(?, ?, ?, ?)";
		
		List<Object> args = new ArrayList<Object>();
		args.add(stuName);
		args.add(stuGender);
		args.add(stuPhone);
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

	/**
	 * 删除一个student只需要此student的ID即可
	 */
	@Override
	public Integer deleteStudent(Map<String, Object> paraMap) {
		Integer result = null ;
		int stuID = Integer.parseInt( paraMap.get("studentID").toString() );
		
		String sql = "delete from student where stuID=?" ; 
		
		List<Object> args = new ArrayList<Object>();
		args.add(stuID);
		
		System.out.println("sql:"+sql+"，args:"+args.toString());
		try {
			result = genericJDBCDao.execute(sql, args.toArray());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}

}
