package me.ivps.android.myweibo.dao.topic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.ivps.android.myweibo.bean.MessageBean;
import me.ivps.android.myweibo.bean.TopicResultListBean;
import me.ivps.android.myweibo.dao.URLHelper;
import me.ivps.android.myweibo.support.debug.AppLogger;
import me.ivps.android.myweibo.support.error.WeiboException;
import me.ivps.android.myweibo.support.http.HttpMethod;
import me.ivps.android.myweibo.support.http.HttpUtility;
import me.ivps.android.myweibo.support.settinghelper.SettingUtility;
import me.ivps.android.myweibo.support.utils.TimeUtility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * User: qii Date: 12-9-26
 */
public class SearchTopicDao {
    
    protected String getUrl() {
        return URLHelper.TOPIC_SEARCH;
    }
    
    private String getMsgListJson() throws WeiboException {
        String url = getUrl();
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("q", q);
        map.put("count", count);
        map.put("page", page);
        
        String jsonData = HttpUtility.getInstance().executeNormalTask(
                HttpMethod.Get, url, map);
        
        return jsonData;
    }
    
    public TopicResultListBean getGSONMsgList() throws WeiboException {
        
        String json = getMsgListJson();
        Gson gson = new Gson();
        
        TopicResultListBean value = null;
        try {
            value = gson.fromJson(json, TopicResultListBean.class);
        }
        catch (JsonSyntaxException e) {
            
            AppLogger.e(e.getMessage());
            return null;
        }
        if (value != null && value.getStatuses() != null
                && value.getStatuses().size() > 0) {
            List<MessageBean> msgList = value.getStatuses();
            Iterator<MessageBean> iterator = msgList.iterator();
            
            while (iterator.hasNext()) {
                MessageBean msg = iterator.next();
                if (msg.getUser() == null) {
                    iterator.remove();
                }
                else {
                    msg.getListViewSpannableString();
                    TimeUtility.dealMills(msg);
                }
            }
        }
        
        return value;
    }
    
    public SearchTopicDao(String token, String q) {
        this.access_token = token;
        this.q = q;
        this.count = SettingUtility.getMsgCount();
    }
    
    private String access_token;
    private String q;
    private String count;
    private String page;
    
    public String getCount() {
        return count;
    }
    
    public void setCount(String count) {
        this.count = count;
    }
    
    public String getPage() {
        return page;
    }
    
    public void setPage(String page) {
        this.page = page;
    }
}
