package com.airAd.yaqinghui.net;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.airAd.yaqinghui.data.model.CommentMark;

public class CommentService {
    public CommentMark getCommentMark(JSONObject jsonObj) {
        CommentMark item = new CommentMark();
        try {
            JSONObject obj = jsonObj.getJSONObject("CepActiveComment");
            item.setMark(obj.getInt("confirmmark"));
            item.setText(obj.getString("confirmtext"));
        } catch (JSONException e) {
            e.printStackTrace();
            item = null;
            return item;
        }
        return item;
    }

}
