package com.airAd.yaqinghui.business.model;

import java.util.ArrayList;

public class NewsDetail {
	private String cepid;
	private String date;
	private String location;
	private String title;
	private String content;
    private ArrayList<String> picList;// 图片列表
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ArrayList<String> getPicList() {
		return picList;
	}

	public String getCepid() {
		return cepid;
	}

	public void setCepid(String cepid) {
		this.cepid = cepid;
	}

	public void setPicList(ArrayList<String> picList) {
		this.picList = picList;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}// end class
