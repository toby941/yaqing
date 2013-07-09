/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.model;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Game.java
 *
 * @author liyuhang
 */
public class Game {	
	private String id;
	private String name;
	private String type; // 比赛、训练
	private String pic;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public static List<Game> instanceList(JSONObject jsonObj)
	{
		List<Game> games = new ArrayList<Game>();
		JSONArray gameArray = jsonObj.getJSONArray("GameTypeInfo");
		for (int i = 0; i < gameArray.size(); i++) {
			Game game = new Game();
			JSONObject obj = gameArray.getJSONObject(i);
			game.setId(obj.getString("id"));
			game.setName(obj.getString("name"));
			game.setPic(obj.getString("pic"));
			games.add(game);
		}
		return games;
	}
	
	public String toString()
	{
		return id + "," + name + "," + pic;
	}
}
