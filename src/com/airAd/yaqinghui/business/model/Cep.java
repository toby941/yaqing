/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business.model;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.airAd.yaqinghui.R;
/**
 * Cep.java
 * 
 * @author liyuhang
 */
public class Cep
{
	private String id;
	private String title;
	private String content;
	private String iconType;
	private String pic;
	private String score;
	private List<String> pics;
	private List<CepEvent> cepEvents;
	//
	public String getId()
	{
		return id;
	}
	public String getIconType()
	{
		return iconType;
	}
	public void setIconType(String iconType)
	{
		this.iconType= iconType;
	}
	public void setId(String id)
	{
		this.id= id;
	}
	public List<String> getPics()
	{
		return pics;
	}
	public void setPics(List<String> pics)
	{
		this.pics= pics;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title= title;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content= content;
	}
	public String getPic()
	{
		return pic;
	}
	public void setPic(String pic)
	{
		this.pic= pic;
	}
	public String getScore()
	{
		return score;
	}
	public void setScore(String score)
	{
		this.score= score;
	}
	public List<CepEvent> getCepEvents()
	{
		return cepEvents;
	}
	public void setCepEvents(List<CepEvent> cepEvents)
	{
		this.cepEvents= cepEvents;
	}
	public static Cep instance(JSONObject jsonObj) throws JSONException
	{
		Cep cep= new Cep();
		JSONObject obj= jsonObj.getJSONObject("TheOneCepInfo");
		cep.setId(obj.optString("cepid"));
		cep.setTitle(obj.getString("ceptitle"));
		cep.setContent(obj.getString("cepcontent"));
		cep.setScore(obj.getString("score"));
		cep.setIconType(getIconType(obj.getString("cepid")));
		ArrayList<String> pics= new ArrayList<String>();
		String pic= new String();
		// cepevent
		JSONArray eventsArr= obj.optJSONArray("event");
		List<CepEvent> events= new ArrayList<CepEvent>();
		if (eventsArr != null)
		{
			for (int i= 0; i < eventsArr.size(); i++)
			{
				JSONObject eventObj= eventsArr.getJSONObject(i);
				CepEvent event= new CepEvent();
				event.setCepId(cep.getId());
				event.setId(eventObj.getString("eventid"));
				event.setStartTime(eventObj.optString("begintime"));
				event.setEndTime(eventObj.optString("endtime"));
				event.setPlace(eventObj.optString("cepplace"));
				event.setMaxNum(Integer.parseInt(eventObj.optString("joinnum")));
				event.setAttendNum(Integer.parseInt(eventObj.optString("signupnum")));
				event.setCepEventType(obj.optString("type"));
				event.setFlag(eventObj.optString("flag"));
				event.setTabId(i + 1);
				// if (new Date().getTime() < event.getStartTimel())
				// {
					events.add(event);
				// }
				//
				pic= eventObj.optString("ceppictureone");
			}
			cep.setCepEvents(events);
		}
		if (StringUtils.isNotBlank(pic))
		{
			pics.add(pic);
			cep.setPics(pics);
		}
		//
		return cep;
	}
	public static List<Cep> instanceList(JSONObject jsonObj) throws JSONException
	{
		List<Cep> ceps= new ArrayList<Cep>();
		JSONArray cepArray= jsonObj.getJSONArray("AllCepInfo");
		for (int i= 0; i < cepArray.size(); i++)
		{
			Cep cep= new Cep();
			JSONObject obj= cepArray.getJSONObject(i);
			cep.setId(obj.getString("cepid"));
			cep.setTitle(obj.optString("ceptitle"));
			cep.setContent(obj.optString("cepcontent"));
			cep.setPic(obj.optString("ceppicture"));
			cep.setScore(obj.optString("score"));
			cep.setIconType(getIconType(obj.getString("cepid")));
			cep.setScore(obj.getString("score"));
			ceps.add(cep);
		}// end loop
		return ceps;
	}
	public static String getIconType(String id)
	{
		if ("1".equals(id))
		{
			return "cep_type_blue";
		}
		else if ("2".equals(id) || "3".equals("id"))
		{
			return "cep_type_red";
		}
		else if ("6".equals(id) || "5".equals(id))
		{
			return "cep_type_green";
		}
		return "cep_type_red";
	}
	/**
	 * 1 zh_CN 青年的节日 2 zh_CN 亚洲文化村 3 zh_CN 国际组织展 6 zh_CN 南京历史文化风貌观光 5 zh_CN
	 * 生态环保农业
	 * 
	 * @param id
	 * @return
	 */
	//
	public static int getCepBigPicRes(String ids)
	{
		int id= Integer.parseInt(ids);
		switch (id)
		{
			case 1 :
				return R.drawable.big_yf;
			case 2 :
				return R.drawable.big_ac;
			case 3 :
				return R.drawable.big_is;
			case 5 :
				return R.drawable.big_tn2;
			case 6 :
				return R.drawable.big_tn1;
			default :
				return R.drawable.big_yf;
		}
	}
	public static int getCepSmallPicRes(String ids)
	{
		int id= Integer.parseInt(ids);
		switch (id)
		{
			case 1 :
				return R.drawable.small_yf;
			case 2 :
				return R.drawable.small_ac;
			case 3 :
				return R.drawable.small_is;
			case 5 :
				return R.drawable.small_tn2;
			case 6 :
				return R.drawable.small_tn1;
			default :
				return R.drawable.small_yf;
		}
	}
	public static int getIdFromQrcode(String qrcode)
	{
		if (qrcode.contains("nanjingyaqinghuicepqingnianjieri6853273921"))// 青年的节日
		{
			return 1;
		}
		else if (qrcode.contains("nanjingyaqinghuicepyazhouwenhuacun275369153"))
		{
			return 2;
		}
		else if (qrcode.contains("nanjingyaqinghuicepguojizhuzhizhuan734216983"))
		{
			return 3;
		}
		else if (qrcode.contains("nanjingyaqinghuiceplishiwenhuafenmao294521930"))
		{
			return 6;
		}
		else if (qrcode.contains("nanjingyaqinghuicepshengtaihuanbaolunye20167389215"))
		{
			return 5;
		}
		return 1;
	}
}
