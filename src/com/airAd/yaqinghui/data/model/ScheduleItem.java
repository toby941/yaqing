package com.airAd.yaqinghui.data.model;

/**
 * 日程项目
 * 
 * @author Administrator
 * 
 */
public class ScheduleItem {
	private int type;//项目类型
	private String name;
	private String title;
	private String time;
	private String location;
	private boolean isNotify;// 是否被提醒
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public boolean isNotify() {
		return isNotify;
	}

	public void setNotify(boolean isNotify) {
		this.isNotify = isNotify;
	}
}// end class
