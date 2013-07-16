/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.api.vo.response;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.airAd.yaqinghui.business.model.Base;
import com.airAd.yaqinghui.common.Constants;

/**
 * CepEventCheckinResponse.java
 *
 * @author liyuhang
 */
public class CepEventCheckinResponse extends Base {
	private static String CHECKIN_SUCC = "S1";
	private static String CHECKIN_ERR = "S0";

	private String eventId;


	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public CepEventCheckinResponse() {
		flag = Constants.FLAG_ERR;
		msg = Constants.FLAG_ERR_MSG;
	}


	public static CepEventCheckinResponse instance(JSONObject obj)
			throws JSONException {
		CepEventCheckinResponse res = new CepEventCheckinResponse();
		JSONObject result = obj.getJSONObject("SignInCepActive");
		String statusFlag = result.optString("signinmark");
		res.setFlag(statusFlag.contains(CHECKIN_SUCC)
				? Constants.FLAG_SUCC
				: Constants.FLAG_ERR);
		res.setMsg(result.optString("signintext"));
		if (Constants.FLAG_SUCC.equals(res.getFlag())) {
			res.setEventId(statusFlag.split(",")[1]);
		}
		return res;
	}
}
