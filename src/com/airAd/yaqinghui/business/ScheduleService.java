/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
public class ScheduleService extends BaseService
{
	/**
	 * @param userId
	 * @param Date
	 *            传12 就是 12号
	 * @return
	 */
	public List<ScheduleItem> getScheduleItemsByDate(String userId, int Date)
	{
		List<ScheduleItem> scheduleItems= new ArrayList<ScheduleItem>();
		SQLiteDatabase db= MyApplication.getCurrentReadDB();
		Cursor cur= db
				.rawQuery(
						"select cid, user_id, item_type,title, place, icon_type, start_time, add_time, ref_id ,day, cep_id from schedule where user_id = ? and day = ? order by start_time",
						new String[]
						{userId, Date + ""});
		cur.moveToFirst();
		while (!cur.isAfterLast())
		{
			ScheduleItem item= new ScheduleItem();
			item.setCid(cur.getLong(0));
			item.setUserId(cur.getString(1));
			item.setItemType(cur.getInt(2));
			item.setTitle(cur.getString(3));
			item.setPlace(cur.getString(4));
			item.setIconType(cur.getString(5));
			item.setStartTime(new Date(cur.getLong(6)));
			item.setStartTimel(cur.getLong(6));
			item.setAddTime(new Date(cur.getLong(7)));
			item.setRefId(cur.getString(8));
			item.setDay(cur.getInt(9));
			item.setCepId(cur.getString(10));
			scheduleItems.add(item);
			//
			cur.moveToNext();
		}
		cur.close();
		return scheduleItems;
	}
	
	/**
	 * @param userId
	 * @param Date
	 *            传12 就是 12号
	 * @return
	 */
	public List<ScheduleItem> getScheduleItems(String userId)
	{
		List<ScheduleItem> scheduleItems= new ArrayList<ScheduleItem>();
		SQLiteDatabase db= MyApplication.getCurrentReadDB();
		Cursor cur= db
				.rawQuery(
						"select cid, user_id, item_type,title, place, icon_type, start_time, " +
						"add_time, ref_id ,day, cep_id from schedule where user_id = ?",
						new String[]
						{userId});
		cur.moveToFirst();
		while (!cur.isAfterLast())
		{
			ScheduleItem item= new ScheduleItem();
			item.setCid(cur.getLong(0));
			item.setUserId(cur.getString(1));
			item.setItemType(cur.getInt(2));
			item.setTitle(cur.getString(3));
			item.setPlace(cur.getString(4));
			item.setIconType(cur.getString(5));
			item.setStartTime(new Date(cur.getLong(6)));
			item.setStartTimel(cur.getLong(6));
			item.setAddTime(new Date(cur.getLong(7)));
			item.setRefId(cur.getString(8));
			item.setDay(cur.getInt(9));
			item.setCepId(cur.getString(10));
			scheduleItems.add(item);
			//
			cur.moveToNext();
		}
		cur.close();
		return scheduleItems;
	}
	/**
	 * @param userId
	 * @return {13 => 1, 14 => 2}
	 */
	public Map<Integer, Integer> getCalendlarScheduleData(String userId)
	{
		Map<Integer, Integer> ret= new HashMap<Integer, Integer>();
		SQLiteDatabase db= MyApplication.getCurrentReadDB();
		//
		Cursor cur= db.rawQuery("select day, count(1) num from schedule where user_id = ? group by day", new String[]
		{userId});
		cur.moveToFirst();
		while (!cur.isAfterLast())
		{
			int day= cur.getInt(0);
			int num= cur.getInt(1);
			ret.put(day, num);
			cur.moveToNext();
		}
		cur.close();
		//
		return ret;
	}
	// 删除个人行程
	public int doDelScheduleItem(String userId, long scheduleItemId)
	{
		Map<String, Object> errMap= new HashMap<String, Object>();
		SQLiteDatabase db= MyApplication.getCurrentWirteDB();
		int cur= db.delete("schedule", "cid = ? and user_id= ?", new String[]
		{scheduleItemId + "", userId});
		return cur;
	}
	//
	public ScheduleItem getCepEventScheduleItem(String cepId, String eventId)
	{
		ScheduleItem scheduleItem= null;
		SQLiteDatabase db= MyApplication.getCurrentReadDB();
		Cursor cur= db.rawQuery(
				"select cid, user_id, item_type, title,place,icon_type from schedule where cep_id = ? and ref_id = ?",
				new String[]
				{cepId, eventId});
		cur.moveToFirst();
		while (!cur.isAfterLast())
		{
			scheduleItem= new ScheduleItem();
			scheduleItem.setCid(cur.getLong(0));
			//
			cur.moveToNext();
		}
		cur.close();
		return scheduleItem;
	}
}
