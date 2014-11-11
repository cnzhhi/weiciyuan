package me.ivps.android.myweibo.dao.login;

import java.util.HashMap;
import java.util.Map;

import me.ivps.android.myweibo.bean.AccountBean;
import me.ivps.android.myweibo.dao.URLHelper;
import me.ivps.android.myweibo.support.error.WeiboException;
import me.ivps.android.myweibo.support.http.HttpMethod;
import me.ivps.android.myweibo.support.http.HttpUtility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: qii Date: 12-11-29 sina weibo dont allow third apps use this api, the
 * result is always error
 */
public class RefreshOAuthDao {
    
    public AccountBean refreshToken() throws WeiboException {
        String url = URLHelper.OAUTH2_ACCESS_TOKEN;
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", code);
        map.put("redirect_uri", redirect_uri);
        map.put("client_id", client_id);
        map.put("client_secret", client_secret);
        map.put("grant_type", grant_type);
        
        String jsonData = HttpUtility.getInstance().executeNormalTask(
                HttpMethod.Post, url, map);
        
        if ((jsonData != null) && (jsonData.contains("{"))) {
            try {
                JSONObject localJSONObject = new JSONObject(jsonData);
                
                String access_token = localJSONObject.optString("access_token");
                long expire_in = localJSONObject.optLong("expires_in");
                
                AccountBean accountBean = new AccountBean();
                accountBean.setAccess_token(access_token);
                accountBean.setExpires_time(expire_in);
                
                return accountBean;
            }
            catch (JSONException localJSONException) {
                
            }
        }
        
        return null;
    }
    
    public RefreshOAuthDao(String code) {
        this.code = code;
    }
    
    private String code;
    private String redirect_uri = URLHelper.DIRECT_URL;
    private String client_id = URLHelper.APP_KEY;
    private String client_secret = URLHelper.APP_SECRET;
    private String grant_type = "authorization_code";
}
