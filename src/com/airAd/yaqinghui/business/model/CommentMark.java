package com.airAd.yaqinghui.business.model;

public class CommentMark {
	public static final int SUCCESS=1;
	public static final int FAILED=0;
	
	private Integer mark;
	private String text;
	
	public Integer getMark() {
		return mark;
	}
	public void setMark(Integer mark) {
		this.mark = mark;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}//end class
