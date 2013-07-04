package com.airAd.yaqinghui.business;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.business.api.BasicAPI;
import com.airAd.yaqinghui.business.api.vo.param.CepEventCheckinParam;
import com.airAd.yaqinghui.business.api.vo.param.CepEventScoreParam;
import com.airAd.yaqinghui.business.api.vo.response.CepEventCheckinResponse;
import com.airAd.yaqinghui.business.api.vo.response.CepEventReservationResponse;
import com.airAd.yaqinghui.business.api.vo.response.CepEventScoreResponse;
import com.airAd.yaqinghui.business.model.Cep;
import com.airAd.yaqinghui.business.model.CepEvent;
import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.core.HessianClient;

public class CepService extends BaseService {
    public List<Cep> getCeps() {
        BasicAPI api = HessianClient.create();
        try {
            JSONObject jsonObj = api.SelectAllCepActive(MyApplication.getCurrentUser().getId(), User.getLan());
            return Cep.instanceList(jsonObj);
        }
        catch (Exception e) {
            return null;
        }
    }

    public Cep getCep(String cepId) {
        BasicAPI api = HessianClient.create();
        try {
            JSONObject jsonObj =
                    api.SelectTheOneCepActive(cepId, MyApplication.getCurrentUser().getId(), User.getLan());
            return Cep.instance(jsonObj);
        }
        catch (Exception e) {
            return null;
        }
    }

    // 预约
    public CepEventReservationResponse doReservationCepEvent(String eventId) {
        return null;
    }

    // 签到
    public CepEventCheckinResponse doCheckinCepEvent(CepEventCheckinParam param) {
        return null;
    }

    // 打分
    public CepEventScoreResponse doScoreCepEvent(CepEventScoreParam param) {
        return null;
    }

    // 直接对数据库字段进行修改，与业务无关
    public Map<String, Object> doUpdateCepEvent(CepEvent cepEvent) {
        return null;
    }
}
