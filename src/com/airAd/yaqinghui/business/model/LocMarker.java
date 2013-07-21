/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.model;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * LocMarker.java
 *
 * @author liyuhang
 */
public class LocMarker {

	private String id;
	private double lon;
	private double lat;
	private String address;
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static List<LocMarker> instanceList(JSONObject jsonObj) {
		List<LocMarker> locMarkers = new ArrayList<LocMarker>();
		JSONArray locArray = jsonObj.getJSONArray("GameTypeInfo");
		for (int i = 0; i < locArray.size(); i++) {
			JSONObject obj = locArray.getJSONObject(i);
			double lon = obj.optDouble("lon", -1);
			double lat = obj.optDouble("lat", -1);
			//只添加有经纬度的
			if(lon > 0 && lat > 0)
			{
				LocMarker marker = new LocMarker();
				marker.setId(obj.optString("id"));
				marker.setName(obj.optString("name"));
				marker.setLon(lon);
				marker.setLat(lat);
				marker.setAddress(obj.optString("address"));
				locMarkers.add(marker);
			}
			
		}
		return locMarkers;
	}
	
	public String toString()
	{
		return id + ","+ name+"," + lon + "," + lat + "," + address;
	}
}
