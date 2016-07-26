/**
 * 
 */
package com.framework.core;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * 
 */
public class ListRowMapper implements RowMapper<List<Object>> {

	@Override
	public List<Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
		List<Object> list = new ArrayList<Object>();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		for (int i = 1; i <= columnCount; i++)
			list.add(rs.getObject(i));
		return list;
	}
}
