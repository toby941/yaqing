/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.model;

import java.util.Date;

/**
 * Message.java
 * 
 * @author liyuhang
 */
public class NoficationMessage {
    public static final Integer READ = 1;
    public static final Integer UNREAD = 0;

    public static final Integer TYPE_NOTIFICATION = 1;
    public static final Integer TYPE_CEPEVENT_HIS = 2;
    public static final Integer TYPE_BADGE_HIS = 3;

    private Integer cid;
    private String id;
    private Integer readFlag;
    private String content;
    private Integer messageType;
    private Date addTime;

    public Integer getCid() {
        return cid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(Integer readFlag) {
        this.readFlag = readFlag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

}
