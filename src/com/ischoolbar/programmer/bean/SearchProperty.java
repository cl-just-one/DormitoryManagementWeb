package com.ischoolbar.programmer.bean;

public class SearchProperty {
	private String key; // 查询的字段
	private Object value; // 查询的字段值
	private Operator operator;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	
}
