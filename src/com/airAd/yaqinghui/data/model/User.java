package com.airAd.yaqinghui.data.model;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * @author Panyi
 * 
 */
public class User {
	private String temp;
	private String id;
	private String name;
	private String country;
	private String flag;
	private String gender;
	private String[] types;

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public static User instance(String src) {
		JSONObject jsonObject = null;
		try {
			jsonObject = JSONObject.fromObject(src);   
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		return instance(jsonObject);
	}

	public static User instance(JSONObject jsonObject) {
		try {
			User user = new User();
			user.temp = jsonObject.toString();
			JSONObject obj = jsonObject.getJSONObject("UserInfo");
			user.setId(obj.getString("id"));
			user.setName(obj.getString("name"));
			user.setCountry(obj.getString("country"));
			user.setFlag(obj.getString("flag"));
			user.setGender(obj.getString("gender"));
			user.setTypes(obj.getString("type").split(","));
			return user;
		} catch (Exception e) {
			return null;
		}
	}
}// end class
