package me.ivps.android.myweibo.ui.loader;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.ivps.android.myweibo.bean.DMUserListBean;
import me.ivps.android.myweibo.dao.dm.DMDao;
import me.ivps.android.myweibo.support.error.WeiboException;

import android.content.Context;

/**
 * User: qii Date: 13-5-15
 */
public class DMUserLoader extends
        AbstractAsyncNetRequestTaskLoader<DMUserListBean> {
    
    private static Lock lock = new ReentrantLock();
    
    private String token;
    private String cursor;
    
    public DMUserLoader(Context context, String token, String cursor) {
        super(context);
        this.token = token;
        this.cursor = cursor;
    }
    
    public DMUserListBean loadData() throws WeiboException {
        DMDao dao = new DMDao(token);
        dao.setCursor(cursor);
        
        DMUserListBean result = null;
        lock.lock();
        
        try {
            result = dao.getUserList();
        }
        finally {
            lock.unlock();
        }
        return result;
    }
}
