/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;
import java.util.ArrayList;
import java.util.List;

import com.airAd.yaqinghui.business.model.NoficationMessage;
/**
 * MessageService.java
 * 
 * @author liyuhang
 */
public class NotificationMessageService extends BaseService
{
	public List<NoficationMessage> getNoficationMessages()
	{
		List<NoficationMessage> ret= new ArrayList<NoficationMessage>();
		NoficationMessage item1= new NoficationMessage();
		item1.setTitle("活动真的很好，希望可以多举办");
		item1.setReadFlag(0);
		item1.setAddTimel(System.currentTimeMillis());
		item1.setContent("极光推送平台，使得开发者可以即时地向其应用程序的用户推送通知或者消息，与用户保持互动，从而有效地提高留存率，提升用户体验");
		NoficationMessage item2= new NoficationMessage();
		item2.setTitle("活动真的很好，希望可以多举办");
		item2.setReadFlag(1);
		item2.setAddTimel(System.currentTimeMillis());
		item2.setContent("帝国兴衰，皇国命运在此一战，诸君当徐愈加奋勉!帝国兴衰，皇国命运在此一战，诸君当徐愈加奋勉!帝国兴衰，皇国命运在此一战，诸君当徐愈加奋勉!帝国兴衰，皇国命运在此一战，诸君当徐愈加奋勉!");
		NoficationMessage item3= new NoficationMessage();
		item3.setTitle("活动真的很好，希望可以多举办");
		item3.setReadFlag(1);
		item3.setAddTimel(System.currentTimeMillis());
		item3.setContent("帝国兴衰，皇国命运在此一战，诸君当徐愈加奋勉!帝国兴衰，皇国命运在此一战，诸君当徐愈加奋勉!帝国兴衰，皇国命运在此一战，诸君当徐愈加奋勉!帝国兴衰，皇国命运在此一战，诸君当徐愈加奋勉!");
		ret.add(item1);
		ret.add(item2);
		ret.add(item3);
		ret.add(item1);
		ret.add(item2);
		ret.add(item3);
		ret.add(item1);
		ret.add(item2);
		ret.add(item3);
		return ret;
	}
	public List<NoficationMessage> getCepEventHistoryMessages(int type)
	{
		return getNoficationMessages();
		//		return null;
	}
	public List<NoficationMessage> getBadgesHistoryMessages()
	{
		return null;
	}
}
