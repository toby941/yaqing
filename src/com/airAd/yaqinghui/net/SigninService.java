package com.airAd.yaqinghui.net;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import com.airAd.yaqinghui.data.model.SignInCepActive;

/**
 *扫描签到解析服务类
 * @author Panyi 
 *
 */
public class SigninService {
	public SignInCepActive getSignup(JSONObject jsonObj){
		SignInCepActive item=new SignInCepActive();
		try {
			JSONObject obj = jsonObj.getJSONObject("SignInCepActive");
			item.setMark(obj.getString("signinmark"));
			item.setText(obj.getString("signintext"));
		} catch (JSONException e) {
			e.printStackTrace();
			item = null;
			return item;
		}catch (NullPointerException e) {
			e.printStackTrace();
			item = null;
			return item;
		}
		return item;
	}
}//end class
