package me.ivps.android.myweibo.dao.group;

import java.util.HashMap;
import java.util.Map;

import me.ivps.android.myweibo.bean.UserBean;
import me.ivps.android.myweibo.dao.URLHelper;
import me.ivps.android.myweibo.support.debug.AppLogger;
import me.ivps.android.myweibo.support.error.WeiboException;
import me.ivps.android.myweibo.support.http.HttpMethod;
import me.ivps.android.myweibo.support.http.HttpUtility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * User: qii Date: 12-11-6
 */
public class ModifyGroupMemberDao {
    
    public void add(String list_id) throws WeiboException {
        this.list_id = list_id;
        executeTask(URLHelper.GROUP_MEMBER_ADD);
    }
    
    public void delete(String list_id) throws WeiboException {
        this.list_id = list_id;
        executeTask(URLHelper.GROUP_MEMBER_DESTROY);
    }
    
    private UserBean executeTask(String url) throws WeiboException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("uid", uid);
        map.put("list_id", list_id);
        
        String jsonData = HttpUtility.getInstance().executeNormalTask(
                HttpMethod.Post, url, map);
        
        Gson gson = new Gson();
        
        UserBean value = null;
        try {
            value = gson.fromJson(jsonData, UserBean.class);
        }
        catch (JsonSyntaxException e) {
            AppLogger.e(e.getMessage());
        }
        return value;
    }
    
    public ModifyGroupMemberDao(String token, String uid) {
        this.access_token = token;
        this.uid = uid;
    }
    
    private String access_token;
    private String uid;
    private String list_id;
}
