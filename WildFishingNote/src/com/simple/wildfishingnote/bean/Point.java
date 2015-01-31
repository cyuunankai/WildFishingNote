package com.simple.wildfishingnote.bean;

public class Point {
	String id;
	String placeId;
	String rodLengthId;
	String rodLengthName;
	String depth;
	String lureMethodId;
	String lureMethodName;
	String baitId;
	String baitName;
	boolean selected;
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getRodLengthName() {
		return rodLengthName;
	}
	public void setRodLengthName(String rodLengthName) {
		this.rodLengthName = rodLengthName;
	}
	public String getLureMethodName() {
		return lureMethodName;
	}
	public void setLureMethodName(String lureMethodName) {
		this.lureMethodName = lureMethodName;
	}
	public String getBaitName() {
		return baitName;
	}
	public void setBaitName(String baitName) {
		this.baitName = baitName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public String getRodLengthId() {
		return rodLengthId;
	}
	public void setRodLengthId(String rodLengthId) {
		this.rodLengthId = rodLengthId;
	}
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public String getLureMethodId() {
		return lureMethodId;
	}
	public void setLureMethodId(String lureMethodId) {
		this.lureMethodId = lureMethodId;
	}
	public String getBaitId() {
		return baitId;
	}
	public void setBaitId(String baitId) {
		this.baitId = baitId;
	}
	
	
}
