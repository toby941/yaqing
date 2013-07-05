/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.api.vo.response;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.airAd.yaqinghui.common.Constants;

/**
 * CepEventCheckinResponse.java
 *
 * @author liyuhang
 */
public class CepEventCheckinResponse {
	private static String CHECKIN_SUCC = "S1";
	private static String CHECKIN_ERR = "S0";

	private String flag;
	private String msg;

	public CepEventCheckinResponse() {
		flag = Constants.FLAG_ERR;
		msg = Constants.FLAG_ERR_MSG;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static CepEventCheckinResponse instance(JSONObject obj)
			throws JSONException {
		CepEventCheckinResponse res = new CepEventCheckinResponse();
		JSONObject result = obj.getJSONObject("SignInCepActive");
		String statusFlag = result.optString("signinmark");
		res.setFlag(CHECKIN_SUCC.equals(statusFlag)
				? Constants.FLAG_SUCC
				: Constants.FLAG_ERR);
		res.setMsg(result.optString("signintext"));
		return res;
	}
}
