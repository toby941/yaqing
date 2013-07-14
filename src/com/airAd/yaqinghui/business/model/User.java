package com.airAd.yaqinghui.business.model;

import java.util.Locale;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import android.util.Log;

import com.airAd.yaqinghui.common.Constants;

/**
 * @author Panyi
 */
public class User extends Base {
	private String temp;
	private String id;
	private String name;
	private String country;
	private String countryFlag;
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

	public String getCountryFlag() {
		return countryFlag;
	}

	public void setCountryFlag(String countryFlag) {
		this.countryFlag = countryFlag;
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

	public static User instance(String src) throws JSONException {
		JSONObject jsonObject = null;
		jsonObject = JSONObject.fromObject(src);
		return instance(jsonObject);
	}

	public static User instance(JSONObject jsonObject) throws JSONException {
		User user = new User();
		user.temp = jsonObject.toString();
		JSONObject obj = jsonObject.getJSONObject("UserInfo");
		if (obj != null) {
			user.setId(obj.getString("id"));
			user.setName(obj.getString("name"));
			user.setCountry(obj.getString("country"));
			user.setCountryFlag(obj.getString("flag"));
			user.setGender(obj.getString("gender"));
			user.setTypes(obj.getString("type").split(","));
			user.setFlag(Constants.FLAG_SUCC);
		} else {
			obj = jsonObject.getJSONObject("ApiErrorInfo");
			user.setFlag(Constants.FLAG_ERR);
			user.setMsg(obj.optString("msg"));
		}
		return user;
	}

	public static String getLan() {
		Log.d("htestGetLan", Locale.getDefault().getLanguage());
		return "CHI";
		// return Locale.getDefault().getLanguage();
	}
}// end class
