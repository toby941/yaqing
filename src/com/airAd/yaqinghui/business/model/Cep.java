/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.model;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * Cep.java
 * 
 * @author liyuhang
 */
public class Cep {
	private String id;
	private String title;
	private String content;
	private String iconType;
	private String pic;
	private String score;
	private List<String> pics;
	private List<CepEvent> cepEvents;

	//

	public String getId() {
		return id;
	}

	public String getIconType() {
		return iconType;
	}

	public void setIconType(String iconType) {
		this.iconType = iconType;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getPics() {
		return pics;
	}

	public void setPics(List<String> pics) {
		this.pics = pics;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public List<CepEvent> getCepEvents() {
		return cepEvents;
	}

	public void setCepEvents(List<CepEvent> cepEvents) {
		this.cepEvents = cepEvents;
	}

	public static Cep instance(JSONObject jsonObj) throws JSONException {
		Cep cep = new Cep();
		JSONObject obj = jsonObj.getJSONObject("TheOneCepInfo");
		cep.setId(obj.optString("cepid"));
		cep.setTitle(obj.getString("ceptitle"));
		cep.setContent(obj.getString("cepcontent"));
		cep.setScore(obj.getString("score"));
		cep.setIconType(obj.getString("type"));
		//		ArrayList<String> pics = new ArrayList<String>();
		//		String[] picArray = obj.getString("ceppictures").split(",");
		//		for (int i = 0; i < picArray.length; i++) {
		//			pics.add(picArray[i]);
		//		}
		//		cep.setPics(pics);
		//		cep.setPic(obj.optString("ceppictureone"));
		// cepevent
		JSONArray eventsArr = obj.optJSONArray("event");
		List<CepEvent> events = new ArrayList<CepEvent>();
		if (eventsArr != null) {
			for (int i = 0; i < eventsArr.size(); i++) {
				JSONObject eventObj = eventsArr.getJSONObject(i);
				CepEvent event = new CepEvent();
				event.setCepId(cep.getId());
				event.setId(eventObj.getString("eventid"));
				event.setStartTime(eventObj.optString("begintime"));
				event.setEndTime(eventObj.optString("endtime"));
				event.setPlace(eventObj.optString("cepplace"));
				event.setMaxNum(Integer.parseInt(eventObj.optString("joinnum")));
				event.setAttendNum(Integer.parseInt(eventObj
						.optString("signupnum")));
				events.add(event);
			}
			cep.setCepEvents(events);
		}
		//
		return cep;
	}

	public static List<Cep> instanceList(JSONObject jsonObj)
			throws JSONException {
		List<Cep> ceps = new ArrayList<Cep>();
		JSONArray cepArray = jsonObj.getJSONArray("AllCepInfo");
		for (int i = 0; i < cepArray.size(); i++) {
			Cep cep = new Cep();
			JSONObject obj = cepArray.getJSONObject(i);
			cep.setId(obj.getString("cepid"));
			cep.setTitle(obj.optString("ceptitle"));
			cep.setContent(obj.optString("cepcontent"));
			cep.setPic(obj.optString("ceppicture"));
			cep.setScore(obj.optString("score"));
			cep.setIconType(obj.getString("type"));
			cep.setScore(obj.getString("score"));
			ceps.add(cep);
		}// end loop
		return ceps;
	}
	//
}
