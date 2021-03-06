package me.ivps.android.myweibo.dao.group;

import java.util.HashMap;
import java.util.Map;

import me.ivps.android.myweibo.bean.GroupBean;
import me.ivps.android.myweibo.dao.URLHelper;
import me.ivps.android.myweibo.support.debug.AppLogger;
import me.ivps.android.myweibo.support.error.WeiboException;
import me.ivps.android.myweibo.support.http.HttpMethod;
import me.ivps.android.myweibo.support.http.HttpUtility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * User: qii Date: 13-2-15
 * http://open.weibo.com/wiki/2/friendships/groups/create
 */
public class CreateGroupDao {
    
    public GroupBean create() throws WeiboException {
        
        String url = URLHelper.GROUP_CREATE;
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("name", name);
        
        String jsonData = HttpUtility.getInstance().executeNormalTask(
                HttpMethod.Post, url, map);
        
        Gson gson = new Gson();
        
        GroupBean value = null;
        try {
            value = gson.fromJson(jsonData, GroupBean.class);
        }
        catch (JsonSyntaxException e) {
            AppLogger.e(e.getMessage());
        }
        
        return value;
    }
    
    public CreateGroupDao(String token, String name) {
        this.access_token = token;
        this.name = name;
    }
    
    private String access_token;
    private String name;
}
