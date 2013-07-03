package com.airAd.yaqinghui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;



















import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

import com.airAd.framework.util.ImageUtil;
import com.airAd.framework.worker.ImageCache;
import com.airAd.framework.worker.ImageFetcher;
import com.airAd.yaqinghui.factory.ImageFetcherFactory;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.net.RequestListener;

/**
 * @author Panyi
 * 
 */
public class ContactActivity extends BaseActivity {
	private ImageButton mBackBtn,mLogin;
	 private Weibo mWeibo;
	 private static final String CONSUMER_KEY = "90579958";// 替换为开发者的appkey，例如"1646212860";
	 private static final String REDIRECT_URL = "http://127.0.0.1";
	 public static Oauth2AccessToken accessToken;
	 
	 private JSONArray weiboInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		init();
	}

	private void init() {
		
		mBackBtn = (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBackBtn.setOnClickListener(new BackClick());
		
		mLogin = (ImageButton) findViewById(R.id.main_banner_right_btn);
		mLogin.setOnClickListener(new WeiLogin());
		mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
		
		String js = "{\"users\":[{\"id\":2869582162,\"idstr\":\"2869582162\",\"screen_name\":\"中国易经风水官网\",\"name\":\"中国易经风水官网\",\"province\":\"43\",\"city\":\"13\",\"location\":\"湖南 娄底\",\"description\":\"我的座右铭：创世界一流预测理论，扬中华瑰宝周易文化；将命理预测推向世界，让传统文化造福人类。\",\"url\":\"\",\"profile_image_url\":\"http://tp3.sinaimg.cn/2869582162/50/40025482867/1\",\"profile_url\":\"u/2869582162\",\"domain\":\"\",\"weihao\":\"\",\"gender\":\"m\",\"followers_count\":328992,\"friends_count\":2,\"statuses_count\":492,\"favourites_count\":0,\"created_at\":\"Sat Jul 14 15:13:14 +0800 2012\",\"following\":true,\"allow_all_act_msg\":true,\"geo_enabled\":true,\"verified\":true,\"verified_type\":5,\"remark\":\"\",\"status_id\":3595297731486970,\"allow_all_comment\":true,\"avatar_large\":\"http://tp3.sinaimg.cn/2869582162/180/40025482867/1\",\"avatar_hd\":\"\",\"verified_reason\":\"中国易经风水官网www.zgyjfs.cn官方微博\",\"follow_me\":false,\"online_status\":0,\"bi_followers_count\":1,\"lang\":\"zh-cn\",\"star\":0,\"mbtype\":0,\"mbrank\":0,\"block_word\":0},{\"id\":1781387491,\"idstr\":\"1781387491\",\"screen_name\":\"微博iPhone客户端\",\"name\":\"微博iPhone客户端\",\"province\":\"11\",\"city\":\"8\",\"location\":\"北京 海淀区\",\"description\":\"欢迎来到@微博iPhone客户端 官方微博！\n在这里：\n轻松更新浏览你关注的好友、娱乐明星、专家发布的最新微博；即时获取国内外热点新闻,...\",\"url\":\"http://news.sina.com.cn/wap/wbclient.html\",\"profile_image_url\":\"http://tp4.sinaimg.cn/1781387491/50/5661992330/0\",\"profile_url\":\"58351\",\"domain\":\"weiboiphone\",\"weihao\":\"58351\",\"gender\":\"f\",\"followers_count\":15855827,\"friends_count\":78,\"statuses_count\":849,\"favourites_count\":128,\"created_at\":\"Thu Jul 22 12:12:30 +0800 2010\",\"following\":true,\"allow_all_act_msg\":true,\"geo_enabled\":true,\"verified\":true,\"verified_type\":2,\"remark\":\"\",\"status_id\":3595279888285730,\"allow_all_comment\":true,\"avatar_large\":\"http://tp4.sinaimg.cn/1781387491/180/5661992330/0\",\"avatar_hd\":\"\",\"verified_reason\":\"新浪微博iPhone客户端客服帐号\",\"follow_me\":false,\"online_status\":0,\"bi_followers_count\":61,\"lang\":\"zh-cn\",\"star\":0,\"mbtype\":0,\"mbrank\":0,\"block_word\":0},{\"id\":1237869662,\"idstr\":\"1237869662\",\"screen_name\":\"南派三叔\",\"name\":\"南派三叔\",\"province\":\"11\",\"city\":\"13\",\"location\":\"北京 顺义区\",\"description\":\"找我请找郑先生：sheilan@qq.com 影视工作，请e林女士：linshuiyao_2005@vip.163.com\",\"url\":\"http://blog.sina.com.cn/npss\",\"profile_image_url\":\"http://tp3.sinaimg.cn/1237869662/50/40018484697/1\",\"profile_url\":\"npss\",\"domain\":\"npss\",\"weihao\":\"\",\"gender\":\"m\",\"followers_count\":6760232,\"friends_count\":506,\"statuses_count\":4732,\"favourites_count\":7,\"created_at\":\"Fri Aug 28 16:14:09 +0800 2009\",\"following\":true,\"allow_all_act_msg\":false,\"geo_enabled\":false,\"verified\":true,\"verified_type\":0,\"remark\":\"\",\"status_id\":3594148659285006,\"allow_all_comment\":true,\"avatar_large\":\"http://tp3.sinaimg.cn/1237869662/180/40018484697/1\",\"avatar_hd\":\"\",\"verified_reason\":\"《倩女幽魂2》剧情创意总监，《盗墓笔记》、《沙海》等书作者\",\"follow_me\":false,\"online_status\":0,\"bi_followers_count\":497,\"lang\":\"zh-cn\",\"star\":0,\"mbtype\":2,\"mbrank\":4,\"block_word\":0},{\"id\":2746457200,\"idstr\":\"2746457200\",\"screen_name\":\"中国易经风水\",\"name\":\"中国易经风水\",\"province\":\"43\",\"city\":\"1000\",\"location\":\"湖南\",\"description\":\"风水不是迷信、要正确好好利用。易经风水第一博。关注我会给您带来好运！有事加微信号：zgyjfs\",\"url\":\"\",\"profile_image_url\":\"http://tp1.sinaimg.cn/2746457200/50/40025482804/1\",\"cover_image\":\"http://ww1.sinaimg.cn/crop.0.0.980.300/a3b39c70jw1e11h7w67cej.jpg\",\"profile_url\":\"zgyjfs\",\"domain\":\"zgyjfs\",\"weihao\":\"\",\"gender\":\"m\",\"followers_count\":1755412,\"friends_count\":1,\"statuses_count\":2658,\"favourites_count\":1,\"created_at\":\"Mon Mar 26 08:48:22 +0800 2012\",\"following\":true,\"allow_all_act_msg\":true,\"geo_enabled\":true,\"verified\":false,\"verified_type\":-1,\"remark\":\"\",\"status_id\":3595297693730558,\"allow_all_comment\":true,\"avatar_large\":\"http://tp1.sinaimg.cn/2746457200/180/40025482804/1\",\"avatar_hd\":\"\",\"verified_reason\":\"\",\"follow_me\":false,\"online_status\":0,\"bi_followers_count\":1,\"lang\":\"zh-cn\",\"star\":0,\"mbtype\":12,\"mbrank\":3,\"block_word\":0},{\"id\":2184708610,\"idstr\":\"2184708610\",\"screen_name\":\"群星龙行-李建南\",\"name\":\"群星龙行-李建南\",\"province\":\"31\",\"city\":\"16\",\"location\":\"上海 金山区\",\"description\":\"安利营销经理\",\"url\":\"http://blog.sina.com.cn/u/2184708610\",\"profile_image_url\":\"http://tp3.sinaimg.cn/2184708610/50/5666330880/1\",\"profile_url\":\"u/2184708610\",\"domain\":\"\",\"weihao\":\"\",\"gender\":\"m\",\"followers_count\":322,\"friends_count\":209,\"statuses_count\":2550,\"favourites_count\":70,\"created_at\":\"Tue Jun 21 00:52:37 +0800 2011\",\"following\":true,\"allow_all_act_msg\":false,\"geo_enabled\":true,\"verified\":false,\"verified_type\":-1,\"remark\":\"\",\"status_id\":3595288608927960,\"allow_all_comment\":true,\"avatar_large\":\"http://tp3.sinaimg.cn/2184708610/180/5666330880/1\",\"avatar_hd\":\"\",\"verified_reason\":\"\",\"follow_me\":false,\"online_status\":0,\"bi_followers_count\":44,\"lang\":\"zh-cn\",\"star\":0,\"mbtype\":0,\"mbrank\":0,\"block_word\":0},{\"id\":3172918062,\"idstr\":\"3172918062\",\"screen_name\":\"星雲大師\",\"name\":\"星雲大師\",\"province\":\"71\",\"city\":\"2\",\"location\":\"台湾 高雄市\",\"description\":\"我把自己一生走过的路，以每十年为一个时期：成长、学习、参学、文学、历史、哲学、伦理、佛学，81岁以后便是「随缘人生」！\",\"url\":\"http://hsinyundashi.tumblr.com/\",\"profile_image_url\":\"http://tp3.sinaimg.cn/3172918062/50/5663286990/1\",\"cover_image\":\"http://ww1.sinaimg.cn/crop.0.0.980.300/bd1ee32egw1e4wtrc3t60j20r808cabu.jpg\",\"profile_url\":\"foguangyun\",\"domain\":\"foguangyun\",\"weihao\":\"\",\"gender\":\"m\",\"followers_count\":74628,\"friends_count\":27,\"statuses_count\":454,\"favourites_count\":1,\"created_at\":\"Wed Jan 30 20:34:27 +0800 2013\",\"following\":true,\"allow_all_act_msg\":false,\"geo_enabled\":false,\"verified\":true,\"verified_type\":0,\"remark\":\"\",\"status_id\":3595218156417581,\"allow_all_comment\":true,\"avatar_large\":\"http://tp3.sinaimg.cn/3172918062/180/5663286990/1\",\"avatar_hd\":\"\",\"verified_reason\":\"佛光山开山宗长星云大师官方微博\",\"follow_me\":false,\"online_status\":0,\"bi_followers_count\":19,\"lang\":\"zh-tw\",\"star\":0,\"mbtype\":12,\"mbrank\":3,\"block_word\":1},{\"id\":2482386165,\"idstr\":\"2482386165\",\"screen_name\":\"汽车微吧\",\"name\":\"汽车微吧\",\"province\":\"11\",\"city\":\"5\",\"location\":\"北京 朝阳区\",\"description\":\"汽车微吧：http://weiba.weibo.com/qicheba\",\"url\":\"http://weiba.weibo.com/qicheba\",\"profile_image_url\":\"http://tp2.sinaimg.cn/2482386165/50/5654928888/1\",\"profile_url\":\"u/2482386165\",\"domain\":\"\",\"weihao\":\"\",\"gender\":\"m\",\"followers_count\":532989,\"friends_count\":109,\"statuses_count\":880,\"favourites_count\":2,\"created_at\":\"Mon Nov 21 18:44:56 +0800 2011\",\"following\":true,\"allow_all_act_msg\":true,\"geo_enabled\":true,\"verified\":true,\"verified_type\":3,\"remark\":\"\",\"status_id\":3593384582755761,\"allow_all_comment\":true,\"avatar_large\":\"http://tp2.sinaimg.cn/2482386165/180/5654928888/1\",\"avatar_hd\":\"\",\"verified_reason\":\"汽车微吧官方微博 http://weiba.weibo.com/10077\",\"follow_me\":false,\"online_status\":0,\"bi_followers_count\":47,\"lang\":\"zh-cn\",\"star\":0,\"mbtype\":0,\"mbrank\":0,\"block_word\":0},{\"id\":1708808173,\"idstr\":\"1708808173\",\"screen_name\":\"normanzheng\",\"name\":\"normanzheng\",\"province\":\"44\",\"city\":\"3\",\"location\":\"广东 深圳\",\"description\":\"学习中\",\"url\":\"http://www.mulandesign.com/blog\",\"profile_image_url\":\"http://tp2.sinaimg.cn/1708808173/50/5630931108/1\",\"profile_url\":\"webdesign168\",\"domain\":\"webdesign168\",\"weihao\":\"\",\"gender\":\"m\",\"followers_count\":981,\"friends_count\":186,\"statuses_count\":2575,\"favourites_count\":128,\"created_at\":\"Thu Mar 11 13:30:38 +0800 2010\",\"following\":true,\"allow_all_act_msg\":false,\"geo_enabled\":true,\"verified\":false,\"verified_type\":-1,\"remark\":\"\",\"status_id\":3582479334159842,\"allow_all_comment\":true,\"avatar_large\":\"http://tp2.sinaimg.cn/1708808173/180/5630931108/1\",\"avatar_hd\":\"\",\"verified_reason\":\"\",\"follow_me\":false,\"online_status\":0,\"bi_followers_count\":129,\"lang\":\"zh-cn\",\"star\":0,\"mbtype\":0,\"mbrank\":0,\"block_word\":0},{\"id\":1657776532,\"idstr\":\"1657776532\",\"screen_name\":\"张欣\",\"name\":\"张欣\",\"province\":\"11\",\"city\":\"1000\",\"location\":\"北京\",\"description\":\"SOHO中国CEO\",\"url\":\"http://blog.sina.com.cn/zhangxin\",\"profile_image_url\":\"http://tp1.sinaimg.cn/1657776532/50/5664258967/0\",\"cover_image\":\"http://ww1.sinaimg.cn/crop.0.127.980.300/62cfa994jw1dy21ctk33vj.jpg\",\"profile_url\":\"sohozhangxin\",\"domain\":\"sohozhangxin\",\"weihao\":\"\",\"gender\":\"f\",\"followers_count\":6116771,\"friends_count\":130,\"statuses_count\":2987,\"favourites_count\":4,\"created_at\":\"Sun Nov 01 15:01:39 +0800 2009\",\"following\":true,\"allow_all_act_msg\":false,\"geo_enabled\":false,\"verified\":true,\"verified_type\":0,\"remark\":\"\",\"status_id\":3594910240654281,\"allow_all_comment\":true,\"avatar_large\":\"http://tp1.sinaimg.cn/1657776532/180/5664258967/0\",\"avatar_hd\":\"\",\"verified_reason\":\"SOHO中国CEO\",\"follow_me\":false,\"online_status\":0,\"bi_followers_count\":100,\"lang\":\"zh-cn\",\"star\":0,\"mbtype\":2,\"mbrank\":4,\"block_word\":0},{\"id\":3152772463,\"idstr\":\"3152772463\",\"screen_name\":\"誓言新语\",\"name\":\"誓言新语\",\"province\":\"100\",\"city\":\"1000\",\"location\":\"其他\",\"description\":\"戳中心坎的话，我来帮你说～\",\"url\":\"\",\"profile_image_url\":\"http://tp4.sinaimg.cn/3152772463/50/40009243028/1\",\"profile_url\":\"532921233\",\"domain\":\"\",\"weihao\":\"532921233\",\"gender\":\"m\",\"followers_count\":7257,\"friends_count\":1757,\"statuses_count\":7573,\"favourites_count\":0,\"created_at\":\"Fri Nov 30 15:21:21 +0800 2012\",\"following\":true,\"allow_all_act_msg\":false,\"geo_enabled\":true,\"verified\":false,\"verified_type\":-1,\"remark\":\"\",\"status_id\":3595301044768336,\"allow_all_comment\":true,\"avatar_large\":\"http://tp4.sinaimg.cn/3152772463/180/40009243028/1\",\"avatar_hd\":\"\",\"verified_reason\":\"\",\"follow_me\":false,\"online_status\":0,\"bi_followers_count\":1461,\"lang\":\"zh-cn\",\"star\":0,\"mbtype\":0,\"mbrank\":0,\"block_word\":0}],\"next_cursor\":11,\"previous_cursor\":1,\"total_number\":212}";
		creatList(js);
		
	}

	private class BackClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			ContactActivity.this.finish();
		}
	}// end inner class
	
	private class WeiLogin implements OnClickListener {
		@Override
		public void onClick(View v) {
			 mWeibo.authorize(ContactActivity.this, new AuthDialogListener());
		}
	}// end inner class
	
	class AuthDialogListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
        	 String token = values.getString("access_token");
             String expires_in = values.getString("expires_in");
             accessToken = new Oauth2AccessToken(token, expires_in);
             if (accessToken.isSessionValid()) {
            	 AccountAPI user = new AccountAPI(accessToken);
            	 final FriendshipsAPI f = new FriendshipsAPI(accessToken);
            	 user.getUid(new RequestListener(){
            		
         			@Override
         			public void onComplete(String arg0) {
         				// TODO Auto-generated method stub
         				String a= arg0;
         				JSONObject jsonObj;
						try {
							jsonObj = new JSONObject(a);
							long uid = jsonObj.getLong("uid");
							
							
							f.friends(uid, 10, 1, true,new RequestListener(){

		            			@Override
		            			public void onComplete(String arg0) {
		            				// TODO Auto-generated method stub
		            				String a= arg0;
		            				
		            				creatList(a);
		            			}

		            			@Override
		            			public void onError(WeiboException arg0) {
		            				// TODO Auto-generated method stub
		            				WeiboException a= arg0;
		            				System.out.print(a);
		            				
		            			}

		            			@Override
		            			public void onIOException(IOException arg0) {
		            				// TODO Auto-generated method stub
		            				IOException a= arg0;
		            				System.out.print(a);
		            				
		            			}
		            			
		            		});
							
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
         				
         				
         			}

         			@Override
         			public void onError(WeiboException arg0) {
         				// TODO Auto-generated method stub
         				WeiboException a= arg0;
         				System.out.print(a);
         				
         			}

         			@Override
         			public void onIOException(IOException arg0) {
         				// TODO Auto-generated method stub
         				IOException a= arg0;
         				System.out.print(a);
         				
         			}
         			
         		});
            /*	*/ 
             }
        }

        @Override
        public void onError(WeiboDialogError e) {
            Toast.makeText(getApplicationContext(),
                    "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "Auth cancel",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getApplicationContext(),
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }
	
	private void creatList(String s){
			
			new DownloadImageTask().execute(s) ; 
			
			
			
			
		
    	
    }
	/*class   ListData extends Thread {
		
		public void run() {
			
			
			ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			for (int i = 0 ; i<weiboInfo.length();i++){
				HashMap<String, Object> row  = new HashMap<String, Object>();
				JSONObject jsonOne;
				try {
					jsonOne = (JSONObject)weiboInfo.get(i);
				
					String url = jsonOne.getString("profile_image_url");
					String name = jsonOne.getString("name");
					Bitmap bt = getBitmap(url);
					row.put("img", bt)
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//加载图片
				/*Bitmap bitmap = null;
		        Drawable d = null;          
		        try {                   
		            bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
		            bitmap.setDensity(Bitmap.DENSITY_NONE);
		            d = new BitmapDrawable(bitmap);
		        } catch (MalformedURLException e) {
		          e.printStackTrace();
		        } catch (IOException e) {
		          e.printStackTrace();
		        }
		        row.put("img", d);
			    row.put("wb_name", name);
			    data.add(row);
			}
		}
		
		public Bitmap getBitmap(String url){
			Bitmap bt = null;
				if (url != null && !url.equals("")) {
					InputStream is = null;
					try {
						URL url1 = new URL(url);
						HttpURLConnection conn = (HttpURLConnection) url1
								.openConnection();
		
						conn.setDoInput(true);
						conn.connect();
						is = conn.getInputStream();
						bt = BitmapFactory.decodeStream(is);
						is.close();
						conn.disconnect();
		
					} catch (Exception e) {
						e.printStackTrace();
					} 
		
				}
				return bt;
			}

	}
	*/
	private ArrayList<HashMap<String, Object>> loadImageFromNetwork(String s)    
	{    
	    Drawable drawable = null;   
	    ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	   
			 try {    
			        // 可以在这里通过文件名来判断，是否本地有此图片  
				    JSONObject jsonObj = new JSONObject(s);
					weiboInfo =jsonObj.getJSONArray("users");
					for(int i = 0 ; i<weiboInfo.length();i++){
						HashMap<String, Object> row  = new HashMap<String, Object>();
						JSONObject one = (JSONObject)weiboInfo.get(i);
						String imageUrl = one.getString("profile_image_url");
						drawable = Drawable.createFromStream(    
			                new URL(imageUrl).openStream(), "image.jpg");  
						row.put("icoData", drawable);
						row.put("wbName", one.getString("name"));
						data.add(row);
					}
			    } catch (IOException e) {    
			        Log.d("test", e.getMessage());    
			    } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}    
			    if (drawable == null) {    
			        Log.d("test", "null drawable");    
			    } else {    
			        Log.d("test", "not null drawable");    
			    } 
	        
	    return data ;    
	} 
	
	private class DownloadImageTask extends AsyncTask<String, Void, ArrayList<HashMap<String, Object>>>   
	{  
	          
	        protected ArrayList<HashMap<String, Object>> doInBackground(String... urls) {  
	            return loadImageFromNetwork(urls[0]);  
	        }  
	  
	        protected void onPostExecute(ArrayList<HashMap<String, Object>> data) {  
	        	SimpleAdapter adapter = new SimpleAdapter(ContactActivity.this, data,R.layout.weibo_list_item,
			              new String[] {"icoData","wbName"},
			              new int[] {R.id.ico,R.id.wb_name});
	        	ListView userList= (ListView) findViewById(R.id.userInfo);
				userList.setAdapter(adapter); 
				adapter.setViewBinder(new ViewBinder() {
					
					@Override
					public boolean setViewValue(View view, Object data,
							String textRepresentation) {
						// TODO Auto-generated method stub
						if(view instanceof ImageView && data instanceof Drawable){
		                     ImageView iv=(ImageView)view;
		                     iv.setImageDrawable((Drawable)data);
		                     return true;
		                 }
						return false;
					}
				});
	        }  
	}  
	

}// end class
