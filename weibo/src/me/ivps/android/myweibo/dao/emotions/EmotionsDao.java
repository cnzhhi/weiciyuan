package me.ivps.android.myweibo.dao.emotions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ivps.android.myweibo.bean.EmotionBean;
import me.ivps.android.myweibo.dao.URLHelper;
import me.ivps.android.myweibo.support.debug.AppLogger;
import me.ivps.android.myweibo.support.error.WeiboException;
import me.ivps.android.myweibo.support.http.HttpMethod;
import me.ivps.android.myweibo.support.http.HttpUtility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * User: qii Date: 12-9-24
 */
public class EmotionsDao {
    
    public EmotionsDao(String token) {
        this.access_token = token;
    }
    
    public List<EmotionBean> getEmotions() throws WeiboException {
        String url = URLHelper.EMOTIONS;
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("type", type);
        map.put("language", language);
        
        String jsonData = HttpUtility.getInstance().executeNormalTask(
                HttpMethod.Get, url, map);
        
        Gson gson = new Gson();
        
        List<EmotionBean> value = null;
        try {
            value = gson.fromJson(jsonData,
                    new TypeToken<ArrayList<EmotionBean>>() {
                    }.getType());
        }
        catch (JsonSyntaxException e) {
            AppLogger.e(e.getMessage());
        }
        return value;
    }
    
    private String access_token;
    private String type;
    private String language;
}
