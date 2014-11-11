package me.ivps.android.myweibo.dao.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ivps.android.myweibo.bean.GroupBean;
import me.ivps.android.myweibo.dao.URLHelper;
import me.ivps.android.myweibo.support.debug.AppLogger;
import me.ivps.android.myweibo.support.error.WeiboException;
import me.ivps.android.myweibo.support.http.HttpMethod;
import me.ivps.android.myweibo.support.http.HttpUtility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * User: qii Date: 12-11-6
 */
public class GroupListDao {
    
    public List<String> getInfo() throws WeiboException {
        
        String url = URLHelper.GROUP_MEMBER_LIST;
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("uids", uids);
        
        String jsonData = HttpUtility.getInstance().executeNormalTask(
                HttpMethod.Get, url, map);
        
        Gson gson = new Gson();
        
        List<GroupUser> value = null;
        try {
            value = gson.fromJson(jsonData, new TypeToken<List<GroupUser>>() {
            }.getType());
        }
        catch (JsonSyntaxException e) {
            AppLogger.e(e.getMessage());
        }
        
        if (value != null && value.size() > 0) {
            GroupUser user = value.get(0);
            List<String> ids = new ArrayList<String>();
            for (GroupBean b : user.lists) {
                ids.add(b.getIdstr());
            }
            return ids;
        }
        
        return null;
    }
    
    public GroupListDao(String token, String uids) {
        this.access_token = token;
        this.uids = uids;
    }
    
    private String access_token;
    private String uids;
    
    class GroupUser {
        String uid;
        List<GroupBean> lists;
    }
}
