package com.airAd.yaqinghui.core;

import java.net.MalformedURLException;

import net.sf.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.business.api.BasicAPI;
import com.airAd.yaqinghui.common.Config;
import com.caucho.hessian.client.HessianProxyFactory;

public class HessianClient {
    public static BasicAPI create() {
        String url = Config.REMOTE;
        HessianProxyFactory factory = new HessianProxyFactory();
        try {
            // factory.setDebug(true);
            factory.setReadTimeout(3000);
            factory.setHessian2Reply(false);
            factory.setChunkedPost(false);
            BasicAPI basic = factory.create(BasicAPI.class, url);
            return basic;
        } catch (MalformedURLException e) {
            String s=e.getMessage();
        	MyApplication.push(s);
            e.printStackTrace();
            return null;
        }
    }

//    public static void main(String[] args) {
//        BasicAPI api = create();
//        JSONObject json = api.SelectAllCepActive("00000001");
//        // json=api.SelectTheOneCepActive(cepid)
//    }
}
