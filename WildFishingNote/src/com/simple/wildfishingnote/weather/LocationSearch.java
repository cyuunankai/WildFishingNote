package com.simple.wildfishingnote.weather;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

import com.simple.wildfishingnote.bean.LocationData;

public class LocationSearch extends WwoApi {
	public static final String FREE_API_ENDPOINT = "http://api.worldweatheronline.com/free/v2/search.ashx";
	public static final String PREMIUM_API_ENDPOINT = "http://api.worldweatheronline.com/free/v1/search.ashx";
	
	public LocationSearch(boolean freeAPI) {
		super(freeAPI);
		
		if(freeAPI) {
			apiEndPoint = FREE_API_ENDPOINT;
		} else {
			apiEndPoint = PREMIUM_API_ENDPOINT;
		}
	}

	public LocationData callAPI(String query) {
		return getLocationSearchData(getInputStream(apiEndPoint + query));
	}
	
	public LocationData getLocationSearchData(InputStream is) {
		LocationData location = null;
		
		try {
			Log.d("WWO", "getLocationSearchData");
			
	        XmlPullParser xpp = getXmlPullParser(is);
	        
	        location = new LocationData();
	
	        location.setAreaName(getDecode(getTextForTag(xpp, "areaName")));
	        location.setCountry(getDecode(getTextForTag(xpp, "country")));
	        location.setRegion(getDecode(getTextForTag(xpp, "region")));
	        
		} catch (Exception e) {
			
		}
	
		return location;
	}
	
	public class Params extends RootParams {
		String query;					//required
		String num_of_results="1";
		String timezone;
		String popular;
		String format;				//default "xml"
		String key;					//required
		
		public Params(String key) {
			num_of_results = "1";
			this.key = key;
		}
		
		public Params setQuery(String query) {
			this.query = query;
			return this;
		}
		
		Params setNumOfResults(String num_of_results) {
			this.num_of_results = num_of_results;
			return this;
		}
		
		Params setTimezone(String timezone) {
			this.timezone = timezone;
			return this;
		}
		
		Params setPopular(String popular) {
			this.popular = popular;
			return this;
		}
		
		Params setFormat(String format) {
			this.format = format;
			return this;
		}
		
		Params setKey(String key) {
			this.key = key;
			return this;
		}
	}

}

