/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.api.vo.response;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.airAd.yaqinghui.business.model.Base;
import com.airAd.yaqinghui.common.Constants;

/**
 * ChangePasswordResponse.java
 * 
 * @author liyuhang
 */
public class ChangePasswordResponse extends Base {

    private String temp;

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public ChangePasswordResponse() {
        flag = Constants.FLAG_ERR;
        msg = Constants.FLAG_ERR_MSG;
        temp = "";
    }

    public String toString() {
        return getTemp();
    }

    public static ChangePasswordResponse instance(JSONObject obj) throws JSONException {
        ChangePasswordResponse res = new ChangePasswordResponse();
		JSONObject result = obj.optJSONObject("PasswordModify");
        if (result != null) {
            res.setFlag(Constants.FLAG_SUCC);
			res.setMsg(result.optString("info"));
        }
        else {
			result = obj.optJSONObject("ApiErrorInfo");
            if (result != null) {
                res.setFlag(Constants.FLAG_ERR);
                res.setMsg(result.optString("msg"));
            }
        }
        res.setTemp(result.toString());
        return res;
    }

}
