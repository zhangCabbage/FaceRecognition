package com.facerecognition.service.student;

import java.util.List;
import java.util.Map;

public interface StudentServiceDAO {
	public List< Map<String, Object> > findAllStudent(Map<String,Object> paraMap);
	public Integer insertStudent(Map<String, Object> paraMap);
	public Integer deleteStudent(Map<String, Object> paraMap);
}
