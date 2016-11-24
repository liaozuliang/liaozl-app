package com.liaozl.photo.util.page;

import java.util.ArrayList;
import java.util.List;


/**
 * @author hxy
 * @date 2011-10-25
 * 
 */
public class PaginationParameter {
	private static final int DEFAULT_PAGE = 1;
	public static int DEFAULT_PAGE_SIZE = 20;
	private int totalRecord;//总数
	private int pageSize = DEFAULT_PAGE_SIZE;//每页多少条
	private int currentPage = DEFAULT_PAGE;//当前页
	private int totalPage;//总页数
	private int nextPage;//下一页
	private int prevPage;//上一页
	private List<Integer> listPage=new ArrayList<Integer>();//
	


	public List<Integer> getListPage() {
		return listPage;
	}

	public void setListPage(List<Integer> listPage) {
		this.listPage = listPage;
	}

	private void calculate(){
		totalPage=totalRecord%pageSize==0? (totalRecord / pageSize): (totalRecord / pageSize) + 1;
		if(totalPage<currentPage+1){
			nextPage=totalPage;
		}else{
			nextPage=currentPage+1;
		}
		if(currentPage>1){
			prevPage=currentPage-1;
		}else{
			prevPage=1;
		}
		
		for (int i = 1; i <= totalPage; i++) {
			if(i==1              ||
			   i==2              ||
			   i==currentPage - 2||
			   i==currentPage - 1||
			   i==currentPage    ||
			   i==currentPage + 1    ||
			   i==currentPage + 2    ||
			   i==totalPage-1    ||
			   i==totalPage    
			   ){
				listPage.add(i);
			}else if(i==currentPage - 3||i==currentPage + 3){
				listPage.add(0);
			}			
		}
	}
	
	public PaginationParameter() {
		super();
	}
	
	public PaginationParameter(int totalRecord) {
		super();
		this.totalRecord = totalRecord;
		calculate();
	}

	public PaginationParameter(int totalRecord, int pageSize, int currentPage) {
		super();
		this.totalRecord = totalRecord;
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		calculate();
//		System.out.println("");
	}

	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getNextPage() {
		return nextPage;
	}
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	public int getPrevPage() {
		return prevPage;
	}
	public void setPrevPage(int prevPage) {
		this.prevPage = prevPage;
	}
	public static void main(String[] args) {
		PaginationParameter a=new PaginationParameter(0,7,1);
		List<Integer> lst= a.getListPage();
		for (Integer integer : lst) {
			System.out.println(integer);
		}
	}
}
