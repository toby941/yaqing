/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
			JSONObject jsonObj = api.GetGameInfo(MyApplication.getCurrentUser()
					.getId(), User.getLan());
			Log.i("GameService", jsonObj.toString());
			return Game.instanceList(jsonObj);
		} catch (Exception e) {
			return null;
		}
	}

	public List<GameInfo> getGameInfo(String typeId, Date date) {
		BasicAPI api = HessianClient.create();
		try {
			Log.i("GameService",
					typeId + "," + sdf.format(date) + User.getLan());
			JSONObject jsonObj = api.GetGameDetailInfo(typeId,
					sdf.format(date), User.getLan());
			Log.i("GameService", jsonObj.toString());
			return GameInfo.instanceList(jsonObj);
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<GameInfo> getGameInfoDetailByPlace(String place, Date date) {
		BasicAPI api = HessianClient.create();
		try {
			Log.i("getGameInfoDetailByPlace",
					place + "," + sdf.format(date) + User.getLan());
			JSONObject jsonObj = api.GetGameDetailInfoPlace(place,
					sdf.format(date), User.getLan());
			Log.i("getGameInfoDetailByPlace", jsonObj.toString());
			return GameInfo.instanceList(jsonObj);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 添加到日程
	 */
	public int addtoSchedule(GameInfo gameInfo, String picURL) {
		SQLiteDatabase db = MyApplication.getCurrentWirteDB();
		ContentValues cValue = new ContentValues();
		//
		cValue.put("user_id", MyApplication.getCurrentUser().getId());
		cValue.put("ref_id", gameInfo.getId());
		if (gameInfo.isGame()) {
			cValue.put("item_type", ScheduleItem.TYPE_GAME);
		} else {
			cValue.put("item_type", ScheduleItem.TYPE_TRAINING);
		}
		cValue.put("title", gameInfo.getTitle());
		cValue.put("place", gameInfo.getPlace());
		cValue.put("add_time", new Date().getTime());
		cValue.put("start_time", gameInfo.getStartTime());
		cValue.put("icon_type", picURL);
		cValue.put("day", gameInfo.getDay());
		//
		db.insert("schedule", null, cValue);

		Cursor cursor = db.query("schedule", new String[] { "cid" }, "ref_id = ?",
				new String[] { gameInfo.getId() }, null, null, null);
		cursor.moveToFirst();
		int cid = cursor.getInt(0);
		cursor.close();
		return cid;
	}

	/**
	 * 
	 * 从日程删除
	 * 
	 * @param gameInfo
	 * @param picURL
	 */
	public void deleteFromSchedule(GameId gameId) {
		SQLiteDatabase db = MyApplication.getCurrentWirteDB();
		db.delete("schedule", "ref_id = ? AND start_time = ?", new String[] { gameId.id, gameId.startTime.getTimeInMillis() + "" });
	}

	/**
	 * 获取schedule的全部id
	 * 
	 * @return
	 */
	public List<String> queryScheduleIds() {
		SQLiteDatabase db = MyApplication.getCurrentWirteDB();
		Cursor cursor = db.query("schedule", new String[] { "ref_id" }, null,
				null, null, null, null);
		cursor.moveToFirst();
		List<String> res = new ArrayList<String>();
		while (!cursor.isAfterLast()) {
			res.add(cursor.getString(0));
			cursor.moveToNext();
		}
		cursor.close();
		return res;
	}
	
	/**
	 * 获取schedule的全部id
	 * 
	 * @return
	 */
	public List<GameId> queryScheduleIdAndStarttime() {
		SQLiteDatabase db = MyApplication.getCurrentWirteDB();
		Cursor cursor = db.query("schedule", new String[] { "ref_id" , "start_time"}, null,
				null, null, null, null);
		cursor.moveToFirst();
		List<GameId> res = new ArrayList<GameId>();
		while (!cursor.isAfterLast()) {
			res.add(GameId.createBy(cursor.getString(0), cursor.getLong(1)));
			cursor.moveToNext();
		}
		cursor.close();
		return res;
	}
	
	public static final class GameId
	{
		private String id;
		private Calendar startTime;
		
		public static GameId createBy(String id, long startTime)
		{
			GameId gameId = new GameId();
			gameId.id = id;
			gameId.startTime = Calendar.getInstance();
			gameId.startTime.setTimeInMillis(startTime);
			return gameId;
		}
		
		public boolean fit(String id, long startTime)
		{
			if(this.id.equals(id) 
					&& this.startTime.getTimeInMillis() == startTime)
				return true;
			return false;
		}
		
		public boolean equals(Object target)
		{
			if(target instanceof GameId)
			{
				GameId tGameInfo = (GameId)target;
				if(tGameInfo.fit(id, startTime.getTimeInMillis()))
					return true;
			}
			return false;
		}
	}
}
