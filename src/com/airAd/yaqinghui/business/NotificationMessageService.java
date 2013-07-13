/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class NotificationMessageService extends BaseService {
	public List<NotificationMessage> getNoficationMessages() {
		return getMessagesByType(NotificationMessage.TYPE_NOTIFICATION);
	}
	public List<NotificationMessage> getMessagesByType(int type) {
		List<NotificationMessage> messages = new ArrayList<NotificationMessage>();
		SQLiteDatabase db = MyApplication.getCurrentReadDB();
		Cursor cur = db
				.rawQuery(
						"select cid, title, content,user_id,message_type,add_time,read_flag from messages where message_type = ? order by add_time desc",
						new String[]{type + ""});
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			NotificationMessage message = new NotificationMessage();
			message.setCid(cur.getLong(0));
			message.setTitle(cur.getString(1));
			message.setContent(cur.getString(2));
			message.setUserId(cur.getString(3));
			message.setMessageType(cur.getInt(4));
			Long time = cur.getLong(5);
			message.setAddTimeStr(ApiUtil
					.convertDateToDateString(new Date(time)));
			message.setReadFlag(cur.getInt(6));
			messages.add(message);
			//
			cur.moveToNext();
		}
		cur.close();
		return messages;
	}

	public long addMessage(NotificationMessage message) {
		long result = -1;
		SQLiteDatabase db = MyApplication.getCurrentWirteDB();
		ContentValues cValue = new ContentValues();
		//
		cValue.put("user_id", message.getUserId());
		cValue.put("title", message.getTitle());
		cValue.put("content", message.getContent());
		cValue.put("message_type", message.getMessageType());
		cValue.put("read_flag", message.getReadFlag());
		cValue.put("add_time", message.getAddTimel());
		//
		result = db.insert("messages", null, cValue);
		// 如果是活动，则插入个人行程
		if (NotificationMessage.TYPE_CEPEVENT_SIGNUP_HIS.equals(message
				.getMessageType())) {
			ScheduleItem si = new ScheduleService().getCepEventScheduleItem(
					message.getCepId(), message.getEventId());
			if (si == null) {
				Cep cep = new CepService().getCep(message.getUserId(),
						message.getCepId());
				CepEvent cepEvent = null;
				List<CepEvent> events = cep.getCepEvents();
				for(CepEvent event : events)
				{
					if(message.getEventId().equals(event.getId())){
						cepEvent = event;
						break;
					}
				}
				// 村外活动，数据库里本来没记录，插入
				ContentValues params = new ContentValues();
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
		}
		return result;
	}

	public NotificationMessage getMessage(long id) {
		NotificationMessage message = new NotificationMessage();
		SQLiteDatabase db = MyApplication.getCurrentReadDB();
		Cursor cur = db
				.rawQuery(
						"select cid, title, content,user_id,message_type,add_time,read_flag from messages where cid = ?",
						new String[]{id + ""});
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			message.setCid(cur.getLong(0));
			message.setTitle(cur.getString(1));
			message.setContent(cur.getString(2));
			message.setUserId(cur.getString(3));
			message.setMessageType(cur.getInt(4));
			Long time = cur.getLong(5);
			message.setAddTimeStr(ApiUtil
					.convertDateToDateString(new Date(time)));
			message.setReadFlag(cur.getInt(6));
			//
			cur.moveToNext();
		}
		cur.close();
		//

		return message;
	}

	public Integer markMessageAsRead(long id) {
		SQLiteDatabase db = MyApplication.getCurrentWirteDB();
		ContentValues cv = new ContentValues();
		cv.put("read_flag", NotificationMessage.READ);
		String[] args = {String.valueOf(id)};
		return db.update("messages", cv, "cid = ?", args);
	}
}// end class
