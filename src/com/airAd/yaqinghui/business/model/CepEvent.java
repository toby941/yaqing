/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.airAd.yaqinghui.common.ApiUtil;

/**
 * CepEvent.java
 * 
 * @author liyuhang
 */
public class CepEvent {
	public static final String CEP_EVENT_TYPE_IN = "0";
	public static final String CEP_EVENT_TYPE_OUT = "1";

	private static final String CEP_EVENT_FLAG_SIGNED_UP = "1";
	private static final String CEP_EVENT_FLAG_CHECKED_IN = "2";
	private static final String CEP_EVENT_FLAG_SCORED = "3";
	private static final String CEP_EVENT_FLAG_SIGNUP_CANCELED = "9"; // 确认取消
	private static final String CEP_EVENT_FLAG_DEFAULT = "A";

	private Integer tabId;
	private String id;
	private String cepId;
	private String place;
	private String startTime;
	private String endTime;
	private String flag;
	private String name;
	private Integer maxNum;
	private Integer attendNum;
	private String cepPic;
	private String cepEventType;

	// public boolean canSignUp() {
	// if (CEP_EVENT_TYPE_OUT.equals(cepEventType)) {
	// // 村外线下报名
	// if (CEP_EVENT_FLAG_DEFAULT.equals(flag)
	// || CEP_EVENT_FLAG_SIGNUP_CANCELED.equals(flag)) {
	// return true;
	// } else {
	// return false;
	// }
	// } else {
	// // 村内不用报名
	// return true;
	// }
	// }
	public boolean canCheckIn() {
		// 如果cep活动过期，则不能签到
		Date now = new Date();
		Date today00 = DateUtils.truncate(now, Calendar.DATE);
		//
		Date gameStartAt = DateUtils.truncate(ApiUtil.convertDateStringToDate(startTime), Calendar.DATE);
		if (gameStartAt.compareTo(today00) != 0) {
			return false;
		}
		//
		if (CEP_EVENT_TYPE_OUT.equals(cepEventType)) {
			// 村外报名通过才能签到
			if (CEP_EVENT_FLAG_SIGNED_UP.equals(flag)) {
				return true;
			} else {
				return false;
			}
		} else {
			// 村内直接签到
			if (CEP_EVENT_FLAG_CHECKED_IN.equals(flag)
					|| CEP_EVENT_FLAG_SCORED.equals(flag)) {
				return false;
			} else {
				return true;
			}
		}
	}
	public boolean canScored() {
		if (CEP_EVENT_FLAG_CHECKED_IN.equals(flag)) {
			return true;
		} else {
			return false;
		}
	}

	public Integer getTabId() {
		return tabId;
	}
	public void setTabId(Integer tabId) {
		this.tabId = tabId;
	}
	public String getCepEventType() {
		return cepEventType;
	}
	public void setCepEventType(String cepEventType) {
		this.cepEventType = cepEventType;
	}
	public String getCepPic() {
		return cepPic;
	}
	public void setCepPic(String cepPic) {
		this.cepPic = cepPic;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}
	public Integer getAttendNum() {
		return attendNum;
	}
	public void setAttendNum(Integer attendNum) {
		this.attendNum = attendNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCepId() {
		return cepId;
	}

	public void setCepId(String cepId) {
		this.cepId = cepId;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Long getStartTimel() {
		return ApiUtil.convertDateStringToDate(startTime).getTime();
	}
	
	public Long getEndTimel(){
		return ApiUtil.convertDateStringToDate(endTime).getTime();
	}
	public String getEventTimeRangeDescription() {
		Date s= ApiUtil.convertDateStringToDate(startTime);
		Date e= ApiUtil.convertDateStringToDate(endTime);
		SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return df.format(s) + "-" + df.format(e).split(" ")[1];
	}

	public int getStartDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(getStartTimel()));
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return day;
	}
	//
}
