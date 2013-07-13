package com.airAd.yaqinghui.business;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.business.api.BasicAPI;
import com.airAd.yaqinghui.business.api.vo.param.CepEventCheckinParam;
import com.airAd.yaqinghui.business.api.vo.param.CepEventScoreParam;
import com.airAd.yaqinghui.business.api.vo.response.CepEventCheckinResponse;
import com.airAd.yaqinghui.business.api.vo.response.CepEventReservationResponse;
import com.airAd.yaqinghui.business.api.vo.response.CepEventScoreResponse;
import com.airAd.yaqinghui.business.model.Cep;
import com.airAd.yaqinghui.business.model.CepEvent;
import com.airAd.yaqinghui.business.model.ScheduleItem;
import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.common.Constants;
import com.airAd.yaqinghui.core.HessianClient;

public class CepService extends BaseService {
	public List<Cep> getCeps(String userId) {
		BasicAPI api = HessianClient.create();
		try {
			JSONObject jsonObj = api.SelectAllCepActive(userId, User.getLan());
			Log.d("htestGetCeps", jsonObj.toString());
			return Cep.instanceList(jsonObj);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public Cep getCep(String userId, String cepId) {
		BasicAPI api = HessianClient.create();
		try {
			JSONObject jsonObj = api.SelectTheOneCepActive(cepId, userId,
					User.getLan());
			Log.d("htestGetCep", jsonObj.toString());
			return Cep.instance(jsonObj);
		} catch (Exception e) {
			return null;
		}
	}

	// 预约
	public CepEventReservationResponse doReservationCepEvent(
			final String userId, final Cep cep, final CepEvent event) {
		BasicAPI api = HessianClient.create();
		CepEventReservationResponse res = new CepEventReservationResponse();
		try {
			JSONObject jsonObj = api.PrecontractSignUpCepActive(cep.getId(),
					event.getId(), userId, User.getLan());
			Log.d("htestDoReservationCepEvent", jsonObj.toString());
			res = CepEventReservationResponse.instanceSignupObj(jsonObj);
			// 如果预约成功则插入数据库个人行程表
			if (Constants.FLAG_SUCC.equals(res)) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						SQLiteDatabase db = MyApplication.getCurrentWirteDB();
						ContentValues cValue = new ContentValues();
						//
						cValue.put("user_id", userId);
						cValue.put("cep_id", cep.getId());
						cValue.put("ref_id", event.getId());
						cValue.put("item_type", ScheduleItem.TYPE_CEP_EVENT);
						cValue.put("title", cep.getTitle());
						cValue.put("icon_type", cep.getIconType());
						cValue.put("start_time", event.getStartTimel());
						cValue.put("add_time", new Date().getTime());
						cValue.put("time_str",
								event.getEventTimeRangeDescription());
						cValue.put("day", event.getStartDayOfMonth());
						//
						db.insert("schedule", null, cValue);
					}
				});
				thread.start();
			}
			return res;
		} catch (Exception e) {
			return res;
		}
	}

	public CepEventReservationResponse doCancelReservationCepEvent(
			final String userId, final String cepId, final String eventId) {
		BasicAPI api = HessianClient.create();
		CepEventReservationResponse res = new CepEventReservationResponse();
		try {
			JSONObject jsonObj = api.PrecontractCancelCepActive(cepId, eventId,
					userId, User.getLan());
			Log.d("htestDoCancelReservationCepEvent", jsonObj.toString());
			res = CepEventReservationResponse.instanceCancelObj(jsonObj);
			// 如果取消成功则删除数据库中相应记录
			if (Constants.FLAG_SUCC.equals(res)) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						SQLiteDatabase db = MyApplication.getCurrentWirteDB();
						String[] params = {eventId, cepId};
						db.delete(
								"schedule",
								"where item_type = 2 and ref_id = ? and cep_id = ?",
								params);
					}
				});
				thread.start();
			}
			return res;
		} catch (Exception e) {
			return res;
		}
	}
	// 签到
	public CepEventCheckinResponse doCheckinCepEvent(CepEventCheckinParam param) {
		BasicAPI api = HessianClient.create();
		CepEventCheckinResponse res = new CepEventCheckinResponse();
		try {
			JSONObject jsonObj = api.SignInCepActive(param.getQrcode(),
					param.getUserId(), param.getLng(), param.getLat(),
					User.getLan());
			Log.d("htestDoCheckinCepEvent", jsonObj.toString());
			res = CepEventCheckinResponse.instance(jsonObj);
			return res;
		} catch (Exception e) {
			return res;
		}
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
