package me.ivps.android.myweibo.ui.loader;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.ivps.android.myweibo.bean.DMListBean;
import me.ivps.android.myweibo.dao.dm.DMConversationDao;
import me.ivps.android.myweibo.support.error.WeiboException;

import android.content.Context;

/**
 * User: qii Date: 13-5-15
 */
public class DMConversationLoader extends
        AbstractAsyncNetRequestTaskLoader<DMListBean> {
    
    private static Lock lock = new ReentrantLock();
    
    private String token;
    private String uid;
    private String page;
    
    public DMConversationLoader(Context context, String token, String uid,
            String page) {
        super(context);
        this.token = token;
        this.uid = uid;
        this.page = page;
    }
    
    public DMListBean loadData() throws WeiboException {
        DMConversationDao dao = new DMConversationDao(token);
        dao.setPage(Integer.valueOf(page));
        dao.setUid(uid);
        
        DMListBean result = null;
        lock.lock();
        
        try {
            result = dao.getConversationList();
        }
        finally {
            lock.unlock();
        }
        return result;
    }
}
