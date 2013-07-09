/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.business.model.ScheduleItem;

/**
 * ScheduleService.java
 * 
 * @author liyuhang
 */
public class ScheduleService extends BaseService {
	/**
	 * @param userId
	 * @param Date
	 *            传12 就是 12号
	 * @return
	 */
	public List<ScheduleItem> getScheduleItemsByDate(String userId, int Date) {
		ScheduleItem item1 = new ScheduleItem();
		item1.setTitle("男子200m花样游泳决赛");
		item1.setPlace("奥体中心");
		item1.setItemType(ScheduleItem.TYPE_GAME);
		item1.setPic("diving.png");
		item1.setTimeStr(System.currentTimeMillis() + "");
		ScheduleItem item2 = new ScheduleItem();
		item2.setTitle("亚洲文化村");
		item2.setPlace("亚洲文化村活动中心");
		item2.setItemType(ScheduleItem.TYPE_CEP_EVENT);
		item2.setPic("cep_type_red.png");
		item2.setTimeStr(System.currentTimeMillis() + "");
		ScheduleItem item3 = new ScheduleItem();
		item3.setTitle("亚洲文化村");
		item3.setPlace("亚洲文化村活动中心");
		item3.setItemType(ScheduleItem.TYPE_TRAINING);
		item3.setPic("tennis.png");
		item3.setTimeStr(System.currentTimeMillis() + "");
		List<ScheduleItem> ret = new LinkedList<ScheduleItem>();
		ret.add(item1);
		ret.add(item2);
		ret.add(item3);
		ret.add(item1);
		ret.add(item1);
		ret.add(item2);
		ret.add(item3);
		//
		return ret;
	}

	/**
	 * @param userId
	 * @return {13 => 1, 14 => 2}
	 */
	public Map<Integer, Integer> getCalendlarScheduleData(String userId) {
		Map<Integer, Integer> ret = new HashMap<Integer, Integer>();
		SQLiteDatabase db = MyApplication.getCurrentReadDB();
		//
		Cursor cur = db.rawQuery(
						"select day, count(1) num from schedule where user_id = ? group by day",
				new String[]{userId});
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			int day = cur.getInt(0);
			int num = cur.getInt(1);
			ret.put(day, num);
			cur.moveToNext();
		}
		cur.close();
		//
		return ret;
	}

	// 删除个人行程
	public Map<String, Object> doDelScheduleItem(String userId,
			int scheduleItemId) {
		Map<String, Object> errMap = new HashMap<String, Object>();
		return errMap;
	}
}
