package com.facerecognition.service.mclass.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.facerecognition.service.mclass.MClassServiceDAO;
import com.framework.dao.impl.GenericHBDao;
import com.framework.dao.impl.GenericJDBCDao;

public class MClassServiceImpl implements MClassServiceDAO {

	private GenericJDBCDao genericJDBCDao;//偏向于使用修改数据库中的数据使用
	private GenericHBDao genericHBDao;//偏向于查找数据库中的数据
	
	public void setGenericJDBCDao(GenericJDBCDao genericJDBCDao) {
		this.genericJDBCDao = genericJDBCDao;
	}

	public void setGenericHBDao(GenericHBDao genericHBDao) {
		this.genericHBDao = genericHBDao;
	} 
	
	@Override
	public List<Map<String, Object>> findAllClass(Map<String, Object> paraMap) {
		//具体实现从数据库中获得所有的class表项
		List<Map<String, Object>> result = null;
		int teacherID = Integer.parseInt( paraMap.get("teacherID").toString() );
		
		String sql = "select classID, className, classStuNum FROM class where teacherID = ?";
//		String sql = "select classID,className from class";//这里我们只查找class班级的ID和班级的Name
		List<Object> args = new ArrayList<Object>();
		args.add(teacherID);
		System.out.println("sql:"+sql+"，args:"+args.toString());
		
		try {
			result = genericHBDao.findMapBySQL(sql, args.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	/**
	 * 返回记录的主键值
	 */
	@Override
	public Integer insertClass(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		Integer result = null;
		String className = paraMap.get("className").toString();
		int teacherID = Integer.parseInt( paraMap.get("teacherID").toString() );
		int schoolID = Integer.parseInt( paraMap.get("schoolID").toString() );
		String sql = "insert into class(className, teacherID, schoolID) values(?, ?, ?)";
		List<Object> args = new ArrayList<Object>();
		args.add(className);
		args.add(teacherID);
		args.add(schoolID);
		System.out.println("sql:"+sql+"，args:"+args.toString());
		try {
			result = genericJDBCDao.Insert(sql, args.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public Integer deleteClass(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		Integer result = null;
		int classID = Integer.parseInt( paraMap.get("classID").toString() );
		
		String sql = "delete from class where classID=?" ; 
		List<Object> args = new ArrayList<Object>();
		args.add(classID);
		System.out.println("sql:"+sql+"，args:"+args.toString());
		try {
			result = genericJDBCDao.execute(sql, args.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 更新class数据库中stuNum的值，每次添加一个学生，那么就更新一次，+1
	 */
	@Override
	public Integer addUpdateClass(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		Integer result = null;
		int classID = Integer.parseInt( paraMap.get("classID").toString() );
		
		String sql = "update class set classStuNum = classStuNum + 1 where classID = ?";
		
		List<Object> args = new ArrayList<Object>();
		args.add(classID);
		
		System.out.println("sql:"+sql+"，args:"+args.toString());
		try {
			result = genericJDBCDao.execute(sql, args.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 更新class数据库中stuNum的值，每次删除一个学生，那么就更新一次，-1
	 */
	@Override
	public Integer deleteUpdateClass(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		Integer result = null;
		int classID = Integer.parseInt( paraMap.get("classID").toString() );
		
		String sql = "update class set classStuNum = classStuNum - 1 where classID = ?";
		
		List<Object> args = new ArrayList<Object>();
		args.add(classID);
		
		System.out.println("sql:"+sql+"，args:"+args.toString());
		try {
			result = genericJDBCDao.execute(sql, args.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
