/**
 * 
 */
package com.framework.dao;

import java.util.List;

import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

/**
 * @author Administrator
 *
 */
public abstract class SQLUtils {
	
	/**
	 * 通过查询属性字段创建ProjectionList
	 * @param properties 所需查询的属性字段，多个以","间隔
	 * @return
	 */
	public static ProjectionList createProjectionList(String properties) {
		return createProjectionList(properties.split(","));
	}
	
	public static ProjectionList createProjectionList(List<String> list) {
		return createProjectionList((String[]) list.toArray());
	}
	
	public static ProjectionList createProjectionList(String[] properties) {
		ProjectionList projectionList = Projections.projectionList();
		for (String property : properties)
			projectionList.add(Projections.property(property), property);
		return projectionList;
	}
}
