package com.weibo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

public class PropertiesService {
	private Properties prop;
	private String fileName = "weibo.properties";
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
			 InputStream is =context.getAssets().open(fileName); 
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
			File fil = new File(fileName);
			if (!fil.exists())
				fil.createNewFile();
			FileOutputStream s = new FileOutputStream(fil);
			prop.store(s, "");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
}
