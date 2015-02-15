package com.simple.wildfishingnote.bean;

import java.util.List;

public class Campaign {
    
    private String id;
    private String startTime;
    private String endTime;
    private String summary;
    private String placeId;
    public List<String> getPointIdList() {
        return pointIdList;
    }
    public void setPointIdList(List<String> pointIdList) {
        this.pointIdList = pointIdList;
    }
    public List<RelayCamapignStatisticsResult> getStatisticsList() {
        return statisticsList;
    }
    public void setStatisticsList(List<RelayCamapignStatisticsResult> statisticsList) {
        this.statisticsList = statisticsList;
    }
    public List<String> getPicList() {
        return picList;
    }
    public void setPicList(List<String> picList) {
        this.picList = picList;
    }
    private List<String> pointIdList;
    private List<RelayCamapignStatisticsResult> statisticsList;
    private List<String> picList;
    
    public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
}