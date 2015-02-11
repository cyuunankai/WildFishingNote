package com.simple.wildfishingnote.bean;

import java.util.List;

public class FishResult {
	
	private String id;
	private List<RelayResultStatistics> statisticsList;
	private String file_path1;
	private String file_path2;
	private String file_path3;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<RelayResultStatistics> getStatisticsList() {
		return statisticsList;
	}
	public void setStatisticsList(List<RelayResultStatistics> statisticsList) {
		this.statisticsList = statisticsList;
	}
	public String getFile_path1() {
		return file_path1;
	}
	public void setFile_path1(String file_path1) {
		this.file_path1 = file_path1;
	}
	public String getFile_path2() {
		return file_path2;
	}
	public void setFile_path2(String file_path2) {
		this.file_path2 = file_path2;
	}
	public String getFile_path3() {
		return file_path3;
	}
	public void setFile_path3(String file_path3) {
		this.file_path3 = file_path3;
	}

}
