package com.airAd.yaqinghui.data.model;

import android.text.format.Time;

/**
 * 评论
 * @author Panyi 
 *
 */
public class Comment {
	private String cepid;
	private String commentid;
	private String picurl;
	private String name;
	private String time;
	private String content;
	
	public String getCepid() {
		return cepid;
	}
	public void setCepid(String cepid) {
		this.cepid = cepid;
	}
	public String getCommentid() {
		return commentid;
	}
	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}//end class
