package com.simple.wildfishingnote.utils;

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
}
