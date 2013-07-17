package com.airAd.yaqinghui.business;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.business.api.BasicAPI;
import com.airAd.yaqinghui.business.api.vo.param.CepEventCheckinParam;
import com.airAd.yaqinghui.business.api.vo.param.CepEventScoreParam;
import com.airAd.yaqinghui.business.api.vo.response.CepEventCheckinResponse;
import com.airAd.yaqinghui.business.api.vo.response.CepEventReservationResponse;
import com.airAd.yaqinghui.business.api.vo.response.CepEventScoreResponse;
import com.airAd.yaqinghui.business.model.Badge;
import com.airAd.yaqinghui.business.model.Cep;
import com.airAd.yaqinghui.business.model.CepEvent;
import com.airAd.yaqinghui.business.model.NotificationMessage;
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
			Log.e("htestGetCep", e.getMessage());
			return null;
		}
	}
	// 预约
	public long doReservationCepEvent(
			final String userId, final Cep cep, final CepEvent event) {
		long ret= -1;
		SQLiteDatabase db = MyApplication.getCurrentWirteDB();
		Cursor cur = db
				.rawQuery(
						"select count(1) from schedule where user_id = ? and cep_id = ? and ref_id = ?",
						new String[]{userId, cep.getId(), event.getId()});
		int count = 0;
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			count = cur.getInt(0);
			//
			cur.moveToNext();
		}
		cur.close();
		// 如果个人行程已经关注了该场次则不重复插入
		if (count == 0) {

			ContentValues cValue = new ContentValues();
			//
			cValue.put("user_id", userId);
			cValue.put("cep_id", cep.getId());
			cValue.put("ref_id", event.getId());
			cValue.put("item_type", ScheduleItem.TYPE_CEP_EVENT);
			cValue.put("place", event.getPlace());
			cValue.put("title", cep.getTitle());
			cValue.put("icon_type", cep.getIconType());
			cValue.put("start_time", event.getStartTimel());
			cValue.put("add_time", new Date().getTime());
			cValue.put("time_str", event.getEventTimeRangeDescription());
			cValue.put("day", event.getStartDayOfMonth());
			//
			ret= db.insert("schedule", null, cValue);
		}
		return ret;
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
			// 成功则插到签到历史
			if (Constants.FLAG_SUCC.equals(res.getFlag())) {
				param.setEventId(res.getEventId());
				SQLiteDatabase db = MyApplication.getCurrentWirteDB();
				ContentValues cValue = new ContentValues();
				//
				cValue.put("user_id", param.getUserId());
				cValue.put("title", res.getMsg());
				cValue.put("content", res.getMsg());
				cValue.put("message_type",
						NotificationMessage.TYPE_CEPEVENT_CHECKIN_HIS);
				cValue.put("read_flag", NotificationMessage.READ);
				cValue.put("add_time", new Date().getTime());
				// 查出对应的cepId
				Cep cep = new CepService().getCep(param.getUserId(),
						param.getCepId());
				CepEvent cepEvent = null;
				List<CepEvent> events = cep.getCepEvents();
				for (CepEvent event : events) {
					if (param.getEventId().equals(event.getId())) {
						cepEvent = event;
						break;
					}
				}
				//
				cValue.put("cep_id", cep.getId());
				cValue.put("event_id", cepEvent.getId());
				cValue.put("cep_title", cep.getTitle());
				cValue.put("cep_place", cepEvent.getPlace());
				cValue.put("cep_start_time", cepEvent.getStartTimel());
				//
				long messagesId = db.insert("messages", null, cValue);
			}
			//
			return res;
		} catch (Exception e) {
			Log.e("htest", e.getMessage());
			return res;
		}
	}
	// 打分
	public CepEventScoreResponse doScoreCepEvent(CepEventScoreParam param) {
		BasicAPI api = HessianClient.create();
		CepEventScoreResponse res = new CepEventScoreResponse();
		try {
			JSONObject jsonObj = api.CepActiveComment(param.getCepId(),
					param.getEventId(), param.getUserId(), param.getScore(),
					User.getLan());
			Log.d("htestDoScoreCepEvent", jsonObj.toString());
			res = CepEventScoreResponse.instance(jsonObj);
			//
			SQLiteDatabase db = MyApplication.getCurrentWirteDB();
			if (Constants.FLAG_SUCC.equals(res.getFlag())) {
				// 评分成功则添加一枚徽章
				ContentValues cv = new ContentValues();
				cv.put("add_time", new Date().getTime());
				cv.put("user_id", param.getUserId());
				cv.put("cep_id", param.getCepId());
				cv.put("event_id", param.getEventId());
				cv.put("badge_id", Badge.getBadgeId(param.getCepId()));
				long badgeId = db.insert("badges", null, cv);
				// 如果评分成功则，插到评分历史

				ContentValues cValue = new ContentValues();
				//
				cValue.put("user_id", param.getUserId());
				cValue.put("title", res.getMsg());
				cValue.put("content", res.getMsg());
				cValue.put("message_type",
						NotificationMessage.TYPE_CEPEVENT_SCORE_HIS);
				cValue.put("read_flag", NotificationMessage.READ);
				cValue.put("add_time", new Date().getTime());
				// 查出对应的cepId
				Cep cep = new CepService().getCep(param.getUserId(),
						param.getCepId());
				CepEvent cepEvent = null;
				List<CepEvent> events = cep.getCepEvents();
				for (CepEvent event : events) {
					if (param.getEventId().equals(event.getId())) {
						cepEvent = event;
						break;
					}
				}
				//
				cValue.put("cep_id", cep.getId());
				cValue.put("event_id", cepEvent.getId());
				cValue.put("cep_title", cep.getTitle());
				cValue.put("cep_place", cepEvent.getPlace());
				cValue.put("cep_start_time", cepEvent.getStartTimel());
				//
				long messagesId = db.insert("messages", null, cValue);
			}
			return res;
		} catch (Exception e) {
			return res;
		}
	}
}
