/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

/**
 * BaseService.java
 * 
 * @author liyuhang
 */
public class BaseService {
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	protected Date convertDateStringToDate(String str) {
		try {
			return sdf.parse(str);
		} catch (Exception e) {
			Log.e("etestConverDateStringToDate", e.getMessage());
			return new Date();
		}
	}

	protected String convertDateToDateString(Date date) {
		try {
			return sdf.format(date);
		} catch (Exception e) {
			Log.e("etestConvertDateToDateString", e.getMessage());
			return "";
		}
	}
}
