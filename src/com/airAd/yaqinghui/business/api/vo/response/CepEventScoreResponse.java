/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.api.vo.response;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.airAd.yaqinghui.business.model.Base;
import com.airAd.yaqinghui.common.Constants;

/**
 * CepEventScoreResponse.java
 * 
 * @author liyuhang
 */
public class CepEventScoreResponse extends Base {
	public static final String SCORE_SUCC = "1";
	public static final String SCORE_ERR = "0";

	public CepEventScoreResponse() {
		flag = Constants.FLAG_ERR;
		msg = Constants.FLAG_ERR_MSG;
	}

	public static CepEventScoreResponse instance(JSONObject obj)
			throws JSONException {
		CepEventScoreResponse res = new CepEventScoreResponse();
		JSONObject result = obj.optJSONObject("CepActiveComment");
		String statusFlag = result.optString("commentmark");
		res.setFlag(SCORE_SUCC.equals(statusFlag)
				? Constants.FLAG_SUCC
				: Constants.FLAG_ERR);
		res.setMsg(result.optString("commenttext"));
		return res;
	}
}
