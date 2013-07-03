package com.airAd.yaqinghui.data.model;

public class ActivityItem {
	public static final int ACTIVITY_STATUS_APPLYING=1;//申请状态
	public static final int ACTIVITY_STATUS_HASATTEND=2;//已经报名
	
	private int status;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	private String title;
	private String time;
	private String location;
	private String content;
	private String picurl;
}//end class
