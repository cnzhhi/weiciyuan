package me.ivps.android.myweibo.ui.loader;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.ivps.android.myweibo.bean.FavListBean;
import me.ivps.android.myweibo.dao.fav.FavListDao;
import me.ivps.android.myweibo.support.error.WeiboException;

import android.content.Context;

/**
 * User: qii Date: 13-5-15
 */
public class MyFavMsgLoader extends
        AbstractAsyncNetRequestTaskLoader<FavListBean> {
    
    private static Lock lock = new ReentrantLock();
    
    private String token;
    private String page;
    
    public MyFavMsgLoader(Context context, String token, String page) {
        super(context);
        this.token = token;
        this.page = page;
    }
    
    public FavListBean loadData() throws WeiboException {
        FavListDao dao = new FavListDao(token);
        dao.setPage(page);
        FavListBean result = null;
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
