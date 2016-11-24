package com.liaozl.dao.base;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: Page
 * @Description: 封装分页的page对象
 * @author Robert
 * @date 2014-9-28 下午5:05:05
 * 
 */
public class Page<E> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 总页数
	 */
	private int totalPage;

	/**
	 * 当前页数
	 */
	private int page;

	/**
	 * 分页获得的数据
	 */
	private List<E> data;

	/**
	 * 显示多少记录
	 */
	private int rowNumber;

	/**
	 * 总记录数
	 */
	private int totalRecord;

	/**
	 * 是否查询记录总数
	 */
	private boolean queryRecordCount = true;

	/**
	 * 是否存在用户定义的计算记录总数的sql 
	 * 如果=ture系统会默认去读sqlId +“_Count” 作为新sqlId的sql语句查询记录总数，
	 * 如果=false（默认false），系统会自动拼接一个统计条数的sql语句查询记录总数。
	 */

	private boolean haveCountSql=false;

	public Page() {
		totalPage = 0;
		page = 1;
		rowNumber = 10;
		totalRecord = 0;
	}
	
	/**
	 * @param page
	 *            当前页码
	 * @param rowNumber
	 *            页大小
	 */
	public Page(int page, int rowNumber) {
		this.page = page < 1 ? 1 : page;
		this.rowNumber = rowNumber < 1 ? 1 : rowNumber;
	}

	public List<E> getData() {
		return data;
	}

	public void setData(List<E> data) {
		this.data = data;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
		int totalPage = totalRecord % rowNumber == 0 ? totalRecord / rowNumber
				: totalRecord / rowNumber + 1;
		this.setTotalPage(totalPage);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	/**
	 * 取当前页的第一行数据在数据库中的行号，在操作数据库的时候有用
	 * 
	 * @return
	 */
	public int getRowOffset() {
		return (getPage() - 1) * getRowNumber();
	}

	@Override
	public String toString() {
		return "Page [totalPage=" + totalPage + ", page=" + page + ", data="
				+ data + ", rowNumber=" + rowNumber + ", totalRecord="
				+ totalRecord + ", haveCountSql=" + haveCountSql
				+ "]";
	}

	public boolean isQueryRecordCount() {
		return queryRecordCount;
	}

	public void setQueryRecordCount(boolean queryRecordCount) {
		this.queryRecordCount = queryRecordCount;
	}

	public boolean isHaveCountSql() {
		return haveCountSql;
	}

	public void setHaveCountSql(boolean haveCountSql) {
		this.haveCountSql = haveCountSql;
	}

}
