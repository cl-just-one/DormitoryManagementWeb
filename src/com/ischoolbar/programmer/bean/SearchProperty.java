package com.ischoolbar.programmer.bean;

public class SearchProperty {
	private String key; // ��ѯ���ֶ�
	private Object value; // ��ѯ���ֶ�ֵ
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