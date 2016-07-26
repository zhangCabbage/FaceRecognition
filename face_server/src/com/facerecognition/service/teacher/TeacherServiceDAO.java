package com.facerecognition.service.teacher;

import java.util.List;
import java.util.Map;

public interface TeacherServiceDAO {
	
	public Integer insertTeacher(Map<String,Object> paraMap);
	public Map<String,Object> checkLogin(Map<String,Object> paraMap);
	public List< Map<String,Object> > findAllTeacher(Map<String,Object> paraMap);
	public String modifyPassword(Map<String,Object> paraMap);
	public Map<String,Object> selfInfo(Map<String,Object> paraMap);
}
