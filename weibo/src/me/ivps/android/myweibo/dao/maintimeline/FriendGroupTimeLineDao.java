package me.ivps.android.myweibo.dao.maintimeline;

import java.util.HashMap;
import java.util.Map;

import me.ivps.android.myweibo.bean.MessageListBean;
import me.ivps.android.myweibo.dao.URLHelper;
import me.ivps.android.myweibo.support.debug.AppLogger;
import me.ivps.android.myweibo.support.error.WeiboException;
import me.ivps.android.myweibo.support.http.HttpMethod;
import me.ivps.android.myweibo.support.http.HttpUtility;
import me.ivps.android.myweibo.support.utils.TimeLineUtility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * User: qii Date: 12-10-17
 */
public class FriendGroupTimeLineDao extends MainFriendsTimeLineDao {
    
    protected String getUrl() {
        return URLHelper.FRIENDSGROUP_TIMELINE;
    }
    
    private String getMsgListJson() throws WeiboException {
        String url = getUrl();
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("since_id", since_id);
        map.put("max_id", max_id);
        map.put("count", count);
        map.put("page", page);
        map.put("base_app", base_app);
        map.put("feature", feature);
        map.put("trim_user", trim_user);
        map.put("list_id", list_id);
        
        String jsonData = HttpUtility.getInstance().executeNormalTask(
                HttpMethod.Get, url, map);
        
        return jsonData;
    }
    
    public MessageListBean getGSONMsgList() throws WeiboException {
        
        String json = getMsgListJson();
        Gson gson = new Gson();
        
        MessageListBean value = null;
        try {
            value = gson.fromJson(json, MessageListBean.class);
        }
        catch (JsonSyntaxException e) {
            
            AppLogger.e(e.getMessage());
            return null;
        }
        if (value != null && value.getItemList().size() > 0) {
            TimeLineUtility.filterMessage(value);
        }
        
        return value;
    }
    
    public FriendGroupTimeLineDao(String access_token, String list_id) {
        
        super(access_token);
        this.list_id = list_id;
    }
    
    private String list_id;
}
