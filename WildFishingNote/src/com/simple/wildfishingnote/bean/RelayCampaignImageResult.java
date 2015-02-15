package com.simple.wildfishingnote.bean;


public class RelayCampaignImageResult {
	
	private String id;
	private String campaignId;
	private String filePath;
	
	public String getCampaignId() {
        return campaignId;
    }
    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
