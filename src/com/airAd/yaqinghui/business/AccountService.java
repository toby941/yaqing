/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;

import net.sf.json.JSONObject;
import android.util.Log;

import com.airAd.yaqinghui.business.api.BasicAPI;
import com.airAd.yaqinghui.business.api.vo.response.ChangePasswordResponse;
import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.core.HessianClient;

/**
 * AccountService.java
 * 
 * @author liyuhang
 */
public class AccountService extends BaseService {
	public User doLogin(String username, String password) {
		BasicAPI basicAPI = HessianClient.create();
		try {
			// ("200970", "032747","CHI");
			JSONObject jsonObj = basicAPI.UserLogin(username, password,
					User.getLan());
			Log.d("htestDoLogin", jsonObj.toString());
			return User.instance(jsonObj);
		} catch (Exception e) {
			return null;
		}
	}


	//

	public ChangePasswordResponse doChangePassword(String userId,
			String oldPassword, String newPassword, String confirmPassword) {
		BasicAPI api = HessianClient.create();
		try {
			JSONObject jsonObj = api.ChangePassword(userId, oldPassword,
					newPassword, confirmPassword, User.getLan());
			Log.d("htestChangePassword", jsonObj.toString());
			return ChangePasswordResponse.instance(jsonObj);
		} catch (Exception e) {
			return new ChangePasswordResponse();
		}
	}
}
