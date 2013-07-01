package com.airAd.yaqinghui.data.model;

public class PrecontractCancelCepActive {
	public static final String CANCEL_SUCCESS="C1";
	public static final String CANCEL_FAILED="C0";
	
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
