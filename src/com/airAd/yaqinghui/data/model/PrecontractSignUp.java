package com.airAd.yaqinghui.data.model;

public class PrecontractSignUp {
	public static final String PRECONTRACT_SUCCESS="P1";
	public static final String PRECONTRACT_FAILED="P0";
	private String text;
	private String  mark;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
}//end class
