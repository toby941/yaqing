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

}
