/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.model;

import java.util.List;

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
    private List<CepEvent> cepEvents;

    //
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    //

}
