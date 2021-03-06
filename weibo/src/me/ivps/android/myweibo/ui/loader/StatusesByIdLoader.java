package me.ivps.android.myweibo.ui.loader;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.ivps.android.myweibo.bean.MessageListBean;
import me.ivps.android.myweibo.dao.user.StatusesTimeLineDao;
import me.ivps.android.myweibo.support.error.WeiboException;

import android.content.Context;
import android.text.TextUtils;

/**
 * User: qii Date: 13-5-12
 */
public class StatusesByIdLoader extends
        AbstractAsyncNetRequestTaskLoader<MessageListBean> {
    
    private static Lock lock = new ReentrantLock();
    
    private String token;
    private String sinceId;
    private String maxId;
    private String screenName;
    private String uid;
    private String count;
    
    public StatusesByIdLoader(Context context, String uid, String screenName,
            String token, String sinceId, String maxId) {
        super(context);
        this.token = token;
        this.sinceId = sinceId;
        this.maxId = maxId;
        this.uid = uid;
        this.screenName = screenName;
    }
    
    public StatusesByIdLoader(Context context, String uid, String screenName,
            String token, String sinceId, String maxId, String count) {
        this(context, uid, screenName, token, sinceId, maxId);
        this.count = count;
    }
    
    public MessageListBean loadData() throws WeiboException {
        StatusesTimeLineDao dao = new StatusesTimeLineDao(token, uid);
        
        if (TextUtils.isEmpty(uid)) {
            dao.setScreen_name(screenName);
        }
        
        if (!TextUtils.isEmpty(count)) {
            dao.setCount(count);
        }
        
        dao.setSince_id(sinceId);
        dao.setMax_id(maxId);
        MessageListBean result = null;
        
        lock.lock();
        
        try {
            result = dao.getGSONMsgList();
        }
        finally {
            lock.unlock();
        }
        
        return result;
    }
}
