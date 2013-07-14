package com.airAd.yaqinghui.business.model;

import java.util.Date;

public class ScheduleItem {
    public static final int TYPE_GAME = 1;
    public static final int TYPE_CEP_EVENT = 2;
	public static final int TYPE_TRAINING= 3;

	private Long cid;
    private String title;
    private String timeStr;
    private String place;
    private String pic;
    private String userId;
	// itemtype 为1 refId 为比赛id itemtype为2 refId 为cep id
    private String refId;
    private String cepId;
	// itemtype 1 比赛 2 cep活动
    private Integer itemType;
    private Integer year;
    private Integer month;
    private Integer day;
    private Long addTimel;
    private Date addTime;
    private Long startTimel;
    private Date startTime;
	private String iconType;
    //

	public Long getCid() {
        return cid;
    }

	public String getIconType() {
		return iconType;
	}

	public void setIconType(String iconType) {
		this.iconType = iconType;
	}

	public Long getStartTimel() {
        return startTimel;
    }

    public void setStartTimel(Long startTimel) {
        this.startTimel = startTimel;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

	public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getCepId() {
        return cepId;
    }

    public void setCepId(String cepId) {
        this.cepId = cepId;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
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

    //

}// end class
