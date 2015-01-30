package com.simple.wildfishingnote.database;

import android.provider.BaseColumns;

public class WildFishingContract {

    public static abstract class Campaigns implements BaseColumns {
        
        public static final String TABLE_NAME = "campaigns";
        public static final String COL_NAME_START_TIME = "start_time";
        public static final String COL_NAME_END_TIME = "end_time";
        public static final String COL_NAME_SUMMARY = "summary";
    }
}
