package me.ivps.android.myweibo.ui.loader;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.ivps.android.myweibo.bean.UserListBean;
import me.ivps.android.myweibo.dao.search.SearchDao;
import me.ivps.android.myweibo.support.error.WeiboException;

import android.content.Context;

/**
 * User: qii Date: 13-5-12
 */
public class SearchUserLoader extends
        AbstractAsyncNetRequestTaskLoader<UserListBean> {
    
    private static Lock lock = new ReentrantLock();
    
    private String token;
    private String searchWord;
    private String page;
    
    public SearchUserLoader(Context context, String token, String searchWord,
            String page) {
        super(context);
        this.token = token;
        this.searchWord = searchWord;
        this.page = page;
    }
    
    public UserListBean loadData() throws WeiboException {
        SearchDao dao = new SearchDao(token, searchWord);
        dao.setPage(page);
        
        UserListBean result = null;
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
