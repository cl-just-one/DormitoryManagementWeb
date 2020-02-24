package com.ischoolbar.programmer.bean;

public class Page {
	private int pageNumber; // ��ǰҳ��
	private int pageSize; // ÿҳ��ʾ����
	private int offset; // ��Ӧ���ݿ��ƫ����
	
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
