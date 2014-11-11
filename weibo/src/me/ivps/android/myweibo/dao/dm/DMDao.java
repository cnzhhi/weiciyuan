package me.ivps.android.myweibo.dao.dm;

import java.util.HashMap;
import java.util.Map;

import me.ivps.android.myweibo.bean.DMUserBean;
import me.ivps.android.myweibo.bean.DMUserListBean;
import me.ivps.android.myweibo.dao.URLHelper;
import me.ivps.android.myweibo.support.debug.AppLogger;
import me.ivps.android.myweibo.support.error.WeiboException;
import me.ivps.android.myweibo.support.http.HttpMethod;
import me.ivps.android.myweibo.support.http.HttpUtility;
import me.ivps.android.myweibo.support.settinghelper.SettingUtility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * User: qii Date: 12-11-14
 */
public class DMDao {
    
    private String access_token;
    
    private String cursor = "0";
    
    private String count;
    
    public DMDao(String token) {
        this.access_token = token;
    }
    
    public void setCursor(String cursor) {
        this.cursor = cursor;
        this.count = SettingUtility.getMsgCount();
    }
    
    public DMUserListBean getUserList() throws WeiboException {
        String url = URLHelper.DM_USERLIST;
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("count", count);
        map.put("cursor", cursor);
        
        String jsonData = HttpUtility.getInstance().executeNormalTask(
                HttpMethod.Get, url, map);
        DMUserListBean value = null;
        try {
            value = new Gson().fromJson(jsonData, DMUserListBean.class);
            for (DMUserBean b : value.getItemList()) {
                if (!b.isMiddleUnreadItem()) {
                    b.getListViewSpannableString();
                    b.getListviewItemShowTime();
                }
            }
        }
        catch (JsonSyntaxException e) {
            
            AppLogger.e(e.getMessage());
        }
        return value;
    }
}
