package me.ivps.android.myweibo.ui.loader;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.ivps.android.myweibo.bean.CommentListBean;
import me.ivps.android.myweibo.dao.timeline.CommentsTimeLineByIdDao;
import me.ivps.android.myweibo.support.error.WeiboException;

import android.content.Context;

/**
 * User: qii Date: 13-5-15
 */
public class CommentsByIdMsgLoader extends
        AbstractAsyncNetRequestTaskLoader<CommentListBean> {
    
    private static Lock lock = new ReentrantLock();
    
    private String token;
    private String sinceId;
    private String maxId;
    private String id;
    
    public CommentsByIdMsgLoader(Context context, String id, String token,
            String sinceId, String maxId) {
        super(context);
        this.token = token;
        this.sinceId = sinceId;
        this.maxId = maxId;
        this.id = id;
    }
    
    public CommentListBean loadData() throws WeiboException {
        CommentsTimeLineByIdDao dao = new CommentsTimeLineByIdDao(token, id);
        dao.setSince_id(sinceId);
        dao.setMax_id(maxId);
        CommentListBean result = null;
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
