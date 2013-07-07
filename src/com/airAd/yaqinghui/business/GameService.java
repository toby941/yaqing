/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import android.util.Log;

import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.business.api.BasicAPI;
import com.airAd.yaqinghui.business.model.Game;
import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.core.HessianClient;

/**
 * GameService.java
 *
 * @author pengf
 */
public class GameService extends BaseService {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public List<Game> getGame() {
		BasicAPI api = HessianClient.create();
		try {
			JSONObject jsonObj = api.GetGameInfo(MyApplication.getCurrentUser().getId(), User.getLan());
			return Game.instanceList(jsonObj);
		} catch (Exception e) {
			return null;
		}
    }
	
	public List<Game> getGameInfo(String typeId, Date date) {
		BasicAPI api = HessianClient.create();
		try {
			JSONObject jsonObj = api.GetGameDetailInfo(typeId, sdf.format(date), User.getLan());
			Log.i("GameService", jsonObj.toString());
			return Game.instanceList(jsonObj);
		} catch (Exception e) {
			return null;
		}
    }
}
