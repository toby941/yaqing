package com.airAd.yaqinghui.data.model;

public class ConfirmCepActive {
	public static final String FLAG_PASS="Q1";//通过
	public static final String FLAG_FULL="Q0";//人数已满
	public static final String FLAG_CHECKING="Q9";//批准中
	
	private String confirmmark;
	private String confirmtext;
	
	public String getConfirmmark() {
		return confirmmark;
	}
	public void setConfirmmark(String confirmmark) {
		this.confirmmark = confirmmark;
	}
	public String getConfirmtext() {
		return confirmtext;
	}
	public void setConfirmtext(String confirmtext) {
		this.confirmtext = confirmtext;
	}
}//end class
