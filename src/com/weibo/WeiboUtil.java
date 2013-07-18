package com.weibo;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airAd.yaqinghui.BaseActivity;
import com.airAd.yaqinghui.CepDetailActivity;
import com.airAd.yaqinghui.ShareActivity;
import com.airAd.yaqinghui.ShareFriendActivity;
import com.airAd.yaqinghui.util.MethodObject;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.FriendshipsAPI;

public class WeiboUtil {
	private static final String CONSUMER_KEY = "1904063158";// 替换为开发者的appkey，例如"1646212860";
	private static final String REDIRECT_URL = "http://www.sina.com";
	private Weibo mWeibo;
	protected static FriendshipsAPI f;
	protected static long uid = 0;

	public static Oauth2AccessToken accessToken;
	public static List<ImageAndText> friendList = new ArrayList<ImageAndText>();
	public static Boolean isLoding = false;
	public static Handler handler;
	public static String token;
	public static String expires_in;

	public void login(BaseActivity activit, PropertiesService pro) {

		mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
		mWeibo.authorize(activit, new AuthDialogListener(activit, pro));
	}

	// 回掉
	class AuthDialogListener implements WeiboAuthListener {

		private BaseActivity hand;
		private PropertiesService pro;

		public AuthDialogListener(BaseActivity hand, PropertiesService pro) {
			// TODO Auto-generated constructor stub
			this.hand = hand;
			this.pro = pro;
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(Bundle values) {
			// TODO Auto-generated method stub
			token = values.getString("access_token");
			expires_in = values.getString("expires_in");
			initToken(token, expires_in);
			if (accessToken.isSessionValid()) {
				pro.putString("token", WeiboUtil.token);
				pro.putString("expires_in", WeiboUtil.expires_in);
				pro.saveConfig();
				hand.startService(new Intent(hand, WeiboService.class));
				Intent it= new Intent(hand, ShareActivity.class);
				hand.startActivity(it);
			}
		}

		@Override
		public void onError(WeiboDialogError arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
		}

	}

	public void initToken(String token, String expires_in) {

		accessToken = new Oauth2AccessToken(token, expires_in);
	}

	

}
