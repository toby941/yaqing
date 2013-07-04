/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.model;


/**
 * CepEvent.java
 * 
 * @author liyuhang
 */
public class CepEvent {
    private String id;
    private String cepId;
    private String place;
    private String startTime;
    private String endTime;
    private String flag;
	private String name;
	private Integer maxNum;
	private Integer attendNum;
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name= name;
	}
	public Integer getMaxNum()
	{
		return maxNum;
	}
	public void setMaxNum(Integer maxNum)
	{
		this.maxNum= maxNum;
	}
	public Integer getAttendNum()
	{
		return attendNum;
	}
	public void setAttendNum(Integer attendNum)
	{
		this.attendNum= attendNum;
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

    //

}
