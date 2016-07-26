/**
 * 
 */
package com.framework.core;

import java.io.Serializable;

/**
 */
@SuppressWarnings("serial")
public class Page implements Serializable {
	//当前页
	private Integer currentPage;
	//页行数
	private Integer pageSize;
	//总行数
	private Integer totalRows;
	//排序字段名
	private String orderColumn;
	//排序方式
	private boolean orderASC;
	
	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public boolean isOrderASC() {
		return orderASC;
	}

	public void setOrderASC(boolean orderASC) {
		this.orderASC = orderASC;
	}
}
