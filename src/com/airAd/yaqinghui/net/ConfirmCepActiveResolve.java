package com.airAd.yaqinghui.net;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import com.airAd.yaqinghui.data.model.ConfirmCepActive;

public class ConfirmCepActiveResolve {
    public ConfirmCepActive getConfirmCepActive(JSONObject jsonObj) {
    	ConfirmCepActive ret= new ConfirmCepActive();
        try {
            JSONObject obj= jsonObj.getJSONObject("ConfirmCepActive");
            ret.setConfirmmark(obj.getString("confirmmark"));
            ret.setConfirmtext(obj.getString("confirmtext"));
        } catch (JSONException e) {
            e.printStackTrace();
            ret = null;
            return ret;
        }
        return ret;
    }

}
