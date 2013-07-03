package com.airAd.yaqinghui;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.db.DatabaseService;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.DisplayMetrics;

/**
 * 
 * @authorPanyi
 * 
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
	
	public static void push(Object obj) {
		stack.push(obj);
	}

	public static Object pop() {
		return stack.pop();
	}

	@Override
	public void onCreate() {
		current = this;
		stack = new Stack<Object>();
		super.onCreate();
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
}
