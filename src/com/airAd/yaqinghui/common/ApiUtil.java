/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

/**
 * ApiUtil.java
 * 
 * @author liyuhang
 */
public class ApiUtil {
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	public static Date convertDateStringToDate(String str) {
		try {
			return sdf.parse(str);
		} catch (Exception e) {
			return new Date();
		}
	}

	public static String convertDateToDateString(Date date) {
		try {
			return sdf.format(date);
		} catch (Exception e) {
			Log.e("etestConvertDateToDateString", e.getMessage());
			return "";
		}
	}

	public static String formatDate(String dateStr) {
		Date date = convertDateStringToDate(dateStr);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return df.format(date);
	}

	public static String formatDate(Long ts) {
		Date date = new Date(ts);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return df.format(date);
	}

	public static String getSportsNameByType(String type)
	{
		if ("athletic".equals(type)) {
			return "田径,Atheltics";
		} else if ("squash".equals(type)) {
			return "壁球,Squash";
		} else if ("rugby".equals(type)) {
			return "橄榄球,Rugby";
		} else if ("golf".equals(type)) {
			return "高尔夫球,Golf";
		} else if ("fencing".equals(type)) {
			return "击剑,Fencing";
		} else if ("weightlifing".equals(type)) {
			return "举重,Weightlifting";
		} else if ("basketba".equals(type)) {
			return "篮球,Basketball";
		} else if ("table-tennis".equals(type)) {
			return "乒乓球,Table Tennis";
		} else if ("judo".equals(type)) {
			return "柔道,Judo";
		} else if ("shoutting".equals(type)) {
			return "射击,Shooting";
		} else if ("handball".equals(type)) {
			return "手球,Handball";
		} else if ("diving".equals(type)) {
			return "跳水,Diving";
		} else if ("tennis".equals(type)) {
			return "网球,Tennis";
		} else if ("swimming".equals(type)) {
			return "游泳,Swimming";
		} else if ("badminton".equals(type)) {
			return "羽毛球,Badminton";
		} else if ("footb".equals(type)) {
			return "足球,Football";
		} else if ("taekwondo".equals(type)) {
			return "跆拳道,aekwondo";
		}

		return "亚青,Sports";
	}

}
