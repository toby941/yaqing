package com.airAd.yaqinghui.business.model;

/**
 * CepActive
 * @author Panyi
 *
 */
public class CepActive {
	public static final String STATE_APPLING="S1";
	public static final String STATE_ATTEND="P1";
	
	private String cepid;
	private String cepstate;
	private String ceptitle;
	private String cepcontent;
	private String  ceptime;
	private String cepplace;
	private String signuptime;
	private String thumb;
	
	public String getCepid() {
		return cepid;
	}
	
	public void setCepid(String cepid) {
		this.cepid = cepid;
	}
	
	public String getCepstate() {
		return cepstate;
	}
	
	public void setCepstate(String cepstate) {
		this.cepstate = cepstate;
	}
	
	public String getCeptitle() {
		return ceptitle;
	}
	public void setCeptitle(String ceptitle) {
		this.ceptitle = ceptitle;
	}
	public String getCepcontent() {
		return cepcontent;
	}
	public void setCepcontent(String cepcontent) {
		this.cepcontent = cepcontent;
	}
	public String getCeptime() {
		return ceptime;
	}
	public void setCeptime(String ceptime) {
		this.ceptime = ceptime;
	}
	public String getCepplace() {
		return cepplace;
	}
	public void setCepplace(String cepplace) {
		this.cepplace = cepplace;
	}
	public String getSignuptime() {
		return signuptime;
	}
	public void setSignuptime(String signuptime) {
		this.signuptime = signuptime;
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
}//end class
