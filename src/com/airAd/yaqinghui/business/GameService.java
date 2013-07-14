/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;
import android.content.ContentValues;
import android.database.Cursor;
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
			Log.i("GameService", jsonObj.toString());
			return Game.instanceList(jsonObj);
		} catch (Exception e) {
			return null;
		}
    }
	
	public List<GameInfo> getGameInfo(String typeId, Date date) {
		BasicAPI api = HessianClient.create();
		try {
			Log.i("GameService", typeId + "," + sdf.format(date) + User.getLan());
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
	public void addtoSchedule(GameInfo gameInfo, String picURL)
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
		cValue.put("start_time", gameInfo.getStartTime());
		cValue.put("icon_type", picURL);
		cValue.put("day", gameInfo.getDay());
		//
		db.insert("schedule", null, cValue);
	}
	
	/**
	 * 
	 * 从日程删除
	 * @param gameInfo
	 * @param picURL
	 */
	public void deleteFromSchedule(String gameId)
	{
		SQLiteDatabase db = MyApplication.getCurrentWirteDB();
		db.delete("schedule", "ref_id = ?", new String[]{gameId});
	}
	
	/**
	 * 获取schedule的全部id
	 * @return
	 */
	public List<String> queryScheduleIds()
	{
		SQLiteDatabase db = MyApplication.getCurrentWirteDB();
		Cursor cursor = db.query("schedule", new String[]{"ref_id"}, null, null, null, null, null);
		cursor.moveToFirst();
		List<String> res = new ArrayList<String>();
		while(!cursor.isAfterLast())
		{
			res.add(cursor.getString(0));
			cursor.moveToNext();
		}
		cursor.close();
		return res;
	}
}
