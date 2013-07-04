package com.airAd.yaqinghui.business;

import java.util.List;

import net.sf.json.JSONObject;

import com.airAd.yaqinghui.business.api.BasicAPI;
import com.airAd.yaqinghui.business.model.Cep;
import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.core.HessianClient;

public class CepService {
    public List<Cep> getCeps(String userId) {
        BasicAPI api = HessianClient.create();
        try {
            JSONObject jsonObj = api.SelectAllCepActive(userId, User.getLan());
            return Cep.instanceList(jsonObj);
        }
        catch (Exception e) {
            return null;
        }
    }

    public Cep getCep(String userId, String cepId) {
        BasicAPI api = HessianClient.create();
        try {
            JSONObject jsonObj = api.SelectTheOneCepActive(cepId, userId, User.getLan());
            return Cep.instance(jsonObj);
        }
        catch (Exception e) {
            return null;
        }
    }
}
