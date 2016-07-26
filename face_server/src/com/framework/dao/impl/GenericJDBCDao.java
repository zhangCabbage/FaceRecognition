/**
 * 
 */
package com.framework.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.framework.core.BeanRowMapper;
import com.framework.core.ListRowMapper;
import com.framework.core.Page;
import com.framework.dao.IGenericJDBCDao;
import com.mysql.jdbc.Statement;

/**
 * @author dranson
 * 2010-12-13
 */
public class GenericJDBCDao extends JdbcDaoSupport implements IGenericJDBCDao {

	@Override
	public int findCount(String sql, Object... args) {
		return getJdbcTemplate().queryForInt(sql, args);
	}
	
	@Override
	public <I> I findForObject(Class<I> clazz, String sql, Object... args) {
		return getJdbcTemplate().queryForObject(sql, clazz, args);
	}
	
	@Override
	public Map<String, Object> findMapBySQL(String sql, Object... args) {
		return getJdbcTemplate().queryForMap(sql, args);
	}

	@Override
	public <T> T findTBySQL(Class<T> c, String sql, Object... args) {
		List<T> list = findTsBySQL(c, sql, args);
		if (list.size() != 0)
			return list.get(0);
		return null;
	}

	@Override
	public List<List<Object>> findListBySQL(String sql, Object... args) {
		return findTsBySQL(new ListRowMapper(), sql, args);
	}

	@Override
	public <T> List<T> findTsBySQL(RowMapper<T> rowMapper, String sql, Object... args) {
		return getJdbcTemplate().query(sql, args, rowMapper);
	}
	
	@Override
	public List<Map<String, Object>> findMapsBySQL(String sql, Object...args) {
		return getJdbcTemplate().queryForList(sql, args);
	}

	@Override
	public <T> List<T> findTsPageBySQL(RowMapper<T> rowMapper, Page page, String regex, String sql, Object... args) {
		StringBuffer buf = new StringBuffer();
		buf.append("select count(*) from (").append(sql).append(")");
		page.setTotalRows(getJdbcTemplate().queryForInt(buf.toString(), args));
		buf.setLength(0);
		buf.append("select * from (").append(sql).append(") ").append(regex);
		Object[] objs = new Object[args.length + 2];
		for (int i = 0; i < args.length; i++)
			objs[i] = args[i];
		objs[objs.length - 2] = page.getPageSize() * (page.getCurrentPage() - 1);
		objs[objs.length - 1] = page.getPageSize();
		return getJdbcTemplate().query(buf.toString(), objs, rowMapper);
	}

	@Override
	public <T> List<T> findTsBySQL(Class<T> c, String sql, Object... args) {
		BeanRowMapper<T> rowMapper = new BeanRowMapper<T>(c);
		return findTsBySQL(rowMapper, sql, args);
	}
	
	@Override
	public <T> List<T> findTsPageBySQL(Class<T> c, Page page, String regex, String sql, Object... args) {
		BeanRowMapper<T> rowMapper = new BeanRowMapper<T>(c);
		return findTsPageBySQL(rowMapper, page, regex, sql, args);
	}
	
	@Override
	public Integer execute(String sql, final Object... args) {
		return getJdbcTemplate().execute(sql, new PreparedStatementCallback<Integer>() {
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				for (int i = 0; i < args.length; i++)
					ps.setObject(i + 1, args[i]);
				return ps.executeUpdate();
			}			
		});
	}
	
	/**
	 * 
	 * @param sql
	 * @return 插入记录，返回记录的主键值，用于主键是自增长类型的记录
	 */
	public Integer Insert(final String sql,final Object... args){
		int result=0;
		KeyHolder keyHolder=new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
					PreparedStatement ps =conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
					for (int i = 0; i < args.length; i++)
						ps.setObject(i + 1, args[i]);
					return ps;				
			}			 
		},keyHolder);
		result=keyHolder.getKey().intValue();
		return result;
	}
}
