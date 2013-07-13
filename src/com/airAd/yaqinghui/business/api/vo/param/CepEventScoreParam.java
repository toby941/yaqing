/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.api.vo.param;
/**
 * CepEventScoreParam.java
 *
 * @author liyuhang
 */
public class CepEventScoreParam {
	private String userId;
	private String score;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}

}
