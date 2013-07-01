package com.airAd.yaqinghui.net;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.airAd.yaqinghui.data.model.Comment;
import com.airAd.yaqinghui.data.model.NewsDetail;

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
            pics.add(obj.getString("ceppictureone"));
            retdata.setPicList(pics);
            ArrayList<Comment> commentList = new ArrayList<Comment>();
            try {
                JSONArray commentArray = obj.getJSONArray("commentdetail");
                for (int i = 0; i < commentArray.size(); i++) {
                    JSONObject commentObj = commentArray.getJSONObject(i);
                    Comment item = new Comment();
                    item.setCommentid(commentObj.getString("commentid"));
                    item.setPicurl(commentObj.getString("picturehttp"));
                    item.setCepid(commentObj.getString("cepid"));
                    item.setName(commentObj.getString("commentname"));
                    item.setTime(commentObj.getString("commenttime"));
                    item.setContent(commentObj.getString("commentconent"));
                    commentList.add(item);
                }// end for i
            } catch (Exception e) {
                // TODO: handle exception
            }
            retdata.setCommentList(commentList);
        } catch (JSONException e) {
            e.printStackTrace();
            retdata = null;
            return retdata;
        }
        return retdata;
    }
}// end class
