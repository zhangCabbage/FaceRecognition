/**
 * 
 */
package com.framework.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.framework.core.Page;

/**
 */
public interface IGenericJDBCDao {
	
	int findCount(String sql, Object...args);
	
	<I> I findForObject(Class<I> clazz, String sql, Object...args);
	
	Map<String, Object> findMapBySQL(String sql, Object... args);
	
	<T> T findTBySQL(Class<T> c, String sql, Object...args);
	
	List<List<Object>> findListBySQL(String sql, Object...args);
	
	<T> List<T> findTsBySQL(Class<T> c, String sql, Object...args);
	
	<T> List<T> findTsPageBySQL(Class<T> c, Page page, String regex, String sql, Object...args);
	
	<T> List<T> findTsBySQL(RowMapper<T> rowMapper, String sql, Object...args);
	
	List<Map<String, Object>> findMapsBySQL(String sql, Object...args);
	
	<T> List<T> findTsPageBySQL(RowMapper<T> rowMapper, Page page, String regex, String sql, Object...args);
	
	Integer execute(String sql, Object... args);
	Integer Insert(final String sql,final Object... args);
}
