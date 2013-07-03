package com.airAd.yaqinghui.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author Administrator
 *
 */
public class DatabaseService {
	public static final String DB_NAME = "yqqinghuidb";
	private InitDatabase sqliteOpen;
	private SQLiteDatabase db;

	public DatabaseService(Context context) {
		sqliteOpen = new InitDatabase(context, DB_NAME, null, 1);
		db = sqliteOpen.getWritableDatabase();
	}
	
}// end class
