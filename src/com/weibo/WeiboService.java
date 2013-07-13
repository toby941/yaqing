package com.weibo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.airAd.yaqinghui.ShareFriendActivity;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.net.RequestListener;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Message;
import android.util.Property;

public class WeiboService extends Service {

	private int pageCount = 0;
	protected static SQLiteDatabase db;

	@Override
	public void onCreate() {
		if(db==null){
			db = openOrCreateDatabase("weibo.db", Context.MODE_PRIVATE, null);
		}
		super.onCreate();
		new Thread(new Runnable() {
			@Override
			public void run() {
				 List<ImageAndText> list= findData();
				if (list == null) {
					findFriends();
				}else{
					WeiboUtil.friendList=list;
				}
			}
		}).start();
	}

	private void findFriends() {
		AccountAPI account = new AccountAPI(WeiboUtil.accessToken);
		account.getUid(new AccountListener());
	}

	// 列表数据处理
	private Boolean loadImageFromNetwork(String s) {

		// 可以在这里通过文件名来判断，是否本地有此图片
		handData(s);
		if (pageCount > 0) {
			WeiboUtil.f.friends(WeiboUtil.uid, 150, pageCount, true,
					new FriendListener());
		} else {
			return true;
		}

		return false;
	}

	private void handData(String s) {
		// 可以在这里通过文件名来判断，是否本地有此图片
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(s);

			JSONArray weiboInfo = jsonObj.getJSONArray("users");
			pageCount = jsonObj.getInt("next_cursor");
			jsonObj.getInt("total_number");

			for (int i = 0; i < weiboInfo.length(); i++) {

				JSONObject one = (JSONObject) weiboInfo.get(i);
				String imageUrl = one.getString("profile_image_url");
				String weName = one.getString("name");

				ImageAndText item = new ImageAndText(imageUrl, weName, "#");
				WeiboUtil.friendList.add(item);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class FriendListener implements RequestListener {

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			String a = arg0;

			new DownloadImageTask().execute(a);
		}

		@Override
		public void onError(WeiboException arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onIOException(IOException arg0) {
			// TODO Auto-generated method stub

		}

	}

	private class DownloadImageTask extends AsyncTask<String, Void, Boolean> {

		protected Boolean doInBackground(String... urls) {
			return loadImageFromNetwork(urls[0]);
		}

		protected void onPostExecute(Boolean data) {
			if (data) {
				if (findData() == null) {
					saveDB();
				}
				Message msg = new Message();
				msg.what = 1;
				if (ShareFriendActivity.handler != null) {
					ShareFriendActivity.handler.sendMessage(msg);
				}
				WeiboUtil.isLoding = true;
			}

		}
	}

	class AccountListener implements RequestListener {

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			String a = arg0;
			JSONObject jsonObj;
			WeiboUtil.f = new FriendshipsAPI(WeiboUtil.accessToken);
			try {
				jsonObj = new JSONObject(a);
				WeiboUtil.uid = jsonObj.getLong("uid");

				WeiboUtil.f.friends(WeiboUtil.uid, 150, pageCount, true,
						new FriendListener());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void onError(WeiboException arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onIOException(IOException arg0) {
			// TODO Auto-generated method stub

		}

	}

	// /========
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private void saveDB() {
		db.execSQL("DROP TABLE IF EXISTS friends");
		db.execSQL("CREATE TABLE friends (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, imgUrl VARCHAR)");
		for (int i = 0; i < WeiboUtil.friendList.size(); i++) {
			ImageAndText item = WeiboUtil.friendList.get(i);
			db.execSQL("INSERT INTO friends VALUES (NULL, ?, ?)", new Object[] {
					item.getText(), item.getImageUrl() });
		}
	}

	private List<ImageAndText> findData() {

		List<ImageAndText> list = null;
		if (tabIsExist("friends")) {
			list = new ArrayList<ImageAndText>();
			Cursor c = db.rawQuery("SELECT * FROM friends", null);
			if (c != null) {
				while (c.moveToNext()) {
					ImageAndText item = new ImageAndText(c.getString(c
							.getColumnIndex("imgUrl")), c.getString(c
							.getColumnIndex("name")), null);
					list.add(item);
				}
				c.close();
			}
		}
		return list;
	}

	private boolean tabIsExist(String tabName) {
		boolean result = false;
		if (tabName == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
					+ tabName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
}
