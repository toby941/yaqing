package com.airAd.yaqinghui.data.model;

import android.text.format.Time;

/**
 * 日程信息
 * 
 * @author Panyi
 * 
 */
public class Schedule {
	private String time;
	private String picUrl;
	private String title;
	private String location;
	private int icon;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}
}// end class
