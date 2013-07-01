package com.airAd.yaqinghui.data.model;

public class SignInCepActive {
	public static final String SIGNIN_SUCCESS="S1";
	public static final String SIGNIN_FAILED="S0";
	
	private String mark;
	private String text;
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}//end class
