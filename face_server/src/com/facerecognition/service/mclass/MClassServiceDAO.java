package com.facerecognition.service.mclass;

import java.util.List;
import java.util.Map;

/**
 * 对数据库中的class表进行操作
 * @author zhang
 *
 */
public interface MClassServiceDAO {
	//返回的是一连串的class列表
	public List< Map<String,Object> > findAllClass(Map<String,Object> paraMap);
	public Integer insertClass(Map<String,Object> paraMap);
	public Integer deleteClass(Map<String,Object> paraMap);
	public Integer addUpdateClass(Map<String,Object> paraMap);
	public Integer deleteUpdateClass(Map<String,Object> paraMap);
}
