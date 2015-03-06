package com.simple.wildfishingnote.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.simple.wildfishingnote.bean.CampaignSummary;
import com.simple.wildfishingnote.bean.RelayCamapignStatisticsResult;
import com.simple.wildfishingnote.common.Constant;

public class BusinessUtil {

    public static String getCampaignSummaryTitle(HashMap<String, List<RelayCamapignStatisticsResult>> statisticsHash, CampaignSummary cc) {
        String title;
        int inMaxWeight = 0;
        int outMaxWeight = 0;
        for(RelayCamapignStatisticsResult rcsr : statisticsHash.get(cc.getId())){
            if("in".equals(rcsr.getHookFlag())){
                inMaxWeight = (Integer.parseInt(rcsr.getWeight()) > inMaxWeight) ? Integer.parseInt(rcsr.getWeight()) : inMaxWeight;
            }
            if("out".equals(rcsr.getHookFlag())){
                outMaxWeight = (Integer.parseInt(rcsr.getWeight()) > outMaxWeight) ? Integer.parseInt(rcsr.getWeight()) : outMaxWeight;
            }
        }
        
        List<String> titleList = new ArrayList<String>();
        if(inMaxWeight > 0){
            titleList.add("入护:" + BusinessUtil.getFishUnit(inMaxWeight));
        }
        if(outMaxWeight > 0){
            titleList.add("跑鱼:" + BusinessUtil.getFishUnit(outMaxWeight));
        }
        title = StringUtils.join(titleList, Constant.SPACE);
        return title;
    }
    
    public static String getFishWeight(HashMap<String, List<RelayCamapignStatisticsResult>> statisticsHash, CampaignSummary cc) {
        int weight = 0;
        for(RelayCamapignStatisticsResult rcsr : statisticsHash.get(cc.getDate())){
            weight = weight + (Integer.parseInt(rcsr.getWeight()) * Integer.parseInt(rcsr.getCount()));
        }
        DecimalFormat df = new DecimalFormat("##.#");
        
        return df.format(weight/500.0);
    }
    
    public static String getFishUnit(int grams) {
        String ret = "";

        int jin = grams / 500;
        int remainGrams = grams % 500;
        int liang = remainGrams / 50;

        if (jin == 0) {
            ret = liang + "两";
        } else {
            if (liang == 0) {
                ret = jin + "斤";
            } else {
                ret = jin + "斤" + liang + "两";
            }

        }

        return ret;
    }
    
    /**
     * 风向
     */
    public static String getWindDirDegreeName(String code) {
        String ret = "";
        float floatCode = Float.parseFloat(code);
        if (floatCode >= 337.5 || floatCode < 22.5) {
            ret = "北风";
        } else if (floatCode >= 22.5 && floatCode < 67.5) {
            ret = "东北风";
        } else if (floatCode >= 67.5 && floatCode < 112.5) {
            ret = "东风";
        } else if (floatCode >= 112.5 && floatCode < 157.5) {
            ret = "东南风";
        } else if (floatCode >= 157.5 && floatCode < 202.5) {
            ret = "南风";
        } else if (floatCode >= 202.5 && floatCode < 247.5) {
            ret = "西南风";
        } else if (floatCode >= 247.5 && floatCode < 292.5) {
            ret = "西风";
        } else if (floatCode >= 292.5 && floatCode < 337.5) {
            ret = "西北风";
        }

        return ret;
    }
    
    /**
     * 风速
     */
    public static String getWindspeedName(String code) {
        String ret = "";
        int intCode = Integer.parseInt(code);
        if (intCode < 1) {
            ret = "无风";
        } else if (intCode >= 1 && intCode <= 5) {
            ret = "1级";
        } else if (intCode >= 6 && intCode <= 11) {
            ret = "2级";
        } else if (intCode >= 12 && intCode <= 19) {
            ret = "3级";
        } else if (intCode >= 20 && intCode <= 28) {
            ret = "4级";
        } else if (intCode >= 29 && intCode <= 38) {
            ret = "5级";
        } else if (intCode >= 39 && intCode <= 49) {
            ret = "6级";
        } else if (intCode >= 50 && intCode <= 61) {
            ret = "7级";
        }else if (intCode >= 62 && intCode <= 74) {
            ret = "8级";
        } else if (intCode >= 75 && intCode <= 88) {
            ret = "9级";
        } else if (intCode >= 89 && intCode <= 102) {
            ret = "10级";
        } else if (intCode >= 103 && intCode <= 117) {
            ret = "11级";
        } else if (intCode > 117) {
            ret = "12级";
        }

        return ret;
    }
    
