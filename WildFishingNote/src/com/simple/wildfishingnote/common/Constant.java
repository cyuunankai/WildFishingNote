package com.simple.wildfishingnote.common;

public class Constant {
    public final static String NEW_LINE = "\n";
    public final static String SPACE = " ";
    public final static String DASH = "-";
    public final static String SLASH = "/";
    public final static String TEMP_C_SYMBOL = "°C";
    public final static String PA_SYMBOL = "Pa";
    
	public final static String PLACE_IMAGE_PATH = "/placeImages/";
	public final static String FISH_RESULT_IMAGE_PATH = "/fishResultImages/";
	
	
	public final static int REQUEST_CODE_PICK_SINGAL_IMAGE = 100;
	public final static int REQUEST_CODE_ADD_PLACE = 101;
	public final static int REQUEST_CODE_EDIT_PLACE = 102;
	public final static int REQUEST_CODE_ADD_POINT = 103;
    public final static int REQUEST_CODE_EDIT_POINT = 104;
    public final static int REQUEST_CODE_SHOW_ALL_ROD_LENGTH = 105;
    public final static int REQUEST_CODE_SHOW_ALL_LURE_METHOD = 106;
    public final static int REQUEST_CODE_SHOW_ALL_BAIT = 107;
    public final static int REQUEST_CODE_PICK_MULTIPLE_IMAGE = 108;
    public final static int REQUEST_CODE_ADD_CAMPAIGN = 109;
    public final static int REQUEST_CODE_EDIT_CAMPAIGN = 110;
    public final static int REQUEST_CODE_SHOW_ALL_AREA = 111;
    
    
    public final static int ADD_PLACE_ID = 10000;
    public final static int ADD_POINT_ID = 10001;
    
    public final static boolean ENABLE_LOG = true;
    
    
    //These keys are only for Demo purpose.
    //You should replace the key with your own key.
    //You can obtain your own key after registering on World Weather Online website.
    public static final String FREE_API_KEY = "2c6cd1603148c8db1d40a83880a94";
    // 2015-03-06
    public static final String PREMIUM_API_KEY = "7e417a05fb4145411c6df7d3a48f1";
    
    public static final String BACKUP_FOLDER_NAME = "wildFishingNote";
    
    public static final String[] MONTH_NAMES = {"01","02","03","04","05","06","07","08","09","10","11","12"};
    public final static String CAMPAIGN_COUNT = "campaign_count";
    public final static String OBTAINED_FISH_BIGGER_THAN = "obtained_fish_bigger_than";
    public final static String ESCAPED_FISH_BIGGER_THAN = "escaped_fish_bigger_than";
    public final static String NOT_OBTAINED_FISH = "not_obtained_fish";
    public final static String FISH_COUNT_MORE_THAN = "fish_count_more_than";
    public final static int[] SEARCH_FISH_WEIGHT = {150, 250, 500, 1000, 1500, 2000, 2500};
    public final static int[] SEARCH_FISH_COUNT = {5, 10, 15, 20, 30, 40, 50};
}
