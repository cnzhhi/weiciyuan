package me.ivps.android.myweibo.ui.loader;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.ivps.android.myweibo.bean.UserListBean;
import me.ivps.android.myweibo.dao.user.FriendListDao;
import me.ivps.android.myweibo.support.error.WeiboException;

import android.content.Context;

/**
 * User: qii Date: 13-5-12
 */
public class FriendUserLoader extends
        AbstractAsyncNetRequestTaskLoader<UserListBean> {
    
    private static Lock lock = new ReentrantLock();
    
    private String token;
    private String uid;
    private String page;
    private String cursor;
    
    public FriendUserLoader(Context context, String token, String uid,
            String cursor) {
        super(context);
        this.token = token;
        this.uid = uid;
        this.cursor = cursor;
    }
    
    public UserListBean loadData() throws WeiboException {
        FriendListDao dao = new FriendListDao(token, uid);
        dao.setCursor(cursor);
        
        UserListBean result = null;
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
