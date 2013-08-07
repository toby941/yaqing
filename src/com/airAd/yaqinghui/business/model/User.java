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
	/*
	中文： CHI
	英文： ENG
	日文： JAP
	越南文：VIE
	韩文： KOR
	泰文： THA
	俄语： RUS
	阿拉伯语: ARA
	香港繁体：HK
	*/
	public static String getLan() {
		String lan = Locale.getDefault().getLanguage();
		String country = Locale.getDefault().getCountry()==null? "":Locale.getDefault().getCountry().trim().toLowerCase() ;
		Log.d("htestGetLan", lan);
		Log.d("htestGetCountry", country);
//		if("ko".equalsIgnoreCase(lan)){
//			//韩国
//			return "KOR";
//		}else if("vi".equalsIgnoreCase(lan)){
//			return "VIE";
//		}else if("zh".equalsIgnoreCase(lan)){
//			if(country.contains("tw")|| country.contains("hk")){
//				return "HK";
//			}
//			return "CHI";
//		}else if("jp".equalsIgnoreCase(lan) || "ja".equalsIgnoreCase(lan)){
//			return "JAP";
//		}else if("ar".equalsIgnoreCase(lan)){
//			return "ARA";
//		}else if("ru".equalsIgnoreCase(lan)){
//			return "RUS";
//		}else if("th".equalsIgnoreCase(lan)){
//			return "THA";
//		}
		return "ARA";
	}
}// end class
