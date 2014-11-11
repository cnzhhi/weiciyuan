package me.ivps.android.myweibo.dao.maintimeline;

import java.util.HashMap;
import java.util.Map;

import me.ivps.android.myweibo.bean.GroupListBean;
import me.ivps.android.myweibo.dao.URLHelper;
import me.ivps.android.myweibo.support.debug.AppLogger;
import me.ivps.android.myweibo.support.error.WeiboException;
import me.ivps.android.myweibo.support.http.HttpMethod;
import me.ivps.android.myweibo.support.http.HttpUtility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * User: qii Date: 12-10-17
 */
public class FriendGroupDao {
    
    public GroupListBean getGroup() throws WeiboException {
        
        String url = URLHelper.FRIENDSGROUP_INFO;
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        
        String jsonData = HttpUtility.getInstance().executeNormalTask(
                HttpMethod.Get, url, map);
        
        Gson gson = new Gson();
        
        GroupListBean value = null;
        try {
            value = gson.fromJson(jsonData, GroupListBean.class);
        }
        catch (JsonSyntaxException e) {
            AppLogger.e(e.getMessage());
        }
        
        return value;
    }
    
    public FriendGroupDao(String token) {
        this.access_token = token;
    }
    
    private String access_token;
}
