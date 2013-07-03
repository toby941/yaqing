package com.airAd.yaqinghui.business.model;

/**
 * �ճ���Ŀ
 * 
 * @author Administrator
 * 
 */
public class ScheduleItem {
	private int type;//��Ŀ����
	private String name;
	private String title;
	private String time;
	private String location;
	private boolean isNotify;// �Ƿ�����
	
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
