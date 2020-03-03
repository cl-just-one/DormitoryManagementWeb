package com.ischoolbar.programmer.entity;
/**
 * 宿舍实体
 * */
public class Dormitory {
	private int id;
	private String sn; // 编号
	private int buildingId; // 所属楼宇
	private String floor; // 所属层数
	private int maxNumber; // 最大入住人数
	private int livedNumber; // 已住人数
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public int getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public int getMaxNumber() {
		return maxNumber;
	}
	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
	}
	public int getLivedNumber() {
		return livedNumber;
	}
	public void setLivedNumber(int livedNumber) {
		this.livedNumber = livedNumber;
	}
	
}
