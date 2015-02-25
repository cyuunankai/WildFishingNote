package com.simple.wildfishingnote.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.simple.wildfishingnote.common.Constant;

public class WwoApi {
	public static final boolean LOGD = true;
	
	String key;
	String apiEndPoint;
	
	WwoApi(boolean freeAPI) {
		if(freeAPI) {
			key = Constant.FREE_API_KEY;
		} else {
			key = Constant.PREMIUM_API_KEY;
		}
	}
	
	WwoApi setKey(String key) {
		this.key = key;
		return this;
	}
	
	WwoApi setApiEndPoint(String apiEndPoint) {
		this.apiEndPoint = apiEndPoint;
		return this;
	}
	
	public class RootParams {
		public String getQueryString(Class cls) {
			String query = null;
			
			Field[] fields = cls.getDeclaredFields();
			
			try {
			  for (Field field : fields) {
				  Object f = field.get(this);
				  if(f != null) {
					if(query == null)
						query = "?" + URLEncoder.encode(field.getName(), "UTF-8") + "="
							+ URLEncoder.encode((String)f, "UTF-8");
					else
						query += "&" + URLEncoder.encode(field.getName(), "UTF-8") + "="
								+ URLEncoder.encode((String)f, "UTF-8");
				  }
			  }
			} catch (Exception e) {
				
			}
			
			return query;
		}
	}
	
	static InputStream getInputStream(String url) {
		InputStream is = null;
		
		try {
			URLConnection conn = null;
	        URL url1 = new URL(url);
	        conn = url1.openConnection();
	        HttpURLConnection httpConn = (HttpURLConnection) conn;
	        httpConn.setRequestMethod("GET");
	        httpConn.connect();
	        if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
	            is = httpConn.getInputStream();
	        }
//			is = (new URL(url)).openConnection().getInputStream();
		} catch (Exception e) {
			Log.i("xxx", "x" + e);
		}
		
		return is;
	}
	
	static XmlPullParser getXmlPullParser(InputStream is) {
		XmlPullParser xpp = null;
		
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	
			// process data
			// use local parser for preventing synchronization issue
	        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        xpp = factory.newPullParser();
	
	        xpp.setInput( rd );
		} catch (Exception e) {
			
		}

		return xpp;
    }
	
	static String getDecode(String value) {
		//value = new String(Base64.decode(value));
		if (value.startsWith("<![CDATA["))
			value = value.substring(9, value.length() - 3);
		return value;
	}

	static String getTextForTag(XmlPullParser xpp, String tag) 
	throws XmlPullParserException, IOException {
		while(myXppNextTag(xpp) != xpp.START_TAG || !xpp.getName().equals(tag))
			if(LOGD) Log.d("CallWWOApi", "Tag="+xpp.getName());
		xpp.require(xpp.START_TAG, "", tag);
		String text = xpp.nextText();
		if(LOGD) Log.d("CallWWOApi", "getTextForTag " + text);
		xpp.require(xpp.END_TAG, "", tag);
		
		return text;
	}
	
	static void reachNextStartTagFor(XmlPullParser xpp, String tag) 
	throws XmlPullParserException, IOException {
		while(myXppNextTag(xpp) != xpp.START_TAG || !xpp.getName().equals(tag))
			if(LOGD) Log.d("CallWWOApi", "Tag="+xpp.getName());
		xpp.require(xpp.START_TAG, "", tag);
	}
	
	//xpp.nextTag does not pass TEXT event
	//myXppNextTag passes TEXT event and whitespace, 
	static int myXppNextTag(XmlPullParser xpp)
	throws XmlPullParserException, IOException {
		   int eventType = xpp.next();
		   while(eventType != xpp.START_TAG &&  eventType != xpp.END_TAG) {   // skip whitespace
		      eventType = xpp.next();
		      if (eventType == xpp.END_DOCUMENT) break;
		   }
		   if (eventType != xpp.START_TAG &&  eventType != xpp.END_TAG) {
		      throw new XmlPullParserException("expected start or end tag", xpp, null);
		   }
		   return eventType;
	}
}