    /**
     * 天气
     */
    public static String getWeatherName(String code) {
        String ret = "";
        int intCode = Integer.parseInt(code);
        if (intCode == 395) {
            // Moderate or heavy snow in area with thunder
            ret = "中雪或大雪(雷)";
        } else if (intCode == 392) {
            // Patchy light snow in area with thunder
            ret = "小雪(雷)";
        } else if (intCode == 389) {
            // Moderate or heavy rain in area with thunder
            ret = "中或大雨(雷)";
        } else if (intCode == 386) {
            // Patchy light rain in area with thunder
            ret = "小雨(雷)";
        } else if (intCode == 377) {
            // Moderate or heavy showers of ice pellets
            ret = "阵冰粒(大或中)";
        } else if (intCode == 374) {
            // Light showers of ice pellets
            ret = "阵冰粒(小)";
        } else if (intCode == 371) {
            // Moderate or heavy snow showers
            ret = "阵雪(中或大)";
        } else if (intCode == 368) {
            // Light snow showers
            ret = "阵雪(小)";
        } else if (intCode == 365) {
            // Moderate or heavy sleet showers
            ret = "雨夹雪(中或大)";
        } else if (intCode == 362) {
            // Light sleet showers
            ret = "雨夹雪(小)";
        } else if (intCode == 359) {
            // Torrential rain shower
            ret = "阵雨(滂沱)";
        } else if (intCode == 356) {
            // Moderate or heavy rain shower
            ret = "阵雨(中或大)";
        } else if (intCode == 353) {
            // Light rain shower
            ret = "阵雨(小)";
        } else if (intCode == 350) {
            // Ice pellets
            ret = "冰粒";
        } else if (intCode == 338) {
            // Heavy snow
            ret = "大雪";
        } else if (intCode == 335) {
            // Patchy heavy snow
            ret = "大雪(片状)";
        } else if (intCode == 332) {
            // Moderate snow
            ret = "中雪";
        } else if (intCode == 329) {
            // Patchy moderate snow
            ret = "中雪(片状)";
        } else if (intCode == 326) {
            // Light snow
            ret = "小雪";
        } else if (intCode == 323) {
            // Patchy light snow
            ret = "小雪(片状)";
        } else if (intCode == 320) {
            // Moderate or heavy sleet
            ret = "中或大雨雪";
        } else if (intCode == 317) {
            // Light sleet
            ret = "小雨雪";
        } else if (intCode == 314) {
            // Moderate or Heavy freezing rain
            ret = "中或大雨(冻)";
        } else if (intCode == 311) {
            // Light freezing rain
            ret = "小雨(冻)";
        } else if (intCode == 308) {
            // Heavy rain
            ret = "大雨";
        } else if (intCode == 305) {
            // Heavy rain at times
            ret = "大雨(有时)";
        } else if (intCode == 302) {
            // Moderate rain
            ret = "中雨";
        } else if (intCode == 299) {
            // Moderate rain at times
            ret = "中雨(有时)";
        } else if (intCode == 296) {
            // Light rain
            ret = "小雨";
        } else if (intCode == 293) {
            // Patchy light rain
            ret = "小雨(片状)";
        } else if (intCode == 284) {
            // Heavy freezing drizzle
            ret = "毛毛雨(重冻结)";
        } else if (intCode == 281) {
            // Freezing drizzle
            ret = "毛毛雨(冻)";
        } else if (intCode == 266) {
            // Light drizzle
            ret = "毛毛雨";
        } else if (intCode == 263) {
            // Patchy light drizzle
            ret = "毛毛雨(片状)";
        } else if (intCode == 260) {
            // Freezing fog
            ret = "冻雾";
        } else if (intCode == 248) {
            // Fog
            ret = "大雾";
        } else if (intCode == 230) {
            // Blizzard
            ret = "暴风雪";
        } else if (intCode == 227) {
            // Blowing snow
            ret = "风吹雪";
        } else if (intCode == 200) {
            // Thundery outbreaks in nearby
            ret = "大雷电(附近)";
        } else if (intCode == 185) {
            // Patchy freezing drizzle nearby
            ret = "小冻雨(附近)";
        } else if (intCode == 182) {
            // Patchy sleet nearby
            ret = "小雨雪(附近)";
        } else if (intCode == 179) {
            // Patchy snow nearby
            ret = "小雪(附近)";
        } else if (intCode == 176) {
            // Patchy rain nearby
            ret = "小雨(附近)";
        } else if (intCode == 143) {
            // Mist
            ret = "雾";
        } else if (intCode == 122) {
            // Overcast
            ret = "阴";
        } else if (intCode == 119) {
            // Cloudy
            ret = "多云(多)";
        } else if (intCode == 116) {
            // Partly Cloudy
            ret = "多云(少)";
        } else if (intCode == 113) {
            // Clear/Sunny
            ret = "晴";
        }

        return ret;
    }
}
