package com.airAd.yaqinghui.business;

import java.util.ArrayList;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.airAd.yaqinghui.business.model.NewsDetail;

/**
 * CEP活动详情 数据解析
 * 
 * @author panyi
 */
public class NewsDetailService {
    /**
     * @param jsonObj
     * @return
     */
    public NewsDetail getItem(JSONObject jsonObj) {
        NewsDetail retdata = new NewsDetail();
        try {
            JSONObject obj = jsonObj.getJSONObject("TheOneCepInfo");
            retdata.setTitle(obj.getString("ceptitle"));
            retdata.setContent(obj.getString("cepcontent"));
            retdata.setLocation(obj.getString("cepplace"));
            retdata.setDate(obj.getString("ceptime"));
            ArrayList<String> pics = new ArrayList<String>();
            String[] picArray = obj.getString("ceppictureone").split(",");
            for(int i=0;i<picArray.length;i++){
            	 pics.add(picArray[i]);
            }
            retdata.setPicList(pics);
        } catch (JSONException e) {
            e.printStackTrace();
            retdata = null;
            return retdata;
        }
        return retdata;
    }
}// end class
