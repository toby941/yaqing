package com.airAd.yaqinghui.net;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.airAd.yaqinghui.data.model.CepActive;
import com.airAd.yaqinghui.data.model.CepItem;

public class MyActiveService {
	public ArrayList<CepActive> getCepActiveList(JSONObject jsonObj) {
		ArrayList<CepActive> retList = new ArrayList<CepActive>();
		try {
			JSONArray arrays = jsonObj.getJSONArray("SelectConfirmCepActive");
			for (int i = 0; i < arrays.size(); i++) {
				CepActive item = new CepActive();
				JSONObject obj = arrays.getJSONObject(i);
				item.setCepid(obj.getString("cepid"));
				item.setCepstate(obj.getString("cepstate"));
				item.setCepcontent(obj.getString("cepcontent"));
				item.setCeptitle(obj.getString("ceptitle"));
				item.setCeptime(obj.getString("ceptime"));
				item.setCepplace(obj.getString("cepplace"));
				item.setThumb("http://upload.njdaily.cn/2012/1031/1351686633791.jpg");
				retList.add(item);
			}// end for i
		} catch (JSONException e) {
			e.printStackTrace();
			retList = null;
			return retList;
		}
		return retList;
	}

}
