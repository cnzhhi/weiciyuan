package me.ivps.android.myweibo.dao.group;

import java.util.HashMap;
import java.util.Map;

import me.ivps.android.myweibo.dao.URLHelper;
import me.ivps.android.myweibo.support.debug.AppLogger;
import me.ivps.android.myweibo.support.error.WeiboException;
import me.ivps.android.myweibo.support.http.HttpMethod;
import me.ivps.android.myweibo.support.http.HttpUtility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * User: qii Date: 13-2-16
 */
public class DestroyGroupDao {
    
    public boolean destroy() throws WeiboException {
        
        String url = URLHelper.GROUP_DESTROY;
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("list_id", list_id);
        
        String jsonData = HttpUtility.getInstance().executeNormalTask(
                HttpMethod.Post, url, map);
        
        Gson gson = new Gson();
        
        Result value = null;
        try {
            value = gson.fromJson(jsonData, Result.class);
        }
        catch (JsonSyntaxException e) {
            AppLogger.e(e.getMessage());
        }
        
        return (value != null);
    }
    
    /**
     * http://open.weibo.com/wiki/2/friendships/groups/destroy suggest use idstr
     */
    
    public DestroyGroupDao(String token, String list_id) {
        this.access_token = token;
        this.list_id = list_id;
    }
    
    private String access_token;
    private String list_id;
    
    private class Result {
        String id;
        String idstr;
        String name;
    }
}
