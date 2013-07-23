package com.weibo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

public class PropertiesService {
	private Properties prop;
	private String fileName = "/sdcard/airAd.dat";
	private Context context;

	public PropertiesService(Context context) {
		this.context = context;
		prop = loadConfig();
	}
	public Properties getProperties(){
		return prop;
	}
	public void putString(String key,String value){
		prop.put(key, value);
	}

	public Properties loadConfig() {
		Properties properties = new Properties();
		try {
			 //InputStream is =context.getAssets().open(fileName); 
			File fil = new File(fileName);
			if (!fil.exists())
				fil.createNewFile();
			 FileInputStream is = new FileInputStream(fileName); 
			properties.load(is);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return properties;
	}

	// 保存配置文件
	public boolean saveConfig() {
		try {
			FileOutputStream s = new FileOutputStream(fileName,false);
			prop.store(s, "");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
}
