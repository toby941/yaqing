/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * GameInfo.java
 * 
 * @author liyuhang
 */
public class GameInfo {

	private String title;
	private String place;
	private String id;
	private String time;
	private String content;
	private String pic;
	private boolean game;

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyyMMdd HH:mm");

	/**
	 * 
	 * @param jsonObj
	 * @return
	 */
	public static List<GameInfo> instanceList(JSONObject jsonObj) {
		List<GameInfo> games = new ArrayList<GameInfo>();
		JSONArray gameArray = jsonObj.getJSONArray("GameTypeInfo");
		for (int i = 0; i < gameArray.size(); i++) {
			GameInfo gameInfo = new GameInfo();
			JSONObject obj = gameArray.getJSONObject(i);
			gameInfo.setId(obj.getString("id"));
			gameInfo.setTime(obj.getString("time"));
			gameInfo.setPlace(obj.getString("place"));
			gameInfo.setTitle(obj.getString("title"));
			gameInfo.setContent(obj.getString("content"));
			gameInfo.setIsGame(obj.getString("flag"));
			games.add(gameInfo);
		}
		return games;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public void setIsGame(String flag) {
		if ("1".equals(flag)) {
			game = true;
		} else {
			game = false;
		}
	}
	
	public boolean isGame()
	{
		return game;
	}

	public long getStartTime() {
		try {
			return sdf.parse(time.substring(0, time.indexOf("-"))).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public int getDay()
	{
		Calendar c = Calendar.getInstance(); 
		try {
			c.setTime(sdf.parse(time.substring(0, time.indexOf("-"))));
			return c.get(Calendar.DAY_OF_MONTH);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
