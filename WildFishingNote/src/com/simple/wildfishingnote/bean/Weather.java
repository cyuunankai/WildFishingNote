package com.simple.wildfishingnote.bean;

import java.util.List;

public class Weather {

	String date;
    Astronomy astronomy;
    List<Hourly> hourlyList;
    public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Astronomy getAstronomy() {
		return astronomy;
	}
	public void setAstronomy(Astronomy astronomy) {
		this.astronomy = astronomy;
	}
	public List<Hourly> getHourlyList() {
		return hourlyList;
	}
	public void setHourlyList(List<Hourly> hourlyList) {
		this.hourlyList = hourlyList;
	}
	public String getMaxtempC() {
		return maxtempC;
	}
	public void setMaxtempC(String maxtempC) {
		this.maxtempC = maxtempC;
	}
	public String getMintempC() {
		return mintempC;
	}
	public void setMintempC(String mintempC) {
		this.mintempC = mintempC;
	}
	String maxtempC;
    String mintempC;
}
