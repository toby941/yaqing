/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.model;

import java.util.ArrayList;
import java.util.List;

import com.airAd.yaqinghui.R;

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
	private String days;

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

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public static List<Game> instanceList(JSONObject jsonObj) {
		List<Game> games = new ArrayList<Game>();
		JSONArray gameArray = jsonObj.getJSONArray("GameTypeInfo");
		for (int i = 0; i < gameArray.size(); i++) {
			Game game = new Game();
			JSONObject obj = gameArray.getJSONObject(i);
			game.setId(obj.getString("id"));
			game.setName(obj.getString("name"));
			game.setPic(obj.getString("pic"));
			game.setDays(obj.optString("days"));
			games.add(game);
		}
		return games;
	}

	public String toString() {
		return id + "," + name + "," + pic;
	}

	public static int getResourceId(String type) {
		if ("footb".equals(type)) {
			return R.drawable.footb;
		} else if ("squash".equals(type)) {
			return R.drawable.squash;
		} else if ("rugby".equals(type)) {
			return R.drawable.rugby;
		} else if ("golf".equals(type)) {
			return R.drawable.golf;
		} else if ("fencing".equals(type)) {
			return R.drawable.fencing;
		} else if ("weightlifing".equals(type)) {
			return R.drawable.weightlifing;
		} else if ("basketba".equals(type)) {
			return R.drawable.basketba;
		} else if ("table-tennis".equals(type)) {
			return R.drawable.table_tennis;
		} else if ("judo".equals(type)) {
			return R.drawable.judo;
		} else if ("shoutting".equals(type)) {
			return R.drawable.shoutting;
		} else if ("handball".equals(type)) {
			return R.drawable.handball;
		} else if ("athletic".equals(type)) {
			return R.drawable.athletic;
		} else if ("diving".equals(type)) {
			return R.drawable.diving;
		} else if ("tennis".equals(type)) {
			return R.drawable.tennis;
		} else if ("swimming".equals(type)) {
			return R.drawable.swimming;
		} else if ("badminton".equals(type)) {
			return R.drawable.badminton;
		} else if ("taekwondo".equals(type)) {
			return R.drawable.taekwondo;
		}
		return R.drawable.run;
	}
}
