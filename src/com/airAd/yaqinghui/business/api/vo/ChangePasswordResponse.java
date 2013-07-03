/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.api.vo;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.airAd.yaqinghui.common.Constants;

/**
 * ChangePasswordResponse.java
 * 
 * @author liyuhang
 */
public class ChangePasswordResponse {

    private String flag;
    private String msg;
    private String temp;

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

    public ChangePasswordResponse() {
        flag = Constants.FLAG_ERR;
        msg = Constants.FLAG_ERR_MSG;
    }

    public static ChangePasswordResponse instance(JSONObject obj) throws JSONException {
        ChangePasswordResponse res = new ChangePasswordResponse();
        JSONObject result = obj.optJSONObject(Constants.FLAG_SUCC);
        if (result != null) {
            res.setFlag(Constants.FLAG_SUCC);
            res.setMsg(result.optString("msg"));
        }
        else {
            result = obj.optJSONObject(Constants.FLAG_ERR);
            if (result != null) {
                res.setFlag(Constants.FLAG_ERR);
                res.setMsg(result.optString("msg"));
            }
        }
        return res;
    }

}
