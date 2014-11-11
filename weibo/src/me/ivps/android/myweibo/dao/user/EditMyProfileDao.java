package me.ivps.android.myweibo.dao.user;

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
 * User: qii Date: 13-3-2
 */
public class EditMyProfileDao {
    
    public UserBean update() throws WeiboException {
        
        String apiUrl = URLHelper.MYPROFILE_EDIT;
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("screen_name", screen_name);
        map.put("url", url);
        map.put("description", description);
        
        String jsonData = HttpUtility.getInstance().executeNormalTask(
                HttpMethod.Post, apiUrl, map);
        
        Gson gson = new Gson();
        
        UserBean value = null;
        try {
            value = gson.fromJson(jsonData, UserBean.class);
        }
        catch (JsonSyntaxException e) {
            AppLogger.e(e.getMessage());
        }
        
        if (this.avatar != null) {
            UploadAvatarDao uploadAvatarDao = new UploadAvatarDao(access_token,
                    avatar);
            uploadAvatarDao.upload();
        }
        
        return value;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public EditMyProfileDao(String token, String screen_name) {
        this.access_token = token;
        this.screen_name = screen_name;
    }
    
    private String access_token;
    private String screen_name;
    private String url;
    private String description;
    private String avatar;
}
