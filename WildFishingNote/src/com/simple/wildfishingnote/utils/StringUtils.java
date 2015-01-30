package com.simple.wildfishingnote.utils;

public class StringUtils {
	
	public static String leftPadTwo(int value){
		String pattern = "00";
		java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
		return df.format(value);
	}
}
