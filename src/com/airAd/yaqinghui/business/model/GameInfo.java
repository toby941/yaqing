/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.model;

import java.util.ArrayList;
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
	
}
