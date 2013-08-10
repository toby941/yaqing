package com.airAd.yaqinghui;

import java.util.Stack;

import org.json.JSONException;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.db.DatabaseService;
import com.airAd.yaqinghui.exception.CrashHandler;
import com.airAd.yaqinghui.exception.Sentry;
import com.airAd.yaqinghui.exception.Sentry.SentryEventBuilder;
import com.airAd.yaqinghui.exception.Sentry.SentryEventCaptureListener;

/**
 * @authorPanyi
 */
public class MyApplication extends Application {
	private DisplayMetrics metrics = new DisplayMetrics();
	private static MyApplication current;
	private static Stack<Object> stack;
	private User user;
	private DatabaseService dbService;
	private Context mContext;
	protected CrashHandler crashHandler;

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
		// JPushInterface.setDebugMode(true);
		// JPushInterface.init(this);
		mContext = this;
		current = this;
		stack = new Stack<Object>();
		dbService = new DatabaseService(this);

		Sentry.setCaptureListener(new SentryEventCaptureListener() {
			@Override
			public SentryEventBuilder beforeCapture(SentryEventBuilder builder) {
				// Needs permission - <uses-permission
				// android:name="android.permission.ACCESS_NETWORK_STATE" />
				ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
				NetworkInfo mWifi = connManager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				PackageManager pm = mContext.getPackageManager();
				try {
					PackageInfo pi = pm.getPackageInfo(
							mContext.getPackageName(), 0);
					builder.getExtra().put("wifi",
							String.valueOf(mWifi.isConnected()));
					builder.getExtra().put("version", pi.versionName);
				} catch (JSONException e) {
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				return builder;
			}
		});
		// Sentry will look for uncaught exceptions from previous runs and send
		// them
		Sentry.init(this, CrashHandler.rawDsn);
		// Capture event
		crashHandler = new CrashHandler();
		crashHandler.init(this);
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
