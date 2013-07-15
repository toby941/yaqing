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
	public static final int BADGE_1 = 1;
	public static final int BADGE_2 = 2;
	public static final int BADGE_3 = 3;

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

	public static int getBadgeId(String cepId) {
    	if("1".equals(cepId)){
    		return BADGE_1;
    	}else if("2".equals(cepId) || "3".equals(cepId)){
    		return BADGE_2;
		} else if ("6".equals(cepId) || "5".equals(cepId)) {
    		return BADGE_3;
    	}
		return BADGE_1;
    }
}
