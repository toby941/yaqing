package com.airAd.yaqinghui;

import java.util.Stack;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import cn.jpush.android.api.JPushInterface;

import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.db.DatabaseService;

/**
 * @authorPanyi
 */
public class MyApplication extends Application {
	private DisplayMetrics metrics = new DisplayMetrics();
	private static MyApplication current;
	private static Stack<Object> stack;
	private User user;
	private DatabaseService dbService;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static MyApplication getCurrentApp() {
		return current;
	}

	public static User getCurrentUser() {
		return getCurrentApp().getUser();
	}

	public static SQLiteDatabase getCurrentReadDB() {
		return getCurrentApp().getDbService().getReadDatabase();
	}

	public static SQLiteDatabase getCurrentWirteDB() {
		return getCurrentApp().getDbService().getWirteDatabase();
	}

	public static void push(Object obj) {
		stack.push(obj);
	}

	public static Object pop() {
		return stack.pop();
	}

	@Override
	public void onCreate() {
		super.onCreate();
	//	JPushInterface.setDebugMode(true);
	//	JPushInterface.init(this);
		current = this;
		stack = new Stack<Object>();
		dbService = new DatabaseService(this);
	}

	@Override
	public void onTerminate() {
		if (stack != null) {
			stack.clear();
		}
		super.onTerminate();
	}

	public DisplayMetrics getMetrics(Activity act) {
		if (metrics.widthPixels <= 0) {
			act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		}
		return metrics;
	}

	public DatabaseService getDbService() {
		return dbService;
	}

	public void setDbService(DatabaseService dbService) {
		this.dbService = dbService;
	}

}
