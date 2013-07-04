/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.airAd.yaqinghui.business.model.ScheduleItem;

/**
 * ScheduleService.java
 * 
 * @author liyuhang
 */
public class ScheduleService extends BaseService {
    /**
     * @param userId
     * @param Date 传12 就是 12号
     * @return
     */
    public List<ScheduleItem> getScheduleItemsByDate(int Date) {
        return null;
    }

    /**
     * @param userId
     * @return {13 => 1, 14 => 2}
     */
    public Map<Integer, Integer> getCalendlarScheduleData() {
        return null;
    }

    // 删除个人行程
    public Map<String, Object> doDelScheduleItem(int scheduleItemId) {
        Map<String, Object> errMap = new HashMap<String, Object>();
        return errMap;
    }

    // 添加个人行程
    public Map<String, Object> doAddScheduleItem(ScheduleItem item) {
        Map<String, Object> errMap = new HashMap<String, Object>();
        return errMap;
    }
}
