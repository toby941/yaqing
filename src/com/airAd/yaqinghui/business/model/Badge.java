/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.model;

import java.util.Date;

/**
 * Badge.java
 * 
 * @author liyuhang
 */
public class Badge {
    private Integer cid;
    private String id;
    private Long addTimel;
    private Date addTime;
    private String userId;
    private String cepId;
    private String eventId;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getAddTimel() {
        return addTimel;
    }

    public void setAddTimel(Long addTimel) {
        this.addTimel = addTimel;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCepId() {
        return cepId;
    }

    public void setCepId(String cepId) {
        this.cepId = cepId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

}
