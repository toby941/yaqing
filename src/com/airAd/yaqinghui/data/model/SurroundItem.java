package com.airAd.yaqinghui.data.model;

public class SurroundItem {
	private int id;
    private String name;
    private String station;
    private double lon;
    private double lat;
    private Integer type;
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
