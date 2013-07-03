package com.airAd.yaqinghui.business;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.airAd.yaqinghui.business.model.CepItem;

public class CepService {
    public ArrayList<CepItem> getCepList(JSONObject jsonObj) {
        ArrayList<CepItem> retList = new ArrayList<CepItem>();
        try {
            JSONArray cepArray = jsonObj.getJSONArray("AllCepInfo");
            for (int i = 0; i < cepArray.size(); i++) {
                CepItem item = new CepItem();
                JSONObject obj = cepArray.getJSONObject(i);
                item.setCepId(obj.getString("cepid"));
                item.setTitle(obj.getString("ceptitle"));
                item.setTips(obj.getString("cepcontent"));
                item.setPicUrl(obj.getString("ceppicture"));
                retList.add(item);
            }// end for i
        } catch (JSONException e) {
            e.printStackTrace();
            retList = null;
            return retList;
        }
        return retList;
    }

}
