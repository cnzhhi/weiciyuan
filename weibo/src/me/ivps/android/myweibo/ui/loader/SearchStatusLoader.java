package me.ivps.android.myweibo.ui.loader;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.ivps.android.myweibo.bean.SearchStatusListBean;
import me.ivps.android.myweibo.dao.search.SearchDao;
import me.ivps.android.myweibo.support.error.WeiboException;

import android.content.Context;

/**
 * User: qii Date: 13-5-12
 */
public class SearchStatusLoader extends
        AbstractAsyncNetRequestTaskLoader<SearchStatusListBean> {
    
    private static Lock lock = new ReentrantLock();
    
    private String token;
    private String searchWord;
    private String page;
    
    public SearchStatusLoader(Context context, String token, String searchWord,
            String page) {
        super(context);
        this.token = token;
        this.searchWord = searchWord;
        this.page = page;
    }
    
    public SearchStatusListBean loadData() throws WeiboException {
        SearchDao dao = new SearchDao(token, searchWord);
        dao.setPage(page);
        
        SearchStatusListBean result = null;
        lock.lock();
        
        try {
            result = dao.getStatusList();
        }
        finally {
            lock.unlock();
        }
        
        return result;
    }
}
