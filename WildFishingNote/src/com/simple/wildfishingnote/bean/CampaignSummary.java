package com.simple.wildfishingnote.bean;

import java.util.List;

public class CampaignSummary {

    private String id;
    private String title;
    private String date;
    private String summary;
    private String imagePath;
    private List<String> fishResultImageList;
    
    public List<String> getFishResultImageList() {
        return fishResultImageList;
    }
    public void setFishResultImageList(List<String> fishResultImageList) {
        this.fishResultImageList = fishResultImageList;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
