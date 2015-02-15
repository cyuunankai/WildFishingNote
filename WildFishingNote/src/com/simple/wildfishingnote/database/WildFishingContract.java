package com.simple.wildfishingnote.database;

import android.provider.BaseColumns;

public class WildFishingContract {

	// 钓鱼活动
    public static abstract class Campaigns implements BaseColumns {
        
        public static final String TABLE_NAME = "campaigns";
        public static final String COL_NAME_START_TIME = "start_time";
        public static final String COL_NAME_END_TIME = "end_time";
        public static final String COL_NAME_SUMMARY = "summary";
        public static final String COL_NAME_PLACE_ID = "place_id";
    }
    
    // 钓位
 	public static abstract class Places implements BaseColumns {
         public static final String TABLE_NAME = "places";
         public static final String COLUMN_NAME_TITLE = "title";
         public static final String COLUMN_NAME_DETAIL = "detail";
         public static final String COLUMN_NAME_FILE_NAME = "file_path";
     }
 	
 	// 钓点
 	public static abstract class Points implements BaseColumns {
         public static final String TABLE_NAME = "points";
         // 钓位ID
         public static final String COLUMN_NAME_PLACE_ID = "place_id";
         // 竿长ID
         public static final String COLUMN_NAME_ROD_LENGTH_ID = "rod_length_id";
         // 水深
         public static final String COLUMN_NAME_DEPTH = "depth";
         // 打窝ID
         public static final String COLUMN_NAME_LURE_METHOD_ID = "lure_method_id";
         // 饵料ID
         public static final String COLUMN_NAME_BAIT_ID = "bait_id";
     }
 	
 	// 活动_钓点关系表
    public static abstract class RelayCampaignPoints implements BaseColumns {
         public static final String TABLE_NAME = "relay_campaign_points";
         public static final String COLUMN_NAME_CAMPAIGN_ID = "campaign_id";
         public static final String COLUMN_NAME_POINT_ID = "point_id";
     }
 	
 	// 竿长
 	public static abstract class RodLengths implements BaseColumns {
         public static final String TABLE_NAME = "rod_lengths";
         public static final String COLUMN_NAME_NAME = "name";
     }
 	
 	// 打窝
 	public static abstract class LureMethods implements BaseColumns {
         public static final String TABLE_NAME = "lure_methods";
         public static final String COLUMN_NAME_NAME = "name";
         public static final String COLUMN_NAME_DETAIL = "detail";
     }
 	
 	// 饵料
 	public static abstract class Baits implements BaseColumns {
         public static final String TABLE_NAME = "baits";
         public static final String COLUMN_NAME_NAME = "name";
         public static final String COLUMN_NAME_DETAIL = "detail";
     }
 	
 	// 活动渔获图片关系表
  	public static abstract class RelayCampaignImageResults implements BaseColumns {
          public static final String TABLE_NAME = "fish_results";
          public static final String COLUMN_NAME_CAMPAIGN_ID = "campaign_id";
          public static final String COLUMN_NAME_FILE_PATH = "file_path";
    }
	
  	// 活动渔获统计关系表
	public static abstract class RelayCamapignStatisticsResults implements BaseColumns {
		public static final String TABLE_NAME = "relay_campaign_statistics_results";
		public static final String COLUMN_NAME_CAMPAIGN_ID = "campaign_id";
		public static final String COLUMN_NAME_POINT_ID = "point_id";
        public static final String COLUMN_NAME_FISH_TYPE_ID = "fish_type_id";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_COUNT = "count";
        public static final String COLUMN_NAME_HOOK_FLAG = "hook_flag";
	}
	
	// 鱼种
	public static abstract class FishType implements BaseColumns {
		public static final String TABLE_NAME = "fish_types";
		public static final String COLUMN_NAME_NAME = "name";
	}
	
	
}
