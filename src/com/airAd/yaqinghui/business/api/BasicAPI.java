package com.airAd.yaqinghui.business.api;

import net.sf.json.JSONObject;

/**
 * http://www.cnblogs.com/over140/archive/2010/07/30/1788563.html
 * 
 * @author xuanrui company
 * @parameter Map parameters from client
 * @return Map results to client 该接口定义客户端能够访问的方法
 */
public interface BasicAPI {
    /**
     * 用户登陆校验 输入参数测试用例：json=basic.UserLogin("00000001", "1236","CHI");
     * 
     * @param username
     * @param password
     * @param ManyLangMark
     * @return
     */
    public JSONObject UserLogin(String username, String password, String ManyLangMark);

    /**
     * 修改密码 输入参数测试用例：json=basic.ChangePassword("00000001","1236","1238","1238","ENG");
     * 
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @param confimPassword
     * @param ManyLangMark
     * @return
     */
    public JSONObject ChangePassword(String userId, String oldPassword, String newPassword, String confimPassword,
            String ManyLangMark);
    public JSONObject SelectAllCepActive(String userid,String ManyLangMark);//获得所有CEP活动
    public JSONObject SelectTheOneCepActive(String cepid,String userid,String ManyLangMark);//获取一个CEP活动详情
    public JSONObject PrecontractSignUpCepActive(String cepid,String eventid,String userid,String ManyLangMark);//预约CEP活动
    public JSONObject PrecontractCancelCepActive(String cepid,String eventid,String userid,String ManyLangMark);//取消预约CEP活动
    public JSONObject SignInCepActive(String twobarcode,String userid,String log,String lat,String ManyLangMark);//二维码签到
    
	public JSONObject SelectConfirmCepActive (String userid,String ManyLangMark);
	public JSONObject CepActiveComment (String cepid,String eventid,String userid,String commentcontent,String ManyLangMark);
	
	//huss add 20130627
	public JSONObject GetWeather(String ManyLangMark);
	public JSONObject GetGameInfo(String userId,String ManyLangMark);
	public JSONObject GetGameDetailInfo(String GameBigType,String GameDate,String ManyLangMark);
	public JSONObject GetGameDetailInfoPlace(String GamePlaceGame,String GameDate,String ManyLangMark);
	public JSONObject getLocation(String ManyLangMark);
	public JSONObject getLocationDetail(String id,String ManyLangMark);
}
