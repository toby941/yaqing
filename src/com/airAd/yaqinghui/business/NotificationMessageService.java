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
import com.airAd.yaqinghui.business.model.NoficationMessage;
import com.airAd.yaqinghui.common.ApiUtil;
/**
 * MessageService.java
 * 
 * @author liyuhang
 */
public class NotificationMessageService extends BaseService {
	public List<NoficationMessage> getNoficationMessages() {
		List<NoficationMessage> messages = new ArrayList<NoficationMessage>();
		SQLiteDatabase db = MyApplication.getCurrentReadDB();
		Cursor cur = db
				.rawQuery(
						"select cid, title, content,user_id,message_type,add_time,read_flag from messages order by add_time desc",
						new String[]{});
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			NoficationMessage message = new NoficationMessage();
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
	public List<NoficationMessage> getCepEventHistoryMessages(int type) {
		List<NoficationMessage> messages = new ArrayList<NoficationMessage>();
		SQLiteDatabase db = MyApplication.getCurrentReadDB();
		Cursor cur = db
				.rawQuery(
						"select cid, title, content,user_id,message_type,add_time,read_flag from messages order by add_time desc",
						new String[]{});
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			NoficationMessage message = new NoficationMessage();
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

	public long addMessage(NoficationMessage message) {
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
		return result;
	}

	public NoficationMessage getMessage(long id) {
		NoficationMessage message = new NoficationMessage();
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
		return message;
	}

	public Integer markMessageAsRead(long id) {
		SQLiteDatabase db = MyApplication.getCurrentWirteDB();
		ContentValues cv = new ContentValues();
		cv.put("read_flag", NoficationMessage.READ);
		String[] args = {String.valueOf(id)};
		return db.update("messages", cv, "cid = ?", args);
	}
}// end class
