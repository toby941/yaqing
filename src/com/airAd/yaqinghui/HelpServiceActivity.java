package com.airAd.yaqinghui;

import java.util.Locale;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;

/**
 * 
 * @author Panyi
 * 
 */
public class HelpServiceActivity extends BaseActivity {
	public static final String TYPE = "type";
	public static final int TYPE_TRAFFIC = 1;
	public static final int TYPE_RELIGION = 2;
	public static final int TYPE_EATTING = 3;
	public static final int TYPE_LAN = 4;
	private ImageButton mBack;
	private WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_service);
		init();
	}

	private void init() {
		mBack = (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBack.setOnClickListener(new BackClick());
		webView = (WebView) findViewById(R.id.webview);
		String lan = Locale.getDefault().getLanguage();
		int type = getIntent().getIntExtra(TYPE, 1);
		String url = "file:///android_asset/html/";
		if ("zh".equalsIgnoreCase(lan)) {// 显示中文
			switch (type) {
			case TYPE_TRAFFIC:
				url += "transport_zh.html";
				break;
			case TYPE_RELIGION:
				url += "religious_zh.html";
				break;
			case TYPE_EATTING:
				url += "food_zh.html";
				break;
			case TYPE_LAN:
				url += "lang_zh.html";
				break;
			default:
				url += "transport_zh.html";
				break;
			}// end switch
		} else {// 显示英文版
			switch (type) {
			case TYPE_TRAFFIC:
				url += "transport_en.html";
				break;
			case TYPE_RELIGION:
				url += "religious_en.html";
				break;
			case TYPE_EATTING:
				url += "food_en.html";
				break;
			case TYPE_LAN:
				url += "lang_en.html";
				break;
			default:
				url += "transport_en.html";
				break;
			}// end switch
		}
		webView.loadUrl(url);
	}

	private final class BackClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			HelpServiceActivity.this.finish();
		}
	}// end inner class
}// end class
