/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;

import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.airAd.yaqinghui.MyApplication;

/**
 * BadgeService.java
 * 
 * @author liyuhang
 */
public class BadgeService extends BaseService {

	public Map<Integer, Integer> getBadgesMapData(String userId) {
		Map<Integer, Integer> ret = new HashMap<Integer, Integer>();
		SQLiteDatabase db = MyApplication.getCurrentReadDB();
		//
		Cursor cur = db
				.rawQuery(
						"select badge_id, count(1) num from badges where user_id = ? group by badge_id",
						new String[]{userId});
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			int badgeId = cur.getInt(0);
			int num = cur.getInt(1);
			ret.put(badgeId, num);
			cur.moveToNext();
		}
		cur.close();
		return ret;
	}
}
