/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.model;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * Cep.java
 * 
 * @author liyuhang
 */
public class Cep {
    private String id;
    private String title;
    private String content;
    private String pic;
    private int joinNum;
    private int sigupNum;
    private String score;
    private String time;
    private String place;
    private List<String> pics;
    private List<CepEvent> cepEvents;

    //
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(int joinNum) {
        this.joinNum = joinNum;
    }

    public int getSigupNum() {
        return sigupNum;
    }

    public void setSigupNum(int sigupNum) {
        this.sigupNum = sigupNum;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<CepEvent> getCepEvents() {
        return cepEvents;
    }

    public void setCepEvents(List<CepEvent> cepEvents) {
        this.cepEvents = cepEvents;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public static Cep instance(JSONObject jsonObj) throws JSONException {
        Cep cep = new Cep();
        JSONObject obj = jsonObj.getJSONObject("TheOneCepInfo");
        cep.setTitle(obj.getString("ceptitle"));
        cep.setContent(obj.getString("cepcontent"));
        cep.setPlace(obj.getString("cepplace"));
        cep.setTime(obj.getString("ceptime"));
        ArrayList<String> pics = new ArrayList<String>();
        String[] picArray = obj.getString("ceppictureone").split(",");
        for (int i = 0; i < picArray.length; i++) {
            pics.add(picArray[i]);
        }
        cep.setPics(pics);
        return cep;
    }

    public static List<Cep> instanceList(JSONObject jsonObj) throws JSONException {
        List<Cep> ceps = new ArrayList<Cep>();
        JSONArray cepArray = jsonObj.getJSONArray("AllCepInfo");
        for (int i = 0; i < cepArray.size(); i++) {
            Cep cep = new Cep();
            JSONObject obj = cepArray.getJSONObject(i);
            cep.setId(obj.getString("cepid"));
            cep.setTitle(obj.getString("ceptitle"));
            cep.setContent(obj.getString("cepcontent"));
            cep.setPic(obj.getString("ceppicture"));
            ceps.add(cep);
        }// end loop
        return ceps;
    }
    //
}
