package com.airAd.yaqinghui.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class InitDatabase extends SQLiteOpenHelper {

	public InitDatabase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 勋章记录表
        db.execSQL("CREATE TABLE badges (cid INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,"
                + " badge_id VARCHAR(30), add_time LONG, user_id VARCHAR(30), cep_id VARCHAR(30), "
				+ "event_id VARCHAR(30));");
		// 消息记录表
		db.execSQL("CREATE TABLE messages (cid INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "
				+ "message_type INTEGER, title VARCHAR(100), content VARCHAR(1000), add_time LONG,"
				+ " user_id VARCHAR(30), read_flag INTEGER, cep_id VARCHAR(100), cep_title VARCHAR(100), cep_place VARCHAR(100), cep_start_time LONG);");
		// 日程记录表
		db.execSQL("CREATE TABLE schedule (cid INTEGER PRIMARY KEY  NOT NULL ,user_id VARCHAR(30) NOT NULL ,"
				+ "item_type INTEGER,title VARCHAR(100),place VARCHAR(100),icon_type VARCHAR(20),start_time LONG,add_time LONG,"
				+ "ref_id VARCHAR(100),day INTEGER,time_str VARCHAR(100), cep_id VARCHAR(20));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}// end class
