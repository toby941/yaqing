/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.business.api.BasicAPI;
import com.airAd.yaqinghui.business.model.Game;
import com.airAd.yaqinghui.business.model.GameInfo;
import com.airAd.yaqinghui.business.model.ScheduleItem;
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
	
	public List<GameInfo> getGameInfo(String typeId, Date date) {
		BasicAPI api = HessianClient.create();
		try {
			JSONObject jsonObj = api.GetGameDetailInfo(typeId, sdf.format(date), User.getLan());
			Log.i("GameService", jsonObj.toString());
			return GameInfo.instanceList(jsonObj);
		} catch (Exception e) {
			return null;
		}
    }
	
	/**
	 * 添加到日程
	 */
	public void addtoSchedule(GameInfo gameInfo)
	{
		SQLiteDatabase db = MyApplication.getCurrentWirteDB();
		ContentValues cValue = new ContentValues();
		//
		cValue.put("user_id", MyApplication.getCurrentUser().getId());
		cValue.put("ref_id", gameInfo.getId());
		cValue.put("item_type", ScheduleItem.TYPE_GAME);
		cValue.put("title", gameInfo.getTitle());
		cValue.put("place", gameInfo.getPlace());
		cValue.put("add_time", new Date().getTime());
		cValue.put("time_str", gameInfo.getStartTime() + "");
		//
		db.insert("schedule", null, cValue);
	}
}
