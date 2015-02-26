package com.simple.wildfishingnote.utils;

public class StringUtils {
	
	public static String leftPadTwo(int value){
		String pattern = "00";
		java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
		return df.format(value);
	}
	
	public static String leftPadFour(int value){
        String pattern = "0000";
        java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
        return df.format(value);
    }
	
    public static String convertToTimeFormat(String str) {
        str = leftPadFour(Integer.parseInt(str));
        String part1 = str.substring(0, 2);
        
        return part1 + "点";
    }
	
}
