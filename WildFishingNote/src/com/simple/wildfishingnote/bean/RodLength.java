package com.simple.wildfishingnote.bean;

public class RodLength {

	String id;
	String name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
    public String toString() {
        return name + "米";
    }
}
