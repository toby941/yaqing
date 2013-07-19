package com.airAd.yaqinghui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.weibo.ImageAndText;
import com.weibo.ImageAndTextListAdapter;
import com.weibo.WeiboService;
import com.weibo.WeiboUtil;

public class ShareFriendActivity extends BaseActivity {
	private ListView mListView;
	private ImageAndTextListAdapter arr;
	private ImageButton mBack;
	private ProgressDialog progressDialog;
	public static Handler handler;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_friend);
		init();
		bindEvent();
	}

	// 初始化数据
	private void init() {
		mListView = (ListView) findViewById(R.id.findList);
		mListView.setOnItemClickListener(new ItemClickListener());
		mBack = (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBack.setOnClickListener(new BackClick());

		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(R.string.dialog_title);
		progressDialog.setMessage(getResources().getText(R.string.dialog_msg));
		progressDialog.setCancelable(true);

	}

	private final class BackClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			ShareFriendActivity.this.finish();

		}
	}// end inner class
		// 绑定页面监听

	private void bindEvent() {
		// @动作
		if(WeiboUtil.friendList.size()>0){
			writeList();
		}else{
			progressDialog.show();
		}
		  
        handler = new Handler(){ 
 
            @Override 
            public void handleMessage(Message msg) { 
                super.handleMessage(msg); 
                if(msg.what==1){ 
                	writeList();
                } 
            } 
             
        }; 
		

	}

	class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			ListView fristList = (ListView) parent;
			ImageAndText map = (ImageAndText) fristList
					.getItemAtPosition(position);
			Intent intent = new Intent();
			//intent.setClass(ShareFriendActivity.this, ShareActivity.class);
			intent.putExtra("uName", map.getText());
			setResult(CONTEXT_RESTRICTED, intent);
			//startActivity(intent);
			//overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			finish();

		}

	}


	private void writeList() {
		progressDialog.dismiss();
		arr = new ImageAndTextListAdapter(ShareFriendActivity.this,
				WeiboUtil.friendList, mListView);
		mListView.setAdapter(arr);
	}
	

}
