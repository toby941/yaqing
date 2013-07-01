package com.airAd.yaqinghui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * 基础Activity类
 * @author panyi a
 *
 */
public class BaseActivity extends SherlockFragmentActivity {
	private LayoutInflater mInflater;
	private PushReceiver mPushReceiver;
	private PopupWindow pop;
	private View popView;
	
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Sherlock_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPushReceiver = new PushReceiver();
    }

    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Constants.PUSH_SERVICE);
        registerReceiver(mPushReceiver, filter);
    }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(mPushReceiver);
    }
    
    private final class PushReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String str=intent.getStringExtra("status");
//			System.out.println(str);
			showPop(str);
		}
    } //end inner class
    
    protected void showPop(String content){
    	if(pop==null){
    		popView = mInflater.inflate(R.layout.confirm_cep, null);
    		pop = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
    		pop.setBackgroundDrawable(new BitmapDrawable());
			pop.setOutsideTouchable(true);
			pop.setFocusable(true);
    	}
    	TextView text = (TextView)popView.findViewById(R.id.confitm_text);
    	text.setText(content);
    	pop.setAnimationStyle(R.style.PopupAnimation);
    	if(getParentView()!=null){
    		pop.showAtLocation(getParentView(), Gravity.BOTTOM, 0, 0);
    	}
    }
    
    protected View getParentView(){
    	return findViewById(R.id.main);
    }
    
}
