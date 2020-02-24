package com.ischoolbar.programmer.bean;

public class Page {
	private int pageNumber; // 当前页码
	private int pageSize; // 每页显示数量
	private int offset; // 对应数据库的偏移量
	
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
	
	
}
