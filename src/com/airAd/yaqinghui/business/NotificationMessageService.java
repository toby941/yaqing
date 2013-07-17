/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.business.model.Cep;
import com.airAd.yaqinghui.business.model.CepEvent;
import com.airAd.yaqinghui.business.model.NotificationMessage;
import com.airAd.yaqinghui.business.model.ScheduleItem;
import com.airAd.yaqinghui.common.ApiUtil;
/**
 * MessageService.java
 * 
 * @author liyuhang
 */
public class NotificationMessageService extends BaseService
{
	public int getUnReadMsg(String userId)
	{
		SQLiteDatabase db= MyApplication.getCurrentReadDB();
		Cursor cur = db
				.rawQuery(
						"select count(1) from messages where (user_id = ? or user_id = '-1') and message_type = 1 and read_flag=?",
						new String[]
		{userId, NotificationMessage.UNREAD + ""});
		cur.moveToFirst();
		int ret= 0;
		while (!cur.isAfterLast())
		{
			ret= cur.getInt(0);
			cur.moveToNext();
		}
		cur.close();

		return ret;
	}
	public Map<Integer, Integer> getHisMapData(String userId)
	{
		Map<Integer, Integer> ret= new HashMap<Integer, Integer>();
		SQLiteDatabase db= MyApplication.getCurrentReadDB();
		//
		Cursor cur= db.rawQuery(
				"select message_type, count(1) num from messages where user_id = ? group by message_type",
				new String[]
				{userId});
		//
		cur.moveToFirst();
		while (!cur.isAfterLast())
		{
			int messageType= cur.getInt(0);
			int num= cur.getInt(1);
			ret.put(messageType, num);
			cur.moveToNext();
		}
		cur.close();
		return ret;
	}
	public List<NotificationMessage> getNoficationMessages(String userId)
	{
		return getMessagesByType(userId, NotificationMessage.TYPE_NOTIFICATION);
	}
	public List<NotificationMessage> getMessagesByType(String userId, int type)
	{
		List<NotificationMessage> messages= new ArrayList<NotificationMessage>();
		SQLiteDatabase db= MyApplication.getCurrentReadDB();
		Cursor cur= db
				.rawQuery(
						"select cid, title, content,user_id,message_type,add_time,read_flag, cep_id, event_id, cep_title, cep_place, cep_start_time from messages where (user_id = ? or user_id = '-1') and message_type = ? order by add_time desc",
						new String[]
						{userId, type + ""});
		cur.moveToFirst();
		while (!cur.isAfterLast())
		{
			NotificationMessage message= new NotificationMessage();
			message.setCid(cur.getLong(0));
			message.setTitle(cur.getString(1));
			message.setContent(cur.getString(2));
			message.setUserId(cur.getString(3));
			message.setMessageType(cur.getInt(4));
			Long time= cur.getLong(5);
			message.setAddTimeStr(ApiUtil.convertDateToDateString(new Date(time)));
			message.setReadFlag(cur.getInt(6));
			message.setCepId(cur.getString(7));
			message.setEventId(cur.getString(8));
			message.setCepTitle(cur.getString(9));
			message.setCepPlace(cur.getString(10));
			message.setCepStartTime(cur.getLong(11));
			messages.add(message);
			//
			cur.moveToNext();
		}
		cur.close();
		return messages;
	}
	public long addMessage(NotificationMessage message)
	{
		long result= -1;
		SQLiteDatabase db= MyApplication.getCurrentWirteDB();
		if (NotificationMessage.TYPE_NOTIFICATION.equals(message
				.getMessageType()))
		{
			ContentValues cValue= new ContentValues();
			//
			cValue.put("user_id", StringUtils.isBlank(message.getUserId())
					? "-1"
					: message.getUserId());
			cValue.put("title", message.getTitle());
			cValue.put("content", message.getContent());
			cValue.put("message_type", message.getMessageType());
			cValue.put("read_flag", message.getReadFlag());
			cValue.put("add_time", message.getAddTimel());
			//
			result= db.insert("messages", null, cValue);
		}
		else if (NotificationMessage.TYPE_CEPEVENT_SIGNUP_HIS.equals(message.getMessageType()))
		{
			// 如果是活动，则插入个人行程
			ScheduleItem si = new ScheduleService().getCepEventScheduleItem(
					message.getCepId(), message.getEventId());
			// 查出对应的cepId
			Cep cep= new CepService().getCep(message.getUserId(), message.getCepId());
			CepEvent cepEvent= null;
			List<CepEvent> events= cep.getCepEvents();
			for (CepEvent event : events)
			{
				if (message.getEventId().equals(event.getId()))
				{
					cepEvent= event;
					break;
				}
			}
			// 如果个人行程中已经有则不重复插入
			if (si == null)
			{
				// 村外活动，数据库里本来没记录，插入
				ContentValues params= new ContentValues();
				//
				params.put("user_id", message.getUserId());
				params.put("item_type", ScheduleItem.TYPE_CEP_EVENT);
				params.put("title", cep.getTitle());
				params.put("place", cepEvent.getPlace());
				params.put("icon_type", cep.getIconType());
				params.put("start_time", cepEvent.getStartTimel());
				params.put("add_time", new Date().getTime());
				params.put("ref_id", message.getEventId());
				params.put("cep_id", message.getCepId());
				params.put("day", cepEvent.getStartDayOfMonth());
				//
				db.insert("schedule", null, params);
			}
			// 插入报名历史记录
			ContentValues cValue = new ContentValues();
			//
			cValue.put("user_id", message.getUserId());
			cValue.put("title", message.getContent());
			cValue.put("content", message.getTitle());
			cValue.put("message_type", message.getMessageType());
			cValue.put("read_flag", message.getReadFlag());
			cValue.put("add_time", message.getAddTimel());
			cValue.put("cep_id", cep.getId());
			cValue.put("cep_title", cep.getTitle());
			cValue.put("cep_place", cepEvent.getPlace());
			cValue.put("cep_start_time", cepEvent.getStartTimel());
			//
			result= db.insert("messages", null, cValue);
		}
		return result;
	}
	public NotificationMessage getMessage(long id)
	{
		NotificationMessage message= new NotificationMessage();
		SQLiteDatabase db= MyApplication.getCurrentReadDB();
		Cursor cur= db.rawQuery(
				"select cid, title, content,user_id,message_type,add_time,read_flag from messages where cid = ?",
				new String[]
				{id + ""});
		cur.moveToFirst();
		while (!cur.isAfterLast())
		{
			message.setCid(cur.getLong(0));
			message.setTitle(cur.getString(1));
			message.setContent(cur.getString(2));
			message.setUserId(cur.getString(3));
			message.setMessageType(cur.getInt(4));
			Long time= cur.getLong(5);
			message.setAddTimeStr(ApiUtil.convertDateToDateString(new Date(time)));
			message.setReadFlag(cur.getInt(6));
			//
			cur.moveToNext();
		}
		cur.close();
		//
		return message;
	}
	public Integer markMessageAsRead(long id)
	{
		SQLiteDatabase db= MyApplication.getCurrentWirteDB();
		ContentValues cv= new ContentValues();
		cv.put("read_flag", NotificationMessage.READ);
		String[] args=
		{String.valueOf(id)};
		return db.update("messages", cv, "cid = ?", args);
	}
}// end class
