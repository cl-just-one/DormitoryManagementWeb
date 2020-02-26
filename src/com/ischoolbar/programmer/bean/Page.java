package com.ischoolbar.programmer.bean;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {
	private int pageNumber; // 当前页码
	private int pageSize; // 每页显示数量
	private int offset; // 对应数据库的偏移量
	private int total;
	private List<T> content = new ArrayList<T>();
	private List<SearchProperty> searchOperties = new ArrayList<SearchProperty>();
	
	public Page(int pageNumber, int pageSize) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.offset = (pageNumber - 1) * pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public List<T> getContent() {
		return content;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public List<SearchProperty> getSearchOperties() {
		return searchOperties;
	}

	public void setSearchOperties(List<SearchProperty> searchOperties) {
		this.searchOperties = searchOperties;
	}
	
}
