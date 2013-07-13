/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.api.vo.response;

import net.sf.json.JSONObject;

import com.airAd.yaqinghui.common.Constants;

/**
 * CepEventReservationResponse.java
 * 
 * @author liyuhang
 */
public class CepEventReservationResponse {
	// 预约返回
	private static String PRECONTRACT_SUCC = "P1";
	private static String PRECONTRACT_ERR = "P0";
	// 签到返回
	private static String SIGNUP_SUCC = "S1";
	private static String SIGNUP_ERR = "S0";
	//
	private static String CANCEL_SUCC = "C1";
	private static String CANCEL_ERR = "C0";
	//
	private String flag;
	private String msg;

	public CepEventReservationResponse() {
		flag = Constants.FLAG_ERR;
		msg = Constants.FLAG_ERR_MSG;
	}

	/**
	 * { "PrecontractSignUp":
	 * {"sucessmark":"P1","sucesstext":"您已预约成功，请到XXX服务台确认，服务台地址：XXX，联系电话：XXX" }}
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static CepEventReservationResponse instanceSignupObj(JSONObject obj)
			throws Exception {
		CepEventReservationResponse res = new CepEventReservationResponse();
		JSONObject result = obj.getJSONObject("PrecontractSignUp");
		String statusFlag = result.optString("sucessmark");
		res.setFlag(PRECONTRACT_SUCC.equals(statusFlag)
				? Constants.FLAG_SUCC
				: Constants.FLAG_ERR);
		res.setMsg(result.optString("sucesstext"));
		return res;
	}

	/**
	 * { "PrecontractCancel":{ "sucessmark":"C1","sucesstext":"该活动报名取消成功" }}
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static CepEventReservationResponse instanceCancelObj(JSONObject obj)
			throws Exception {
		CepEventReservationResponse res = new CepEventReservationResponse();
		JSONObject result = obj.getJSONObject("PrecontractCancel");
		String statusFlag = result.optString("sucessmark");
		res.setFlag(CANCEL_SUCC.equals(statusFlag)
				? Constants.FLAG_SUCC
				: Constants.FLAG_ERR);
		res.setMsg(result.optString("sucesstext"));
		return res;
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
}
