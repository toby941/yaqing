/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.api.vo.param;
/**
 * CepEventCheckinParam.java
 * 
 * @author liyuhang
 */
public class CepEventCheckinParam {
	private String qrcode;
	private String userId;
	private String lng;
	private String lat;
	private String cepId;
	private String eventId;
	
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
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
}
