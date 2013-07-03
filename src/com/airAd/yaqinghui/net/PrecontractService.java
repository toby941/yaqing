package com.airAd.yaqinghui.net;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import com.airAd.yaqinghui.data.model.CommentMark;
import com.airAd.yaqinghui.data.model.PrecontractCancelCepActive;
import com.airAd.yaqinghui.data.model.PrecontractSignUp;

/**
 * 预约相关服务类
 * @author Panyi 
 *
 */
public class PrecontractService {
	public PrecontractSignUp getSignup(JSONObject jsonObj){
		PrecontractSignUp item=new PrecontractSignUp();
		try {
			JSONObject obj = jsonObj.getJSONObject("PrecontractSignUp");
			item.setMark(obj.getString("sucessmark"));
			item.setText(obj.getString("sucesstext"));
		} catch (JSONException e) {
			e.printStackTrace();
			item = null;
			return item;
		}
		return item;
	}
	public PrecontractCancelCepActive getCancel(JSONObject jsonObj){
		PrecontractCancelCepActive item=new PrecontractCancelCepActive();
		try {
			JSONObject obj = jsonObj.getJSONObject("PrecontractCancel");
			item.setMark(obj.getString("sucessmark"));
			item.setText(obj.getString("sucesstext"));
		} catch (JSONException e) {
			e.printStackTrace();
			item = null;
			return item;
		}
		return item;
	}
}//end class
