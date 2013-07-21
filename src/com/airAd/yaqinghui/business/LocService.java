/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;

import java.text.SimpleDateFormat;
import java.util.List;

import net.sf.json.JSONObject;
import android.util.Log;

import com.airAd.yaqinghui.business.api.BasicAPI;
import com.airAd.yaqinghui.business.model.LocMarker;
import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.core.HessianClient;

/**
 * GameService.java
 * 
 * @author pengf
 */
public class LocService extends BaseService {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public List<LocMarker> getLocations() {
		BasicAPI api = HessianClient.create();
		try {
			JSONObject jsonObj = api.getLocation(User.getLan());
			Log.i("LocService", jsonObj.toString());
			return LocMarker.instanceList(jsonObj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<LocMarker> getLocationDetail(String id) {
		BasicAPI api = HessianClient.create();
		try {
			JSONObject jsonObj = api.getLocationDetail(id, User.getLan());
			Log.i("getLocationDetail", jsonObj.toString());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
