package com.simple.wildfishingnote.bean;

public class RelayResultStatistics {

	private String id;
	private String resultId;
	private String pointId;
	private String fishTypeId;
	private String weight;
	private String count;
	private String hookFlag;
	private String pointName;
	private String fishTypeName;
	private String hookFlagName;
	
	public String getHookFlagName() {
        return hookFlagName;
    }
    public void setHookFlagName(String hookFlagName) {
        this.hookFlagName = hookFlagName;
    }
    public String getPointName() {
		return pointName;
	}
	public void setPointName(String pointName) {
		this.pointName = pointName;
	}
	public String getFishTypeName() {
		return fishTypeName;
	}
	public void setFishTypeName(String fishTypeName) {
		this.fishTypeName = fishTypeName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getResultId() {
		return resultId;
	}
	public void setResultId(String resultId) {
		this.resultId = resultId;
	}
	public String getPointId() {
		return pointId;
	}
	public void setPointId(String pointId) {
		this.pointId = pointId;
	}
	public String getFishTypeId() {
		return fishTypeId;
	}
	public void setFishTypeId(String fishTypeId) {
		this.fishTypeId = fishTypeId;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getHookFlag() {
		return hookFlag;
	}
	public void setHookFlag(String hookFlag) {
		this.hookFlag = hookFlag;
	}
	
}
