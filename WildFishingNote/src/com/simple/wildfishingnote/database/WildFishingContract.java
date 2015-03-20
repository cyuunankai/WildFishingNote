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
    
    // 区域
    public static abstract class Areas implements BaseColumns {
        public static final String TABLE_NAME = "areas";
        public static final String COLUMN_NAME_TITLE = "title";
    }

    // 钓位
    public static abstract class Places implements BaseColumns {
        public static final String TABLE_NAME = "places";
        public static final String COLUMN_NAME_AREA_ID = "area_id";
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
        public static final String TABLE_NAME = "relay_campaign_image_results";
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

    // 天气
    public static abstract class Weathers implements BaseColumns {
        public static final String TABLE_NAME = "weathers";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_REGION = "region";
        public static final String COLUMN_NAME_MIN_TEMP_C = "min_temp_c";
        public static final String COLUMN_NAME_MAX_TEMP_C = "max_temp_c";
        public static final String COLUMN_NAME_SUNRISE = "sunrise";
        public static final String COLUMN_NAME_SUNSET = "sunset";
    }

    public static abstract class WeathersHourly implements BaseColumns {
        public static final String TABLE_NAME = "weathers_hourly";
        public static final String COLUMN_NAME_WEATHER_ID = "weather_id";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_TEMP_C = "temp_c";
        public static final String COLUMN_NAME_WIND_SPEED_KMPH = "wind_speed_kmph";
        public static final String COLUMN_NAME_WIND_DIR_DEGREE = "wind_dir_degree";
        public static final String COLUMN_NAME_PRESSURE = "pressure";
        public static final String COLUMN_NAME_CLOUD_COVER = "cloud_cover";
        public static final String COLUMN_NAME_WEATHER_CODE = "weather_code";
    }
    
    // 备份
    public static abstract class Backups implements BaseColumns {
        public static final String TABLE_NAME = "backups";
        public static final String COLUMN_NAME_PATH = "path";
        public static final String COLUMN_NAME_TIME = "time";
    }

}
