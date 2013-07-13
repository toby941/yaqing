/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.model;

import java.util.Date;

import android.util.Log;

import com.airAd.yaqinghui.common.ApiUtil;
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

	private Long cid;
	private String id;
	private Integer readFlag;
	private String content;
	private Integer messageType;
	private Long addTimel;
	private Date addTime;
	private String addTimeStr;
	private String title;
	private String cepId;
	private String eventId;
	private String userId;

	private String status;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAddTimeStr() {
		return addTimeStr;
	}
	public void setAddTimeStr(String addTimeStr) {
		this.addTimeStr = addTimeStr;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getAddTimel() {
		Date date = ApiUtil.convertDateStringToDate(addTimeStr);
		return date.getTime();
	}

	public void setAddTimel(Long addTimel) {
		this.addTimel = addTimel;
	}

	public Long getCid() {
		return cid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCid(Long cid) {
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
		Date date = ApiUtil.convertDateStringToDate(addTimeStr);
		return date;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public static NoficationMessage instance(String src) {
		NoficationMessage item = new NoficationMessage();
		try {
			String[] msgs = src.split("==");
			if (msgs.length != 8)
				return null;
			item.setMessageType(Integer.parseInt(msgs[0]));
			item.setTitle(msgs[1]);
			item.setCepId(msgs[2]);
			item.setEventId(msgs[3]);
			item.setUserId(msgs[4]);
			item.setReadFlag(NoficationMessage.UNREAD);
			item.setStatus(msgs[5]);
			item.setContent(msgs[6]);
			item.setAddTimeStr(msgs[7]);
		} catch (Exception e) {
			Log.e("htest", e.getMessage());
			return null;
		}
		return item;
	}

	@Override
	public String toString() {
		return "title:" + title + " \r\ncontent:" + content;
	}
}
