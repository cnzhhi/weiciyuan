package me.ivps.android.myweibo.dao.show;

import java.util.HashMap;
import java.util.Map;

import me.ivps.android.myweibo.bean.MessageBean;
import me.ivps.android.myweibo.dao.URLHelper;
import me.ivps.android.myweibo.support.debug.AppLogger;
import me.ivps.android.myweibo.support.error.WeiboException;
import me.ivps.android.myweibo.support.http.HttpMethod;
import me.ivps.android.myweibo.support.http.HttpUtility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * User: Jiang Qi Date: 12-8-7
 */
public class ShowStatusDao {
    
    private String access_token;
    private String id;
    
    public ShowStatusDao(String access_token, String id) {
        
        this.access_token = access_token;
        this.id = id;
    }
    
    public MessageBean getMsg() throws WeiboException {
        String url = URLHelper.STATUSES_SHOW;
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("id", id);
        
        String json = HttpUtility.getInstance().executeNormalTask(
                HttpMethod.Get, url, map);
        
        Gson gson = new Gson();
        MessageBean value = null;
        try {
            value = gson.fromJson(json, MessageBean.class);
        }
        catch (JsonSyntaxException e) {
            AppLogger.e(e.getMessage());
        }
        return value;
    }
}
