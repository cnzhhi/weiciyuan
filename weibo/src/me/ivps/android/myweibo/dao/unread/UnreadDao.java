package me.ivps.android.myweibo.dao.unread;

import java.util.HashMap;
import java.util.Map;

import me.ivps.android.myweibo.bean.UnreadBean;
import me.ivps.android.myweibo.dao.URLHelper;
import me.ivps.android.myweibo.support.debug.AppLogger;
import me.ivps.android.myweibo.support.error.WeiboException;
import me.ivps.android.myweibo.support.http.HttpMethod;
import me.ivps.android.myweibo.support.http.HttpUtility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * User: qii Date: 12-9-26
 */
public class UnreadDao {
    
    protected String getUrl() {
        return URLHelper.UNREAD_COUNT;
    }
    
    private String getMsgListJson() throws WeiboException {
        String url = getUrl();
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("uid", uid);
        
        String jsonData = HttpUtility.getInstance().executeNormalTask(
                HttpMethod.Get, url, map);
        
        return jsonData;
    }
    
    public UnreadBean getCount() throws WeiboException {
        String json = getMsgListJson();
        Gson gson = new Gson();
        
        UnreadBean value = null;
        try {
            value = gson.fromJson(json, UnreadBean.class);
        }
        catch (JsonSyntaxException e) {
            
            AppLogger.e(e.getMessage());
            return null;
        }
        
        return value;
    }
    
    private String access_token;
    private String uid;
    
    public UnreadDao(String access_token, String uid) {
        this.access_token = access_token;
        this.uid = uid;
    }
}
