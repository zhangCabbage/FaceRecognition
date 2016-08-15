package com.facerecognition.service.attend;

import java.util.List;
import java.util.Map;

public interface AttendServiceDAO {
	
	public List< Map<String,Object> > findStuAttend(Map<String,Object> paraMap);
	public List< Map<String,Object> > findAllStuOfClassAttend(Map<String,Object> paraMap);
	public List< Map<String,Object> > findClassAttend(Map<String,Object> paraMap);
	public Integer insertAttend(Map<String,Object> paraMap);
}
